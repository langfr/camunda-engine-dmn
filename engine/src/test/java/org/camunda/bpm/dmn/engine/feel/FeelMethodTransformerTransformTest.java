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

import org.camunda.bpm.dmn.engine.impl.feel.FeelMethodTransformer;
import org.junit.Test;

public class FeelMethodTransformerTransformTest {

  @Test
  public void testEndpointString() {
    assertTransform("x", "\"Hello World\"", "${compare(x,\"Hello World\",\"==\")}");
    assertTransform("x", "\"\"", "${compare(x,\"\",\"==\")}");
    assertTransform("x", "\"123\"", "${compare(x,\"123\",\"==\")}");
    assertTransform("x", "\"Why.not?\"", "${compare(x,\"Why.not?\",\"==\")}");
  }

  @Test
  public void testEndpointVariable() {
    assertTransform("x", "y", "${compare(x,y,\"==\")}");
    assertTransform("x", "customer.y", "${compare(x,customer.y,\"==\")}");
  }

  @Test
  public void testEndpointVariableGreater() {
    assertTransform("x", "<y", "${compare(x,y,\"<\")}");
    assertTransform("x", "<customer.y", "${compare(x,customer.y,\"<\")}");
  }

  @Test
  public void testEndpointVariableGreaterEqual() {
    assertTransform("x", "<=y", "${compare(x,y,\"<=\")}");
    assertTransform("x", "<=customer.y", "${compare(x,customer.y,\"<=\")}");
  }

  @Test
  public void testEndpointVariableLess() {
    assertTransform("x", ">y", "${compare(x,y,\">\")}");
    assertTransform("x", ">customer.y", "${compare(x,customer.y,\">\")}");
  }

  @Test
  public void testEndpointVariableLessEqual() {
    assertTransform("x", ">=y", "${compare(x,y,\">=\")}");
    assertTransform("x", ">=customer.y", "${compare(x,customer.y,\">=\")}");
  }

  @Test
  public void testEndpointBoolean() {
    assertTransform("x", "true", "${compare(x,true,\"==\")}");
    assertTransform("x", "false", "${compare(x,false,\"==\")}");
  }

  @Test
  public void testEndpointNumber() {
    assertTransform("x", "13", "${compare(x,13,\"==\")}");
    assertTransform("x", "13.37", "${compare(x,13.37,\"==\")}");
    assertTransform("x", ".37", "${compare(x,.37,\"==\")}");
  }

  @Test
  public void testEndpointNumberGreater() {
    assertTransform("x", "<13", "${compare(x,13,\"<\")}");
    assertTransform("x", "<13.37", "${compare(x,13.37,\"<\")}");
    assertTransform("x", "<.37", "${compare(x,.37,\"<\")}");
  }

  @Test
  public void testEndpointNumberGreaterEqual() {
    assertTransform("x", "<=13", "${compare(x,13,\"<=\")}");
    assertTransform("x", "<=13.37", "${compare(x,13.37,\"<=\")}");
    assertTransform("x", "<=.37", "${compare(x,.37,\"<=\")}");
  }

  @Test
  public void testEndpointNumberLess() {
    assertTransform("x", ">13", "${compare(x,13,\">\")}");
    assertTransform("x", ">13.37", "${compare(x,13.37,\">\")}");
    assertTransform("x", ">.37", "${compare(x,.37,\">\")}");
  }

  @Test
  public void testEndpointNumberLessEqual() {
    assertTransform("x", ">=13", "${compare(x,13,\">=\")}");
    assertTransform("x", ">=13.37", "${compare(x,13.37,\">=\")}");
    assertTransform("x", ">=.37", "${compare(x,.37,\">=\")}");
  }

  @Test
  public void testEndpointDate() {
    assertTransform("x", "date(\"2015-12-12\")", "${compare(x,date(\"2015-12-12\"),\"==\")}");
  }

