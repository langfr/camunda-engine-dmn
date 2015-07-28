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

package org.camunda.bpm.dmn.juel;

import org.junit.BeforeClass;
import org.junit.Test;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import static org.assertj.core.api.Assertions.assertThat;

public class TestJuelScriptEngine {

  protected static ScriptEngineManager scriptEngineManager;

  @BeforeClass
  public static void createScriptEngineManager() {
    scriptEngineManager = new ScriptEngineManager();
  }

  @Test
  public void shouldFindScriptEngineByName() {
    ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("juel");
    assertScriptEngine(scriptEngine);

    scriptEngine = scriptEngineManager.getEngineByName("Juel");
    assertScriptEngine(scriptEngine);

    scriptEngine = scriptEngineManager.getEngineByName("JUEL");
    assertScriptEngine(scriptEngine);
  }

  @Test
  public void shouldFindScriptEngineByExtension() {
    ScriptEngine scriptEngine = scriptEngineManager.getEngineByExtension("juel");
    assertScriptEngine(scriptEngine);
  }

  @Test
  public void shouldEvaluateConstant() throws ScriptException {
    ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("juel");
    String test = (String) scriptEngine.eval("${'test'}");
    assertThat(test).isEqualTo(test);
  }

  @Test
  public void shouldEvaluateExpression() throws ScriptException {
    ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("juel");
    Bindings bindings = scriptEngine.createBindings();

    bindings.put("test", "ok");
    Boolean result = (Boolean) scriptEngine.eval("${test == 'ok'}", bindings);
    assertThat(result).isTrue();

    bindings.put("test", "notok");
    result = (Boolean) scriptEngine.eval("${test == 'ok'}", bindings);
    assertThat(result).isFalse();
  }

  protected void assertScriptEngine(ScriptEngine scriptEngine) {
    assertThat(scriptEngine)
      .isNotNull()
      .isInstanceOf(JuelScriptEngine.class);
  }

}
