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

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.el.FunctionMapper;

public class FeelFunctionMapper extends FunctionMapper {

  protected FunctionMapper delegatingFunctionMapper;
  protected static final Map<String, Method> methodMap;

  static {
    methodMap = new HashMap<String, Method>();
    methodMap.put("date", getDateMethod());
    methodMap.put("compare", getCompareMethod());
    methodMap.put("interval", getIntervalMethod());
  }

  public FeelFunctionMapper() {
  }

  public FeelFunctionMapper(FunctionMapper delegatingFunctionMapper) {
    this.delegatingFunctionMapper = delegatingFunctionMapper;
  }

  public Method resolveFunction(String prefix, String localName) {
    if (methodMap.containsKey(localName)) {
      return methodMap.get(localName);
    }
    else if (delegatingFunctionMapper != null) {
      return delegatingFunctionMapper.resolveFunction(prefix, localName);
    }
    else {
      return null;
    }
  }

  public static Method getDateMethod() {
    try {
      return FeelFunctionMapper.class.getMethod("parseDate", String.class);
    }
    catch (Exception e) {
      throw new RuntimeException("Unable to find method 'parseDate'", e);
    }
  }

  public static Date parseDate(String dateString) throws ParseException {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      return dateFormat.parse(dateString);
  }

  public static Method getCompareMethod() {
    try {
      return FeelFunctionMapper.class.getMethod("feelCompare", Object.class, Object.class, String.class);
    }
    catch (Exception e) {
      throw new RuntimeException("Unable to find method 'feelCompare'", e);
    }
  }

  public static Boolean feelCompare(Object o1, Object o2, String operator) {
    if ("<".equals(operator)) {
      return feelCompareLess(o1, o2);
    }
    else if ("<=".equals(operator)) {
      return feelCompareLessEqual(o1, o2);
    }
    else if (">".equals(operator)) {
      return feelCompareGreater(o1, o2);
    }
    else if (">=".equals(operator)) {
      return feelCompareGreaterEqual(o1, o2);
    }
    else if ("==".equals(operator)){
      return feelCompareEqual(o1, o2);
    }
    else {
      throw new RuntimeException("Unknown comparator '" + operator + "'");
    }
  }

  public static Boolean feelCompareLess(Object o1, Object o2) {
    if (o1 == o2 || o1 == null || o2 == null) {
      return false;
    }
    else if (o1 instanceof Number && o2 instanceof Number) {
      return feelCompareNumbers((Number) o1, (Number) o2) < 0;
    }
    else if (o1 instanceof Comparable) {
      return ((Comparable)o1).compareTo(o2) < 0;
    }
    else if (o2 instanceof Comparable) {
      return ((Comparable)o2).compareTo(o1) > 0;
    }
    else {
      throw new RuntimeException("Unable to compare '" + o1 + "' and '" + o2 + "'");
    }
  }

  public static Boolean feelCompareLessEqual(Object o1, Object o2) {
    if (o1 == o2) {
      return true;
    }
    else if (o1 == null || o2 == null) {
      return false;
    }
    else if (o1 instanceof Number && o2 instanceof Number) {
      return feelCompareNumbers((Number) o1, (Number) o2) <= 0;
    }
    else if (o1 instanceof Comparable) {
      return ((Comparable)o1).compareTo(o2) <= 0;
    }
    else if (o2 instanceof Comparable) {
      return ((Comparable)o2).compareTo(o1) >= 0;
    }
    else {
      throw new RuntimeException("Unable to compare '" + o1 + "' and '" + o2 + "'");
    }
  }

  public static Boolean feelCompareGreater(Object o1, Object o2) {
    if (o1 == o2 || o1 == null || o2 == null) {
      return false;
    }
    else if (o1 instanceof Number && o2 instanceof Number) {
      return feelCompareNumbers((Number) o1, (Number) o2) > 0;
    }
    else if (o1 instanceof Comparable) {
      return ((Comparable)o1).compareTo(o2) > 0;
    }
    else if (o2 instanceof Comparable) {
      return ((Comparable)o2).compareTo(o1) < 0;
    }
    else {
      throw new RuntimeException("Unable to compare '" + o1 + "' and '" + o2 + "'");
    }
  }

  public static Boolean feelCompareGreaterEqual(Object o1, Object o2) {
    if (o1 == o2 ) {
      return true;
    }
    else if (o1 == null || o2 == null) {
			return false;
		}
    else if (o1 instanceof Number && o2 instanceof Number) {
      return feelCompareNumbers((Number) o1, (Number) o2) >= 0;
    }
    else if (o1 instanceof Comparable) {
      return ((Comparable)o1).compareTo(o2) >= 0;
    }
    else if (o2 instanceof Comparable) {
      return ((Comparable)o2).compareTo(o1) <= 0;
    }
    else {
      throw new RuntimeException("Unable to compare '" + o1 + "' and '" + o2 + "'");
    }
  }

  public static Boolean feelCompareEqual(Object o1, Object o2) {
    if (o1 == o2) {
      return true;
    }
    else if (o1 == null || o2 == null) {
      return false;
    }
    else if (o1 instanceof Number && o2 instanceof Number) {
      return feelCompareNumbers((Number) o1, (Number) o2) == 0;
    }
    else {
      return o1.equals(o2);
    }
  }

  public static int feelCompareNumbers(Number n1, Number n2) {
    Double d1 = n1.doubleValue();
    Double d2 = n2.doubleValue();
    return d1.compareTo(d2);
  }

  public static Method getIntervalMethod() {
    try {
      return FeelFunctionMapper.class.getMethod("feelInterval", Object.class, Object.class, Object.class, boolean.class, boolean.class);
    }
    catch (Exception e) {
      throw new RuntimeException("Unable to find method 'feelInterval'", e);
    }
  }

  public static Boolean feelInterval(Object input, Object lowerBound, Object upperBound, boolean lowerBoundInclusive, boolean upperBoundInclusive) {
    Boolean insideLowerBound = lowerBoundInclusive ? feelCompareGreaterEqual(input, lowerBound) : feelCompareGreater(input, lowerBound);
    Boolean insideUpperBound = upperBoundInclusive ? feelCompareLessEqual(input, upperBound) : feelCompareLess(input, upperBound);
    return insideLowerBound && insideUpperBound;
  }

}
