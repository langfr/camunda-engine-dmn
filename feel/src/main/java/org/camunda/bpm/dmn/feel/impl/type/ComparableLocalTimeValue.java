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

package org.camunda.bpm.dmn.feel.impl.type;

import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.type.PrimitiveValueType;
import org.camunda.bpm.engine.variable.value.LocalTimeValue;
import org.joda.time.LocalTime;
import org.joda.time.Period;

public class ComparableLocalTimeValue implements LocalTimeValue, Comparable<ComparableLocalTimeValue> {

  protected LocalTimeValue wrappedValue;

  public ComparableLocalTimeValue(String localTimeString) {
    this.wrappedValue = Variables.localTimeValue(localTimeString);
  }

  public ComparableLocalTimeValue(LocalTimeValue wrappedValue) {
    this.wrappedValue = wrappedValue;
  }

  public String getValue() {
    return wrappedValue.getValue();
  }

  public PrimitiveValueType getType() {
    return wrappedValue.getType();
  }

  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ComparableLocalTimeValue other = (ComparableLocalTimeValue) o;
    return compareTo(other) == 0;
  }

  public int compareTo(ComparableLocalTimeValue other) {
    String thisValue = this.wrappedValue.getValue();
    String otherValue = other.wrappedValue.getValue();

    if (thisValue == null && otherValue == null) {
      return 0;
    }
    else if (thisValue == null) {
      return -1;
    }
    else if (otherValue == null) {
      return 1;
    }

    LocalTime thisLocalTime = new LocalTime(thisValue);
    LocalTime otherLocalTime = new LocalTime(otherValue);

    return thisLocalTime.compareTo(otherLocalTime);
  }

  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((wrappedValue == null) ? 0 : wrappedValue.hashCode());
    return result;
  }

}
