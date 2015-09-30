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

package org.camunda.bpm.dmn.feel.impl.el;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.el.FunctionMapper;

import org.camunda.bpm.dmn.feel.impl.FeelEngineLogger;
import org.camunda.bpm.dmn.feel.impl.FeelLogger;
import org.camunda.bpm.dmn.feel.impl.type.ComparableLocalDateValue;
import org.camunda.bpm.dmn.feel.impl.type.ComparableLocalTimeValue;
import org.camunda.bpm.engine.variable.value.LocalDateValue;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public class FeelFunctionMapper extends FunctionMapper {

  public static final FeelEngineLogger LOG = FeelLogger.ENGINE_LOGGER;

  protected static final Map<String, Method> methods = new HashMap<String, Method>();

  static {
    methods.put("date", getMethod("parseLocalDate", String.class));
    methods.put("time", getMethod("parseLocalTime", String.class));
    methods.put("dateTime", getMethod("parseDateTime", String.class));
  }

  public Method resolveFunction(String prefix, String localName) {
    return methods.get(localName);
  }

  protected static Method getMethod(String name, Class<?>... parameterTypes) {
    try {
      return FeelFunctionMapper.class.getMethod(name, parameterTypes);
    } catch (NoSuchMethodException e) {
      throw LOG.unableToFindMethod(e, name, parameterTypes);
    }
  }

  public static ComparableLocalDateValue parseLocalDate(String dateString) {
    return new ComparableLocalDateValue(dateString);
  }

  public static ComparableLocalTimeValue parseLocalTime(String timeString) {
    return new ComparableLocalTimeValue(timeString);
  }

  public static Date parseDateTime(String dateTimeString) {
    return new DateTime(dateTimeString).toDate();
  }

}
