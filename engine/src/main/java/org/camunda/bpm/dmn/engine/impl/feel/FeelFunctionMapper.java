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
import javax.el.FunctionMapper;

public class FeelFunctionMapper extends FunctionMapper {

  protected FunctionMapper delegatingFunctionMapper;
  protected Method dateMethod;

  public FeelFunctionMapper(FunctionMapper delegatingFunctionMapper) {
    this.delegatingFunctionMapper = delegatingFunctionMapper;
    this.dateMethod = getDateMethod();
  }

  public Method resolveFunction(String prefix, String localName) {
    if (localName.equals("date")) {
      return dateMethod;
    }
    else {
      return delegatingFunctionMapper.resolveFunction(prefix, localName);
    }
  }

  public Method getDateMethod() {
    try {
      return getClass().getMethod("parseDate", String.class);
    }
    catch (Exception e) {
      throw new RuntimeException("Unable to find method 'parseDate'", e);
    }
  }

  public static Date parseDate(String dateString) throws ParseException {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      return dateFormat.parse(dateString);
  }

}
