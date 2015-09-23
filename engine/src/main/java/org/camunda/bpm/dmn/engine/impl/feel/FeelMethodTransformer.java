/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.camunda.bpm.dmn.engine.impl.feel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FeelMethodTransformer {

  public static final Pattern COMPARISON_PATTERN = Pattern.compile("^(<=|>=|<|>)(.+)$");
  public static final Pattern INTERVAL_PATTERN = Pattern.compile("^(\\(|\\[|\\])(.*[^\\.])\\.\\.(.+)(\\)|\\]|\\[)$");
  public static final Pattern NOT_PATTERN = Pattern.compile("^not\\((.+)\\)$");
  public static final Pattern LIST_PATTERN = Pattern.compile("^([^,]+)(,[^,]+)+$");
  public static final Pattern LIST_ELEMENT_PATTERN = Pattern.compile(",([^,]+)");


  public static String transformFeel(String input, String feelExpression) {
    String expression = transformSimpleUnaryTests(input, feelExpression);
    return "${" + expression + "}";
  }

  public static String transformSimpleUnaryTests(String input, String simpleUnaryTests) {
    Matcher matcher = NOT_PATTERN.matcher(simpleUnaryTests);
    if (matcher.matches()) {
      return transformNotSimplePositiveUnaryTests(input, matcher);
    }

    return transformSimplePositiveUnaryTests(input, simpleUnaryTests);
  }

  public static String transformNotSimplePositiveUnaryTests(String input, Matcher matcher) {
    String simplePositiveUnaryTests = matcher.group(1);
    return "not(" + transformSimplePositiveUnaryTests(input, simplePositiveUnaryTests) + ")";
  }

  public static String transformSimplePositiveUnaryTests(String input, String simplePositiveUnaryTests) {
    Matcher matcher = LIST_PATTERN.matcher(simplePositiveUnaryTests);
    if (matcher.matches()) {
      return transformListSimplePositiveUnaryTest(input, simplePositiveUnaryTests, matcher);
    }

    return transformSimplePositiveUnaryTest(input, simplePositiveUnaryTests);
  }

  public static String transformListSimplePositiveUnaryTest(String input, String simplePositiveUnaryTests, Matcher matcher) {
    StringBuilder builder = new StringBuilder();
    builder.append(transformSimplePositiveUnaryTest(input, matcher.group(1)));
    Matcher elementMatcher = LIST_ELEMENT_PATTERN.matcher(simplePositiveUnaryTests);
    while (elementMatcher.find()) {
      builder.append(" || ").append(transformSimplePositiveUnaryTest(input, elementMatcher.group(1)));
    }
    return builder.toString();
  }

  public static String transformSimplePositiveUnaryTest(String input, String simplePositiveUnaryTest) {
    // comparison with endpoint
    Matcher matcher = COMPARISON_PATTERN.matcher(simplePositiveUnaryTest);
    if (matcher.matches()) {
      return transformComparisonEndpoint(input, matcher);
    }

    // interval
    matcher = INTERVAL_PATTERN.matcher(simplePositiveUnaryTest);
    if (matcher.matches()) {
      return transformInterval(input, matcher);
    }

    // default equal comparison
    return transformComparisonEndpoint(input, "==", simplePositiveUnaryTest);
  }

  public static String transformComparisonEndpoint(String input, Matcher matcher) {
    String operator = matcher.group(1);
    String endpoint = matcher.group(2);
    return transformComparisonEndpoint(input, operator, endpoint);
  }

  public static String transformComparisonEndpoint(String input, String operator, String endpoint) {
    return "compare(" + input + "," + transformEndpoint(endpoint) + ",\"" + operator + "\")";
  }

  public static String transformEndpoint(String endpoint) {
    return endpoint;
  }

  public static String transformInterval(String input, Matcher matcher) {
    String startIntervalSymbol = matcher.group(1);
    String lowerEndpoint = matcher.group(2);
    String upperEndpoint = matcher.group(3);
    String endIntervalSymbol = matcher.group(4);
    return transformInterval(input, startIntervalSymbol, lowerEndpoint, upperEndpoint, endIntervalSymbol);
  }

  public static String transformInterval(String input, String startIntervalSymbol, String lowerEndpoint, String upperEndpoint, String endIntervalSymbol) {
    return "interval(" + input + "," + transformEndpoint(lowerEndpoint) + "," + transformEndpoint(upperEndpoint) + "," + startIntervalSymbol.equals("[") + "," + endIntervalSymbol.equals("]") + ")";
  }

}
