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

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.el.ExpressionFactory;
import javax.el.FunctionMapper;
import javax.el.ValueExpression;

import org.camunda.bpm.dmn.engine.impl.feel.FeelFunctionMapper;
import org.camunda.bpm.dmn.engine.impl.feel.FeelJuelTransformer;
import org.camunda.bpm.dmn.engine.impl.feel.FeelMethodTransformer;
import org.junit.Before;
import org.junit.Test;

import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;

public class FeelMethodTransformerEvaluateTest {

  protected Map<String, Object> variables;

  @Before
  public void initVariables() {
    variables = new HashMap<String, Object>();
  }

  @Test
  public void testEndpointString() {
    assertTrue("Hello World", "\"Hello World\"");
    assertFalse("Hello World", "\"Hello Camunda\"");
    assertFalse("Hello World", "\"\"");
    assertTrue("", "\"\"");
    assertTrue("123", "\"123\"");
    assertTrue("Why.not?", "\"Why.not?\"");
  }

  @Test
  public void testEndpointVariable() {
    variables.put("y", "a");
    assertTrue("a", "y");
    assertFalse("b", "y");

    variables.put("customer", Collections.singletonMap("name", "camunda"));
    assertTrue("camunda", "customer.name");
    assertFalse("hello", "customer.name");
  }

  @Test
  public void testEndpointVariableGreater() {
    variables.put("y", 13.37);
    assertTrue(12, "<y");
    assertFalse(13.38, "<y");
  }

  @Test
  public void testEndpointVariableGreaterEqual() {
    variables.put("y", 13.37);
    assertTrue(12, "<=y");
    assertTrue(13.37, "<=y");
    assertFalse(13.38, "<=y");
  }

  @Test
  public void testEndpointVariableLess() {
    variables.put("y", 13.37);
    assertFalse(12, ">y");
    assertTrue(13.38, ">y");
  }

  @Test
  public void testEndpointVariableLessEqual() {
    variables.put("y", 13.37);
    assertFalse(12, ">=y");
    assertTrue(13.37, ">=y");
    assertTrue(13.38, ">=y");
  }

  @Test
  public void testEndpointBoolean() {
    assertTrue(true, "true");
    assertFalse(true, "false");
    assertTrue(false, "false");
    assertFalse(false, "true");
  }

  @Test
  public void testEndpointNumber() {
    assertTrue(13, "13");
    assertTrue(13.37, "13.37");
    assertTrue(0.37, ".37");
    assertFalse(13.37, "23.42");
    assertFalse(0.42, ".37");
  }

  @Test
  public void testEndpointNumberGreater() {
    assertTrue(12, "<13");
    assertTrue(13.35, "<13.37");
    assertTrue(0.337, "<.37");
    assertFalse(13.37, "<13.37");
    assertFalse(0.37, "<.37");
  }

  @Test
  public void testEndpointNumberGreaterEqual() {
    assertTrue(13.37, "<=13.37");
    assertTrue(13.337, "<=13.37");
    assertTrue(0.37, "<=.37");
    assertTrue(0.337, "<=.37");
    assertFalse(13.42, "<=13.37");
    assertFalse(0.42, "<=.37");
  }

  @Test
  public void testEndpointNumberLess() {
    assertTrue(13.37, ">13");
    assertTrue(13.42, ">13.37");
    assertTrue(0.42, ">.37");
    assertFalse(13.37, ">13.37");
    assertFalse(0.37, ">.37");
  }

  @Test
  public void testEndpointNumberLessEqual() {
    assertTrue(13.37, ">=13");
    assertTrue(13.37, ">=13.37");
    assertTrue(0.37, ">=.37");
    assertTrue(0.42, ">=.37");
    assertFalse(13.337, ">=13.37");
    assertFalse(0.23, ">=.37");
  }

