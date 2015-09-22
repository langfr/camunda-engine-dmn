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

package org.camunda.bpm.dmn.engine.feel;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.dmn.engine.DmnDecision;
import org.camunda.bpm.dmn.engine.DmnDecisionOutput;
import org.camunda.bpm.dmn.engine.DmnDecisionResult;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.dmn.engine.impl.feel.FeelFunctionMapper;
import org.camunda.bpm.dmn.engine.test.DecisionResource;
import org.camunda.bpm.dmn.engine.test.DmnEngineRule;
import org.junit.Before;
import org.junit.Test;

public abstract class FeelTheJuelTest {

  public static final String BOOLEAN_DMN = "org/camunda/bpm/dmn/engine/feel/feelBooleanTest.dmn";
  public static final String STRING_DMN = "org/camunda/bpm/dmn/engine/feel/feelStringTest.dmn";
  public static final String NUMBER_DMN = "org/camunda/bpm/dmn/engine/feel/feelNumberTest.dmn";
  public static final String DATE_DMN = "org/camunda/bpm/dmn/engine/feel/feelDateTest.dmn";

  public DmnEngine engine;
  public DmnDecision decision;
  public Map<String, Object> variables;

  @Before
  public void initEngineAndDecision() {
    engine = getDmnEngineRule().getEngine();
    decision = getDmnEngineRule().getDecision();
    variables = new HashMap<String, Object>();
  }

  public abstract DmnEngineRule getDmnEngineRule();

  @Test
  @DecisionResource(resource = BOOLEAN_DMN)
  public void testBooleanTrue () {
    variables.put("a", true);
    variables.put("b", false);

    assertResults(true,
      "Boolean Constant Equals True",
      "Boolean Constant Not Equals False",
      "Boolean Constant List Equals",
      "Boolean Variable Equals",
      "Boolean Variable List Equals",
      "Boolean Mixed List Equals"
    );
  }

  @Test
  @DecisionResource(resource = BOOLEAN_DMN)
  public void testBooleanFalse () {
    variables.put("a", true);
    variables.put("b", false);

    assertResults(false,
      "Boolean Constant Not Equals True",
      "Boolean Constant Equals False",
      "Boolean Variable Not Equals",
      "Boolean Constant List Equals",
      "Boolean Variable List Equals",
      "Boolean Mixed List Equals"
    );
  }

  @Test
  @DecisionResource(resource = STRING_DMN)
  public void testStringVip() {
    variables.put("a", "VIP");
    variables.put("b", "Gold");
    variables.put("c", "Silver");

    assertResults("VIP",
      "String Constant Equals",
      "String Variable Equals",
      "String Constant List Equals",
      "String Variable List Equals",
      "String Mixed List Equals"
    );
  }

  @Test
  @DecisionResource(resource = STRING_DMN)
  public void testStringGold() {
    variables.put("a", "VIP");
    variables.put("b", "Gold");
    variables.put("c", "Silver");

    assertResults("Gold",
      "String Constant Not Equals",
      "String Variable Not Equals",
      "String Constant List Equals",
      "String Variable List Equals",
      "String Mixed List Equals"
    );
  }

  @Test
  @DecisionResource(resource = STRING_DMN)
  public void testStringCamunda() {
    variables.put("a", "VIP");
    variables.put("b", "Gold");
    variables.put("c", "Silver");

    assertResults("Camunda",
      "String Constant Not Equals",
      "String Variable Not Equals",
      "String Constant List Not Equals",
      "String Variable Not Equals",
      "String Mixed List Not Equals"
    );
  }

  @Test
  @DecisionResource(resource = NUMBER_DMN)
  public void testNumberMinus12() {
    variables.put("x", 13.37);
    variables.put("y", 23.42);
    variables.put("v", 20);
    variables.put("w", 21.5);

    assertResults(-12,
      "Number Constant Not Equals",
      "Number Constant Greater",
      "Number Constant Greater Equals",
      "Number Constant Not Less",
      "Number Constant Not Less Equals",
      "Number Constant Not Range []",
      "Number Constant Not Range (]",
      "Number Constant Not Range ]]",
      "Number Constant Not Range [)",
      "Number Constant Not Range [[",
      "Number Constant Not Range ()",
      "Number Constant Not Range ([",
      "Number Constant Not Range ])",
      "Number Constant Not Range ][",
      "Number Constant List Not Equals",
      "Number Constant List Comparison",
      "Number Constant List Not Mixed",
      "Number Variable Not Equals",
      "Number Variable Greater",
      "Number Variable Greater Equals",
      "Number Variable Not Less",
      "Number Variable Not Less Equals",
      "Number Variable Not Range []",
      "Number Variable Not Range (]",
      "Number Variable Not Range ]]",
      "Number Variable Not Range [)",
      "Number Variable Not Range [[",
      "Number Variable Not Range ()",
      "Number Variable Not Range ([",
      "Number Variable Not Range ])",
      "Number Variable Not Range ][",
      "Number Variable List Not Equals",
      "Number Variable List Comparison",
      "Number Variable List Not Mixed",
      "Number Mixed Not Range []",
      "Number Mixed Not Range (]",
      "Number Mixed Not Range ]]",
      "Number Mixed Not Range [)",
      "Number Mixed Not Range [[",
      "Number Mixed Not Range ()",
      "Number Mixed Not Range ([",
      "Number Mixed Not Range ])",
      "Number Mixed Not Range ][",
      "Number Mixed List Not Equals",
      "Number Mixed List Comparison",
      "Number Mixed List Not Mixed"
    );
  }