  @Test
  public void testIntervalNumber() {
    assertTransform("x", "[0..12]", "${interval(x,0,12,true,true)}");
    assertTransform("x", "[0..12)", "${interval(x,0,12,true,false)}");
    assertTransform("x", "[0..12[", "${interval(x,0,12,true,false)}");

    assertTransform("x", "[0.12..13.37]", "${interval(x,0.12,13.37,true,true)}");
    assertTransform("x", "[0.12..13.37)", "${interval(x,0.12,13.37,true,false)}");
    assertTransform("x", "[0.12..13.37[", "${interval(x,0.12,13.37,true,false)}");

    assertTransform("x", "[.12...37]", "${interval(x,.12,.37,true,true)}");
    assertTransform("x", "[.12...37)", "${interval(x,.12,.37,true,false)}");
    assertTransform("x", "[.12...37[", "${interval(x,.12,.37,true,false)}");

    assertTransform("x", "(0..12]", "${interval(x,0,12,false,true)}");
    assertTransform("x", "(0..12)", "${interval(x,0,12,false,false)}");
    assertTransform("x", "(0..12[", "${interval(x,0,12,false,false)}");

    assertTransform("x", "(0.12..13.37]", "${interval(x,0.12,13.37,false,true)}");
    assertTransform("x", "(0.12..13.37)", "${interval(x,0.12,13.37,false,false)}");
    assertTransform("x", "(0.12..13.37[", "${interval(x,0.12,13.37,false,false)}");

    assertTransform("x", "(.12...37]", "${interval(x,.12,.37,false,true)}");
    assertTransform("x", "(.12...37)", "${interval(x,.12,.37,false,false)}");
    assertTransform("x", "(.12...37[", "${interval(x,.12,.37,false,false)}");

    assertTransform("x", "]0..12]", "${interval(x,0,12,false,true)}");
    assertTransform("x", "]0..12)", "${interval(x,0,12,false,false)}");
    assertTransform("x", "]0..12[", "${interval(x,0,12,false,false)}");

    assertTransform("x", "]0.12..13.37]", "${interval(x,0.12,13.37,false,true)}");
    assertTransform("x", "]0.12..13.37)", "${interval(x,0.12,13.37,false,false)}");
    assertTransform("x", "]0.12..13.37[", "${interval(x,0.12,13.37,false,false)}");

    assertTransform("x", "].12...37]", "${interval(x,.12,.37,false,true)}");
    assertTransform("x", "].12...37)", "${interval(x,.12,.37,false,false)}");
    assertTransform("x", "].12...37[", "${interval(x,.12,.37,false,false)}");
  }

  @Test
  public void testIntervalVariable() {
    assertTransform("x", "[a..b]", "${interval(x,a,b,true,true)}");
    assertTransform("x", "[a..b)", "${interval(x,a,b,true,false)}");
    assertTransform("x", "[a..b[", "${interval(x,a,b,true,false)}");

    assertTransform("x", "(a..b]", "${interval(x,a,b,false,true)}");
    assertTransform("x", "(a..b)", "${interval(x,a,b,false,false)}");
    assertTransform("x", "(a..b[", "${interval(x,a,b,false,false)}");

    assertTransform("x", "]a..b]", "${interval(x,a,b,false,true)}");
    assertTransform("x", "]a..b)", "${interval(x,a,b,false,false)}");
    assertTransform("x", "]a..b[", "${interval(x,a,b,false,false)}");
  }

  @Test
  public void testIntervalDate() {
    assertTransform("x", "[date(\"2015-12-12\")..date(\"2016-06-06\")]", "${interval(x,date(\"2015-12-12\"),date(\"2016-06-06\"),true,true)}");
    assertTransform("x", "[date(\"2015-12-12\")..date(\"2016-06-06\"))", "${interval(x,date(\"2015-12-12\"),date(\"2016-06-06\"),true,false)}");
    assertTransform("x", "[date(\"2015-12-12\")..date(\"2016-06-06\")[", "${interval(x,date(\"2015-12-12\"),date(\"2016-06-06\"),true,false)}");

    assertTransform("x", "(date(\"2015-12-12\")..date(\"2016-06-06\")]", "${interval(x,date(\"2015-12-12\"),date(\"2016-06-06\"),false,true)}");
    assertTransform("x", "(date(\"2015-12-12\")..date(\"2016-06-06\"))", "${interval(x,date(\"2015-12-12\"),date(\"2016-06-06\"),false,false)}");
    assertTransform("x", "(date(\"2015-12-12\")..date(\"2016-06-06\")[", "${interval(x,date(\"2015-12-12\"),date(\"2016-06-06\"),false,false)}");

    assertTransform("x", "]date(\"2015-12-12\")..date(\"2016-06-06\")]", "${interval(x,date(\"2015-12-12\"),date(\"2016-06-06\"),false,true)}");
    assertTransform("x", "]date(\"2015-12-12\")..date(\"2016-06-06\"))", "${interval(x,date(\"2015-12-12\"),date(\"2016-06-06\"),false,false)}");
    assertTransform("x", "]date(\"2015-12-12\")..date(\"2016-06-06\")[", "${interval(x,date(\"2015-12-12\"),date(\"2016-06-06\"),false,false)}");
  }

