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

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import org.camunda.bpm.dmn.engine.impl.feel.FeelJuelBuilder;
import org.junit.Before;
import org.junit.Test;

import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.tree.TreeBuilder;
import de.odysseus.el.util.SimpleContext;

public class JuelExtensionTest {

  protected Map<String, Object> variables;

  @Before
  public void initVariables() {
    variables = new HashMap<String, Object>();
  }

  @Test
  public void test() {
    variables.put("x", 12);
    variables.put("y", 13);
    assertThat(evaluate("${not(x == 13, y == x)}")).isTrue();
  }


  public Boolean evaluate(String expression) {
    Properties properties = new Properties();
    properties.setProperty(TreeBuilder.class.getName(), FeelJuelBuilder.class.getName());
    // init expression engine
    ExpressionFactory factory = new ExpressionFactoryImpl(properties);
    SimpleContext context = FeelJuelTransformerEvaluateTest.createElContext();

    // set variables
    for (String variable : variables.keySet()) {
      context.setVariable(variable, factory.createValueExpression(variables.get(variable), Object.class));
    }

    // create expression
    ValueExpression valueExpression = factory.createValueExpression(context, expression, boolean.class);

    // evaluate
    return (Boolean) valueExpression.getValue(context);
  }

}