  @Test
  @DecisionResource(resource = NUMBER_DMN)
  public void testNumber1337() {
    variables.put("x", 13.37);
    variables.put("y", 23.42);
    variables.put("v", 20);
    variables.put("w", 21.5);

    assertResults(13.37,
      "Number Constant Equals",
      "Number Constant Not Greater",
      "Number Constant Greater Equals",
      "Number Constant Not Less",
      "Number Constant Less Equals",
      "Number Constant Range []",
      "Number Constant Not Range (]",
      "Number Constant Not Range ]]",
      "Number Constant Range [)",
      "Number Constant Range [[",
      "Number Constant Not Range ()",
      "Number Constant Not Range ([",
      "Number Constant Not Range ])",
      "Number Constant Not Range ][",
      "Number Constant List Equals",
      "Number Constant List Not Comparison",
      "Number Constant List Mixed",
      "Number Variable Equals",
      "Number Variable Not Greater",
      "Number Variable Greater Equals",
      "Number Variable Not Less",
      "Number Variable Less Equals",
      "Number Variable Range []",
      "Number Variable Not Range (]",
      "Number Variable Not Range ]]",
      "Number Variable Range [)",
      "Number Variable Range [[",
      "Number Variable Not Range ()",
      "Number Variable Not Range ([",
      "Number Variable Not Range ])",
      "Number Variable Not Range ][",
      "Number Variable List Equals",
      "Number Variable List Not Comparison",
      "Number Variable List Mixed",
      "Number Mixed Range []",
      "Number Mixed Not Range (]",
      "Number Mixed Not Range ]]",
      "Number Mixed Range [)",
      "Number Mixed Range [[",
      "Number Mixed Not Range ()",
      "Number Mixed Not Range ([",
      "Number Mixed Not Range ])",
      "Number Mixed Not Range ][",
      "Number Mixed List Equals",
      "Number Mixed List Not Comparison",
      "Number Mixed List Mixed"
    );
  }

  @Test
  @DecisionResource(resource = NUMBER_DMN)
  public void testNumber2342() {
    variables.put("x", 13.37);
    variables.put("y", 23.42);
    variables.put("v", 20);
    variables.put("w", 21.5);

    assertResults(23.42,
      "Number Constant Not Equals",
      "Number Constant Not Greater",
      "Number Constant Not Greater Equals",
      "Number Constant Less",
      "Number Constant Less Equals",
      "Number Constant Range []",
      "Number Constant Range (]",
      "Number Constant Range ]]",
      "Number Constant Not Range [)",
      "Number Constant Not Range [[",
      "Number Constant Not Range ()",
      "Number Constant Not Range ([",
      "Number Constant Not Range ])",
      "Number Constant Not Range ][",
      "Number Constant List Equals",
      "Number Constant List Comparison",
      "Number Constant List Mixed",
      "Number Variable Not Equals",
      "Number Variable Not Greater",
      "Number Variable Not Greater Equals",
      "Number Variable Less",
      "Number Variable Less Equals",
      "Number Variable Range []",
      "Number Variable Range (]",
      "Number Variable Range ]]",
      "Number Variable Not Range [)",
      "Number Variable Not Range [[",
      "Number Variable Not Range ()",
      "Number Variable Not Range ([",
      "Number Variable Not Range ])",
      "Number Variable Not Range ][",
      "Number Variable List Equals",
      "Number Variable List Comparison",
      "Number Variable List Mixed",
      "Number Mixed Range []",
      "Number Mixed Range (]",
      "Number Mixed Range ]]",
      "Number Mixed Not Range [)",
      "Number Mixed Not Range [[",
      "Number Mixed Not Range ()",
      "Number Mixed Not Range ([",
      "Number Mixed Not Range ])",
      "Number Mixed Not Range ][",
      "Number Mixed List Equals",
      "Number Mixed List Comparison",
      "Number Mixed List Mixed"
    );
  }

