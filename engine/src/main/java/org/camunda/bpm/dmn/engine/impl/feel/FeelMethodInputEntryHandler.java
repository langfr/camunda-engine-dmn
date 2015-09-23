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

import org.camunda.bpm.dmn.engine.DmnClause;
import org.camunda.bpm.dmn.engine.handler.DmnElementHandlerContext;
import org.camunda.bpm.dmn.engine.impl.DmnExpressionImpl;
import org.camunda.bpm.dmn.engine.impl.handler.AbstractDmnClauseHandler;
import org.camunda.bpm.model.dmn.instance.InputEntry;

public class FeelMethodInputEntryHandler extends AbstractDmnClauseHandler<InputEntry> {

  protected void postProcessExpressionText(DmnElementHandlerContext context, InputEntry expression, DmnExpressionImpl dmnExpression) {
    if (hasJuelExpressionLanguage(dmnExpression) && !isExpression(dmnExpression)) {
      String feelExpression = dmnExpression.getExpression();
      String variableName = getVariableName(context);
      String juelExpression = FeelMethodTransformer.transformFeel(variableName, feelExpression);
      dmnExpression.setExpression(juelExpression);
    }
  }

  protected String getVariableName(DmnElementHandlerContext context) {
    DmnClause clause = (DmnClause) context.getParent();
    return clause.getOutputName();
  }

}
