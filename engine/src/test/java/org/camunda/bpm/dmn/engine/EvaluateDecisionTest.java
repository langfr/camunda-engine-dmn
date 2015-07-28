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

package org.camunda.bpm.dmn.engine;

import org.camunda.bpm.dmn.engine.test.DecisionResource;
import org.camunda.bpm.dmn.engine.test.DmnDecisionTest;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.camunda.bpm.dmn.engine.test.asserts.DmnAssertions.assertThat;

public class EvaluateDecisionTest extends DmnDecisionTest {

  @Test
  @DecisionResource(resource = NO_INPUT_DMN)
  public void shouldEvaluateRuleWithoutInput() {
    assertThat(engine)
      .evaluates(decision, Collections.<String, Object>emptyMap())
      .hasResult("ok");
  }

  @Test
  @DecisionResource(resource = ONE_RULE_DMN)
  public void shouldEvaluateSingleRule() {
    Map<String, Object> variables = new HashMap<String, Object>();
    variables.put("input", "ok");

    assertThat(engine)
      .evaluates(decision, variables)
      .hasResult("ok");

    assertThat(engine)
      .evaluates(decision, variables)
      .hasResult(null, "ok");

    variables.put("input", "notok");

    assertThat(engine)
      .evaluates(decision, variables)
      .hasEmptyResult();
  }

  @Test
  @DecisionResource(resource = EXAMPLE_DMN)
  public void shouldEvaluateExample() {
    Map<String, Object> variables = new HashMap<String, Object>();
    variables.put("status", "bronze");
    variables.put("sum", 200);

    assertThat(engine)
      .evaluates(decision, variables)
      .hasResult()
      .hasSingleOutput()
      .hasEntry("result", "notok")
      .hasEntry("reason", "work on your status first, as bronze you're not going to get anything");

    variables.put("status", "silver");

    assertThat(engine)
      .evaluates(decision, variables)
      .hasResult()
      .hasSingleOutput()
      .hasEntry("result", "ok")
      .hasEntry("reason", "you little fish will get what you want");

    variables.put("sum", 1200);

    assertThat(engine)
      .evaluates(decision, variables)
      .hasResult()
      .hasSingleOutput()
      .hasEntry("result", "notok")
      .hasEntry("reason", "you took too much man, you took too much!");

    variables.put("status", "gold");
    variables.put("sum", 200);

    assertThat(engine)
      .evaluates(decision, variables)
      .hasResult()
      .hasSingleOutput()
      .hasEntry("result", "ok")
      .hasEntry("reason", "you get anything you want");
  }

  @Test
  @DecisionResource(resource = DATA_TYPE_DMN)
  public void shouldDetectDataTypes() {
    Map<String, Object> variables = new HashMap<String, Object>();
    variables.put("boolean", true);
    variables.put("integer", 9000);
    variables.put("double", 13.37);


    assertThat(engine)
      .evaluates(decision, variables)
      .hasResult(true);

    variables.put("boolean", false);
    variables.put("integer", 10000);
    variables.put("double", 21.42);

    assertThat(engine)
      .evaluates(decision, variables)
      .hasResult(true);

    variables.put("boolean", true);
    variables.put("integer", -9000);
    variables.put("double", -13.37);

    assertThat(engine)
      .evaluates(decision, variables)
      .hasResult(true);
  }

}