  @Test
  @DecisionResource(resource = DATE_DMN)
  public void testDate20151212() throws ParseException {
    variables.put("x", FeelFunctionMapper.parseDate("2015-12-12"));
    variables.put("y", FeelFunctionMapper.parseDate("2016-06-06"));
    variables.put("v", FeelFunctionMapper.parseDate("2016-01-01"));
    variables.put("w", FeelFunctionMapper.parseDate("2016-04-04"));

    Date date = FeelFunctionMapper.parseDate("2015-12-12");
    assertResults(date,
      "Date Constant Equals",
      "Date Constant Not Greater",
      "Date Constant Greater Equals",
      "Date Constant Not Less",
      "Date Constant Less Equals",
      "Date Constant Range []",
      "Date Constant Not Range (]",
      "Date Constant Not Range ]]",
      "Date Constant Range [)",
      "Date Constant Range [[",
      "Date Constant Not Range ()",
      "Date Constant Not Range ([",
      "Date Constant Not Range ])",
      "Date Constant Not Range ][",
      "Date Constant List Equals",
      "Date Constant List Not Comparison",
      "Date Constant List Mixed",
      "Date Variable Equals",
      "Date Variable Not Greater",
      "Date Variable Greater Equals",
      "Date Variable Not Less",
      "Date Variable Less Equals",
      "Date Variable Range []",
      "Date Variable Not Range (]",
      "Date Variable Not Range ]]",
      "Date Variable Range [)",
      "Date Variable Range [[",
      "Date Variable Not Range ()",
      "Date Variable Not Range ([",
      "Date Variable Not Range ])",
      "Date Variable Not Range ][",
      "Date Variable List Equals",
      "Date Variable List Not Comparison",
      "Date Variable List Mixed",
      "Date Mixed Range []",
      "Date Mixed Not Range (]",
      "Date Mixed Not Range ]]",
      "Date Mixed Range [)",
      "Date Mixed Range [[",
      "Date Mixed Not Range ()",
      "Date Mixed Not Range ([",
      "Date Mixed Not Range ])",
      "Date Mixed Not Range ][",
      "Date Mixed List Equals",
      "Date Mixed List Not Comparison",
      "Date Mixed List Mixed"
    );
  }

  @Test
  @DecisionResource(resource = DATE_DMN)
  public void testDate20160606() throws ParseException {
    variables.put("x", FeelFunctionMapper.parseDate("2015-12-12"));
    variables.put("y", FeelFunctionMapper.parseDate("2016-06-06"));
    variables.put("v", FeelFunctionMapper.parseDate("2016-01-01"));
    variables.put("w", FeelFunctionMapper.parseDate("2016-04-04"));

    Date date = FeelFunctionMapper.parseDate("2016-06-06");
    assertResults(date,
      "Date Constant Not Equals",
      "Date Constant Not Greater",
      "Date Constant Not Greater Equals",
      "Date Constant Less",
      "Date Constant Less Equals",
      "Date Constant Range []",
      "Date Constant Range (]",
      "Date Constant Range ]]",
      "Date Constant Not Range [)",
      "Date Constant Not Range [[",
      "Date Constant Not Range ()",
      "Date Constant Not Range ([",
      "Date Constant Not Range ])",
      "Date Constant Not Range ][",
      "Date Constant List Equals",
      "Date Constant List Comparison",
      "Date Constant List Mixed",
      "Date Variable Not Equals",
      "Date Variable Not Greater",
      "Date Variable Not Greater Equals",
      "Date Variable Less",
      "Date Variable Less Equals",
      "Date Variable Range []",
      "Date Variable Range (]",
      "Date Variable Range ]]",
      "Date Variable Not Range [)",
      "Date Variable Not Range [[",
      "Date Variable Not Range ()",
      "Date Variable Not Range ([",
      "Date Variable Not Range ])",
      "Date Variable Not Range ][",
      "Date Variable List Equals",
      "Date Variable List Comparison",
      "Date Variable List Mixed",
      "Date Mixed Range []",
      "Date Mixed Range (]",
      "Date Mixed Range ]]",
      "Date Mixed Not Range [)",
      "Date Mixed Not Range [[",
      "Date Mixed Not Range ()",
      "Date Mixed Not Range ([",
      "Date Mixed Not Range ])",
      "Date Mixed Not Range ][",
      "Date Mixed List Equals",
      "Date Mixed List Comparison",
      "Date Mixed List Mixed"
    );
  }

  protected void assertResults(Object input, String... expectedResults) {
    variables.put("input", input);
    DmnDecisionResult result = engine.evaluate(decision, variables);
    List<String> collectedResults = new ArrayList<String>();
    for (DmnDecisionOutput output : result) {
      collectedResults.add(output.<String>getValue());
    }

    assertThat(collectedResults).containsOnly(expectedResults);
  }

}