  @Test
  public void testEndpointDate() throws ParseException {
    String dateString = "2015-12-12";
    Date date = FeelFunctionMapper.parseDate(dateString);
    assertTrue(date, "date(\"2015-12-12\")");

    variables.put("y", "2015-12-12");
    assertTrue(date, "date(y)");
  }

  @Test
  public void testIntervalNumber() {
    assertTrue(0.23, "[.12...37]");
    assertTrue(0.23, "[.12...37)");
    assertTrue(0.23, "[.12...37[");

    assertTrue(0.23, "(.12...37]");
    assertTrue(0.23, "(.12...37)");
    assertTrue(0.23, "(.12...37[");

    assertTrue(0.23, "].12...37]");
    assertTrue(0.23, "].12...37)");
    assertTrue(0.23, "].12...37[");

    assertFalse(13.37, "[.12...37]");
    assertFalse(13.37, "[.12...37)");
    assertFalse(13.37, "[.12...37[");

    assertFalse(13.37, "(.12...37]");
    assertFalse(13.37, "(.12...37)");
    assertFalse(13.37, "(.12...37[");

    assertFalse(13.37, "].12...37]");
    assertFalse(13.37, "].12...37)");
    assertFalse(13.37, "].12...37[");
  }

  @Test
  public void testIntervalVariable() {
    variables.put("a", 10);
    variables.put("b", 15);

    assertTrue(13.37, "[a..b]");
    assertTrue(13.37, "[a..b)");
    assertTrue(13.37, "[a..b[");

    assertTrue(13.37, "(a..b]");
    assertTrue(13.37, "(a..b)");
    assertTrue(13.37, "(a..b[");

    assertTrue(13.37, "]a..b]");
    assertTrue(13.37, "]a..b)");
    assertTrue(13.37, "]a..b[");

    assertFalse(0.37, "[a..b]");
    assertFalse(0.37, "[a..b)");
    assertFalse(0.37, "[a..b[");

    assertFalse(0.37, "(a..b]");
    assertFalse(0.37, "(a..b)");
    assertFalse(0.37, "(a..b[");

    assertFalse(0.37, "]a..b]");
    assertFalse(0.37, "]a..b)");
    assertFalse(0.37, "]a..b[");
  }

  @Test
  public void testIntervalDate() throws ParseException {
    Date date = FeelFunctionMapper.parseDate("2016-03-03");
    assertTrue(date, "[date(\"2015-12-12\")..date(\"2016-06-06\")]");
    assertTrue(date, "[date(\"2015-12-12\")..date(\"2016-06-06\"))");
    assertTrue(date, "[date(\"2015-12-12\")..date(\"2016-06-06\")[");

    assertTrue(date, "(date(\"2015-12-12\")..date(\"2016-06-06\")]");
    assertTrue(date, "(date(\"2015-12-12\")..date(\"2016-06-06\"))");
    assertTrue(date, "(date(\"2015-12-12\")..date(\"2016-06-06\")[");

    assertTrue(date, "]date(\"2015-12-12\")..date(\"2016-06-06\")]");
    assertTrue(date, "]date(\"2015-12-12\")..date(\"2016-06-06\"))");
    assertTrue(date, "]date(\"2015-12-12\")..date(\"2016-06-06\")[");

    date = FeelFunctionMapper.parseDate("2013-03-03");
    assertFalse(date, "[date(\"2015-12-12\")..date(\"2016-06-06\")]");
    assertFalse(date, "[date(\"2015-12-12\")..date(\"2016-06-06\"))");
    assertFalse(date, "[date(\"2015-12-12\")..date(\"2016-06-06\")[");

    assertFalse(date, "(date(\"2015-12-12\")..date(\"2016-06-06\")]");
    assertFalse(date, "(date(\"2015-12-12\")..date(\"2016-06-06\"))");
    assertFalse(date, "(date(\"2015-12-12\")..date(\"2016-06-06\")[");

    assertFalse(date, "]date(\"2015-12-12\")..date(\"2016-06-06\")]");
    assertFalse(date, "]date(\"2015-12-12\")..date(\"2016-06-06\"))");
    assertFalse(date, "]date(\"2015-12-12\")..date(\"2016-06-06\")[");
  }