  @Test
  public void testNot() {
    assertTransform("x", "not(\"Hello World\")", "${not(compare(x,\"Hello World\",\"==\"))}");
    assertTransform("x", "not(y)", "${not(compare(x,y,\"==\"))}");
    assertTransform("x", "not(<y)", "${not(compare(x,y,\"<\"))}");
    assertTransform("x", "not(<=y)", "${not(compare(x,y,\"<=\"))}");
    assertTransform("x", "not(>y)", "${not(compare(x,y,\">\"))}");
    assertTransform("x", "not(>=y)", "${not(compare(x,y,\">=\"))}");
    assertTransform("x", "not(13.37)", "${not(compare(x,13.37,\"==\"))}");
    assertTransform("x", "not(<13.37)", "${not(compare(x,13.37,\"<\"))}");
    assertTransform("x", "not(<=13.37)", "${not(compare(x,13.37,\"<=\"))}");
    assertTransform("x", "not(>13.37)", "${not(compare(x,13.37,\">\"))}");
    assertTransform("x", "not(>=13.37)", "${not(compare(x,13.37,\">=\"))}");
    assertTransform("x", "not(.37)", "${not(compare(x,.37,\"==\"))}");
    assertTransform("x", "not(<.37)", "${not(compare(x,.37,\"<\"))}");
    assertTransform("x", "not(<=.37)", "${not(compare(x,.37,\"<=\"))}");
    assertTransform("x", "not(>.37)", "${not(compare(x,.37,\">\"))}");
    assertTransform("x", "not(>=.37)", "${not(compare(x,.37,\">=\"))}");
    assertTransform("x", "not(date(\"2015-12-12\"))", "${not(compare(x,date(\"2015-12-12\"),\"==\"))}");
    assertTransform("x", "not(<date(\"2015-12-12\"))", "${not(compare(x,date(\"2015-12-12\"),\"<\"))}");
    assertTransform("x", "not(<=date(\"2015-12-12\"))", "${not(compare(x,date(\"2015-12-12\"),\"<=\"))}");
    assertTransform("x", "not(>date(\"2015-12-12\"))", "${not(compare(x,date(\"2015-12-12\"),\">\"))}");
    assertTransform("x", "not(>=date(\"2015-12-12\"))", "${not(compare(x,date(\"2015-12-12\"),\">=\"))}");
  }

  @Test
  public void testList() {
    assertTransform("x", "a,\"Hello World\"", "${compare(x,a,\"==\") || compare(x,\"Hello World\",\"==\")}");
    assertTransform("x", "y,12,13.37,.37", "${compare(x,y,\"==\") || compare(x,12,\"==\") || compare(x,13.37,\"==\") || compare(x,.37,\"==\")}");
    assertTransform("x", "<y,<=12,>13.37,>=.37", "${compare(x,y,\"<\") || compare(x,12,\"<=\") || compare(x,13.37,\">\") || compare(x,.37,\">=\")}");
    assertTransform("x", "a,date(\"2015-12-12\"),date(\"2016-06-06\"),date(\"2017-07-07\")", "${compare(x,a,\"==\") || compare(x,date(\"2015-12-12\"),\"==\") || compare(x,date(\"2016-06-06\"),\"==\") || compare(x,date(\"2017-07-07\"),\"==\")}");
    assertTransform("x", "<a,<=date(\"2015-12-12\"),>date(\"2016-06-06\"),>=date(\"2017-07-07\")", "${compare(x,a,\"<\") || compare(x,date(\"2015-12-12\"),\"<=\") || compare(x,date(\"2016-06-06\"),\">\") || compare(x,date(\"2017-07-07\"),\">=\")}");
  }

  @Test
  public void testNested() {
    assertTransform("x", "not(>=a,13.37,].37...42),<.37)", "${not(compare(x,a,\">=\") || compare(x,13.37,\"==\") || interval(x,.37,.42,false,false) || compare(x,.37,\"<\"))}");
  }

  public void assertTransform(String input, String feelExpression, String expectedExpression) {
    String expression = FeelMethodTransformer.transformFeel(input, feelExpression);
    assertThat(expression).isEqualTo(expectedExpression);
  }

}
