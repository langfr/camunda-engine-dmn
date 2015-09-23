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

import org.camunda.bpm.dmn.engine.impl.DmnEngineConfigurationImpl;
import org.camunda.bpm.dmn.engine.impl.feel.FeelJuelInputEntryHandler;
import org.camunda.bpm.dmn.engine.impl.feel.FeelMethodInputEntryHandler;
import org.camunda.bpm.dmn.engine.impl.feel.FeelOutputEntryHandler;
import org.camunda.bpm.dmn.engine.impl.feel.FeelScriptEngineResolver;
import org.camunda.bpm.dmn.engine.test.DmnEngineRule;
import org.camunda.bpm.model.dmn.instance.InputEntry;
import org.camunda.bpm.model.dmn.instance.OutputEntry;
import org.junit.Rule;

public class FeelMethodTransformerTest extends FeelTheJuelTest {

  @Rule
  public DmnEngineRule dmnEngineRule = new DmnEngineRule(new FeelJuelEngineConfiguration());

  public DmnEngineRule getDmnEngineRule() {
    return dmnEngineRule;
  }

  class FeelJuelEngineConfiguration extends DmnEngineConfigurationImpl {

    public FeelJuelEngineConfiguration() {
      this.scriptEngineResolver = new FeelScriptEngineResolver();
    }

    protected void initElementHandlerRegistry() {
      super.initElementHandlerRegistry();
      elementHandlerRegistry.getElementHandlers().put(InputEntry.class, new FeelMethodInputEntryHandler());
      elementHandlerRegistry.getElementHandlers().put(OutputEntry.class, new FeelOutputEntryHandler());
    }

  }

}