  @Test
  public void testNot() {
    variables.put("y", 13.37);

    assertTrue("Hello camunda", "not(\"Hello World\")");
    assertTrue(0.37, "not(y)");
    assertFalse(0.37, "not(<y)");
    assertFalse(0.37, "not(<=y)");
    assertTrue(0.37, "not(>y)");
    assertTrue(0.37, "not(>=y)");
    assertTrue(0.37, "not(13.37)");
    assertFalse(0.37, "not(<13.37)");
    assertFalse(0.37, "not(<=13.37)");
    assertTrue(0.37, "not(>13.37)");
    assertTrue(0.37, "not(>=13.37)");
    assertFalse(0.37, "not(.37)");
    assertTrue(0.37, "not(<.37)");
    assertFalse(0.37, "not(<=.37)");
    assertTrue(0.37, "not(>.37)");
    assertFalse(0.37, "not(>=.37)");
  }

  @Test
  public void testList() {
    variables.put("a", "Hello camunda");
    variables.put("y", 0);

    assertTrue("Hello World", "a,\"Hello World\"");
    assertTrue("Hello camunda", "a,\"Hello World\"");
    assertFalse("Hello unknown", "a,\"Hello World\"");
    assertTrue(0, "y,12,13.37,.37");
    assertTrue(12, "y,12,13.37,.37");
    assertTrue(13.37, "y,12,13.37,.37");
    assertTrue(0.37, "y,12,13.37,.37");
    assertFalse(0.23, "y,12,13.37,.37");
    assertTrue(-1, "<y,>13.37,>=.37");
    assertTrue(0.37, "<y,>13.37,>=.37");
    assertFalse(0, "<y,>13.37,>=.37");
  }

  @Test
  public void testNested() {
    variables.put("a", 23.42);
    assertTrue(0.37, "not(>=a,13.37,].37...42),<.37)");
    assertFalse(23.42, "not(>=a,13.37,].37...42),<.37)");
    assertFalse(13.37, "not(>=a,13.37,].37...42),<.37)");
    assertFalse(0.38, "not(>=a,13.37,].37...42),<.37)");
    assertFalse(0, "not(>=a,13.37,].37...42),<.37)");
  }

  @Test
  public void testDontCare() {
    assertTrue(13.37, "-");
  }

  public void assertTrue(Object input, String feelExpression) {
    variables.put("input", input);
    Boolean result = evaluate(variables, feelExpression);
    assertThat(result).isTrue();
  }

  public void assertFalse(Object input, String feelExpression) {
    variables.put("input", input);
    Boolean result = evaluate(variables, feelExpression);
    assertThat(result).isFalse();
  }

  public Boolean evaluate(Map<String, Object> variables, String feelExpression) {
    // init expression engine
    ExpressionFactory factory = new ExpressionFactoryImpl();
    SimpleContext context = createElContext();

    // set variables
    for (String variable : variables.keySet()) {
      context.setVariable(variable, factory.createValueExpression(variables.get(variable), Object.class));
    }

    // create expression
    String expression = FeelMethodTransformer.transformFeel("input", feelExpression);
    ValueExpression valueExpression = factory.createValueExpression(context, expression, boolean.class);

    // evaluate
    return (Boolean) valueExpression.getValue(context);
  }

  public static SimpleContext createElContext() {
    return new TestElContext(new FeelFunctionMapper());
  }

  public static class TestElContext extends SimpleContext {

    public FunctionMapper functionMapper;

    public TestElContext(FunctionMapper functionMapper) {
      this.functionMapper = functionMapper;
    }

    @Override
    public FunctionMapper getFunctionMapper() {
      return functionMapper;
    }

  }

}
