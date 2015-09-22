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

import javax.script.ScriptEngine;

import org.camunda.bpm.dmn.engine.impl.DefaultScriptEngineResolver;
import org.camunda.bpm.dmn.juel.JuelScriptEngine;
import org.camunda.bpm.dmn.juel.JuelScriptEngineFactory;

public class FeelScriptEngineResolver extends DefaultScriptEngineResolver {

  public ScriptEngine getScriptEngineForLanguage(String language) {
    ScriptEngine scriptEngine = super.getScriptEngineForLanguage(language);

    if (JuelScriptEngineFactory.names.contains(language)) {
      ((JuelScriptEngine) scriptEngine).setElContextFactory(new FeelJuelElContextFactory());
    }

    return scriptEngine;
  }

}
