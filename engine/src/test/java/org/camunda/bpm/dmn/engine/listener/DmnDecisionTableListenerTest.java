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

package org.camunda.bpm.dmn.engine.listener;

import static org.camunda.bpm.dmn.engine.test.asserts.DmnAssertions.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.dmn.engine.DmnDecision;
import org.camunda.bpm.dmn.engine.DmnDecisionResult;
import org.camunda.bpm.dmn.engine.DmnDecisionTable;
import org.camunda.bpm.dmn.engine.DmnDecisionTableListener;
import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.dmn.engine.DmnDecisionTableValue;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.dmn.engine.impl.DmnEngineConfigurationImpl;
import org.camunda.bpm.dmn.engine.test.DecisionResource;
import org.camunda.bpm.dmn.engine.test.DmnEngineRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class DmnDecisionTableListenerTest {

  public static final String DMN_FILE = "org/camunda/bpm/dmn/engine/listener/DmnDecisionTableListenerTest.test.dmn";
  @Rule
  public DmnEngineRule dmnEngineRule = new DmnEngineRule(new DmnDecisionTableListenerEngineConfiguration());

  public DmnEngine engine;
  public DmnDecision decision;
  public TestDmnDecisionTableListener listener;


  @Before
  public void initEngineAndDecision() {
    engine = dmnEngineRule.getEngine();
    decision = dmnEngineRule.getDecision();
    listener = ((DmnDecisionTableListenerEngineConfiguration) engine.getConfiguration()).testDecisionTableListener;
  }

  @Test
  @DecisionResource(resource = DMN_FILE)
  public void testListenerIsCalled() {
    Map<String, Object> variables = createVariables(true, "foo", "", "");
    engine.evaluate(decision, variables);
    assertThat(listener.result).isNotNull();
  }

  @Test
  @DecisionResource(resource = DMN_FILE)
  public void testEvaluationMetric() {
    // metric should be independent from input and result
    evaluateDecision(true, "foo", "", "");
    assertThat(listener.result.getEvaluationMetric()).isEqualTo(15);

    evaluateDecision(false, "bar", "", "");
    assertThat(listener.result.getEvaluationMetric()).isEqualTo(15);

    evaluateDecision(false, "", "", "");
    assertThat(listener.result.getEvaluationMetric()).isEqualTo(15);
  }

  @Test
  @DecisionResource(resource = DMN_FILE)
  public void testInputValues() {
    evaluateDecision("true", "foo", "test", "");
    Map<String, DmnDecisionTableValue> inputs = listener.result.getInputs();
    assertThat(inputs).hasSize(3);
  }

  // helper

  public DmnDecisionResult evaluateDecision(Object input1, Object input2, Object input3, Object output1) {
    Map<String, Object> variables = createVariables(input1, input2, input3, output1);
    return engine.evaluate(decision, variables);
  }

  public Map<String, Object> createVariables(Object input1, Object input2, Object input3, Object output1) {
    Map<String, Object> variables = new HashMap<String, Object>();
    variables.put("input1", input1);
    variables.put("input2", input2);
    variables.put("input3", input3);
    variables.put("output1", output1);
    return variables;
  }

  public static class DmnDecisionTableListenerEngineConfiguration extends DmnEngineConfigurationImpl {

    public TestDmnDecisionTableListener testDecisionTableListener = new TestDmnDecisionTableListener();

    public DmnDecisionTableListenerEngineConfiguration() {
      customPostDmnDecisionTableListeners.add(testDecisionTableListener);
    }

  }

  public static class TestDmnDecisionTableListener implements DmnDecisionTableListener {

    public DmnDecisionTableResult result;

    public void notify(DmnDecisionTable decisionTable, DmnDecisionTableResult decisionTableResult) {
      this.result = decisionTableResult;
    }

  }

}
