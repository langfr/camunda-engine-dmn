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

package org.camunda.bpm.dmn.engine.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collections;

import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.dmn.engine.DmnEngineConfiguration;
import org.camunda.bpm.dmn.engine.delegate.DmnDecisionTableEvaluationListener;
import org.camunda.bpm.dmn.engine.impl.DefaultDmnEngine;
import org.camunda.bpm.dmn.engine.impl.DefaultDmnEngineConfiguration;
import org.camunda.bpm.dmn.engine.impl.el.DefaultScriptEngineResolver;
import org.camunda.bpm.dmn.engine.impl.el.JuelElProvider;
import org.camunda.bpm.dmn.engine.impl.metrics.DefaultEngineMetricCollector;
import org.camunda.bpm.dmn.engine.impl.spi.el.ElProvider;
import org.camunda.bpm.dmn.engine.impl.spi.transform.DmnTransformer;
import org.camunda.bpm.dmn.engine.impl.transform.DefaultDmnTransformer;
import org.camunda.bpm.dmn.feel.impl.FeelEngineFactory;
import org.camunda.bpm.dmn.feel.impl.juel.FeelEngineFactoryImpl;
import org.camunda.bpm.dmn.feel.impl.juel.FeelEngineImpl;
import org.junit.Before;
import org.junit.Test;

public class DefaultDmnEngineConfigurationApiTest {

  protected DefaultDmnEngineConfiguration configuration;

  @Before
  public void initConfiguration() {
    configuration = new DefaultDmnEngineConfiguration();
  }

  @Test
  public void shouldSetEngineMetricCollector() {
    configuration.setEngineMetricCollector(null);
    assertThat(configuration.getEngineMetricCollector())
      .isNull();

    DefaultEngineMetricCollector metricCollector = new DefaultEngineMetricCollector();
    configuration.setEngineMetricCollector(metricCollector);

    assertThat(configuration.getEngineMetricCollector())
      .isEqualTo(metricCollector);
    }

  @Test
  public void shouldSetFluentEngineMetricCollector() {
    configuration.engineMetricCollector(null);
    assertThat(configuration.getEngineMetricCollector())
      .isNull();

    DefaultEngineMetricCollector metricCollector = new DefaultEngineMetricCollector();
    configuration.engineMetricCollector(metricCollector);

    assertThat(configuration.getEngineMetricCollector())
      .isEqualTo(metricCollector);
  }

  @Test
  public void shouldSetCustomPreDecisionTableEvaluationListeners() {
    configuration.setCustomPreDecisionTableEvaluationListeners(null);
    assertThat(configuration.getCustomPreDecisionTableEvaluationListeners())
      .isNull();

    configuration.setCustomPreDecisionTableEvaluationListeners(Collections.<DmnDecisionTableEvaluationListener>emptyList());
    assertThat(configuration.getCustomPreDecisionTableEvaluationListeners())
      .isEmpty();

    ArrayList<DmnDecisionTableEvaluationListener> listeners = new ArrayList<DmnDecisionTableEvaluationListener>();
    listeners.add(new DefaultEngineMetricCollector());
    listeners.add(new DefaultEngineMetricCollector());

    configuration.setCustomPreDecisionTableEvaluationListeners(listeners);
    assertThat(configuration.getCustomPreDecisionTableEvaluationListeners())
      .containsExactlyElementsOf(listeners);
  }

  @Test
  public void shouldSetFluentCustomPreDecisionTableEvaluationListeners() {
    configuration.customPreDecisionTableEvaluationListeners(null);
    assertThat(configuration.getCustomPreDecisionTableEvaluationListeners())
      .isNull();

    configuration.customPreDecisionTableEvaluationListeners(Collections.<DmnDecisionTableEvaluationListener>emptyList());
    assertThat(configuration.getCustomPreDecisionTableEvaluationListeners())
      .isEmpty();

    ArrayList<DmnDecisionTableEvaluationListener> listeners = new ArrayList<DmnDecisionTableEvaluationListener>();
    listeners.add(new DefaultEngineMetricCollector());
    listeners.add(new DefaultEngineMetricCollector());

    configuration.customPreDecisionTableEvaluationListeners(listeners);
    assertThat(configuration.getCustomPreDecisionTableEvaluationListeners())
      .containsExactlyElementsOf(listeners);
  }

  @Test
  public void shouldSetCustomPostDecisionTableEvaluationListeners() {
    configuration.setCustomPostDecisionTableEvaluationListeners(null);
    assertThat(configuration.getCustomPostDecisionTableEvaluationListeners())
      .isNull();

    configuration.setCustomPostDecisionTableEvaluationListeners(Collections.<DmnDecisionTableEvaluationListener>emptyList());
    assertThat(configuration.getCustomPostDecisionTableEvaluationListeners())
      .isEmpty();

    ArrayList<DmnDecisionTableEvaluationListener> listeners = new ArrayList<DmnDecisionTableEvaluationListener>();
    listeners.add(new DefaultEngineMetricCollector());
    listeners.add(new DefaultEngineMetricCollector());

    configuration.setCustomPostDecisionTableEvaluationListeners(listeners);
    assertThat(configuration.getCustomPostDecisionTableEvaluationListeners())
      .containsExactlyElementsOf(listeners);
  }

  @Test
  public void shouldSetFluentCustomPostDecisionTableEvaluationListeners() {
    configuration.customPostDecisionTableEvaluationListeners(null);
    assertThat(configuration.getCustomPostDecisionTableEvaluationListeners())
      .isNull();

    configuration.customPostDecisionTableEvaluationListeners(Collections.<DmnDecisionTableEvaluationListener>emptyList());
    assertThat(configuration.getCustomPostDecisionTableEvaluationListeners())
      .isEmpty();

    ArrayList<DmnDecisionTableEvaluationListener> listeners = new ArrayList<DmnDecisionTableEvaluationListener>();
    listeners.add(new DefaultEngineMetricCollector());
    listeners.add(new DefaultEngineMetricCollector());

    configuration.customPostDecisionTableEvaluationListeners(listeners);
    assertThat(configuration.getCustomPostDecisionTableEvaluationListeners())
      .containsExactlyElementsOf(listeners);
  }

  @Test
  public void shouldSetScriptEngineResolver() {
    configuration.setScriptEngineResolver(null);
    assertThat(configuration.getScriptEngineResolver())
      .isNull();

    DefaultScriptEngineResolver scriptEngineResolver = new DefaultScriptEngineResolver();

    configuration.setScriptEngineResolver(scriptEngineResolver);
    assertThat(configuration.getScriptEngineResolver())
      .isEqualTo(scriptEngineResolver);
  }

  @Test
  public void shouldSetFluentScriptEngineResolver() {
    configuration.scriptEngineResolver(null);
    assertThat(configuration.getScriptEngineResolver())
      .isNull();

    DefaultScriptEngineResolver scriptEngineResolver = new DefaultScriptEngineResolver();

    configuration.scriptEngineResolver(scriptEngineResolver);
    assertThat(configuration.getScriptEngineResolver())
      .isEqualTo(scriptEngineResolver);
  }

  @Test
  public void shouldSetElProvider() {
    configuration.setElProvider(null);
    assertThat(configuration.getElProvider())
      .isNull();

    ElProvider elProvider = new JuelElProvider();

    configuration.setElProvider(elProvider);
    assertThat(configuration.getElProvider())
      .isEqualTo(elProvider);
  }

  @Test
  public void shouldSetFluentElProvider() {
    configuration.elProvider(null);
    assertThat(configuration.getElProvider())
      .isNull();

    ElProvider elProvider = new JuelElProvider();

    configuration.elProvider(elProvider);
    assertThat(configuration.getElProvider())
      .isEqualTo(elProvider);
  }

  @Test
  public void shouldSetFeelEngineFactory() {
    configuration.setFeelEngineFactory(null);
    assertThat(configuration.getFeelEngineFactory())
      .isNull();

    FeelEngineFactory feelEngineFactory = new FeelEngineFactoryImpl();

    configuration.setFeelEngineFactory(feelEngineFactory);
    assertThat(configuration.getFeelEngineFactory())
      .isEqualTo(feelEngineFactory);
  }

  @Test
  public void shouldSetFluentFeelEngineFactory() {
    configuration.feelEngineFactory(null);
    assertThat(configuration.getFeelEngineFactory())
      .isNull();

    FeelEngineFactory feelEngineFactory = new FeelEngineFactoryImpl();

    configuration.feelEngineFactory(feelEngineFactory);
    assertThat(configuration.getFeelEngineFactory())
      .isEqualTo(feelEngineFactory);
  }

  @Test
  public void shouldSetDefaultInputExpressionExpressionLanguage() {
    configuration.setDefaultInputExpressionExpressionLanguage(null);
    assertThat(configuration.getDefaultInputExpressionExpressionLanguage())
      .isNull();

    configuration.setDefaultInputExpressionExpressionLanguage("camunda");
    assertThat(configuration.getDefaultInputExpressionExpressionLanguage())
      .isEqualTo("camunda");
  }

  @Test
  public void shouldSetFluentDefaultInputExpressionExpressionLanguage() {
    configuration.defaultInputExpressionExpressionLanguage(null);
    assertThat(configuration.getDefaultInputExpressionExpressionLanguage())
      .isNull();

    configuration.defaultInputExpressionExpressionLanguage("camunda");
    assertThat(configuration.getDefaultInputExpressionExpressionLanguage())
      .isEqualTo("camunda");
  }

  @Test
  public void shouldSetDefaultInputEntryExpressionLanguage() {
    configuration.setDefaultInputEntryExpressionLanguage(null);
    assertThat(configuration.getDefaultInputEntryExpressionLanguage())
      .isNull();

    configuration.setDefaultInputEntryExpressionLanguage("camunda");
    assertThat(configuration.getDefaultInputEntryExpressionLanguage())
      .isEqualTo("camunda");
  }

  @Test
  public void shouldSetFluentDefaultInputEntryExpressionLanguage() {
    configuration.defaultInputEntryExpressionLanguage(null);
    assertThat(configuration.getDefaultInputEntryExpressionLanguage())
      .isNull();

    configuration.defaultInputEntryExpressionLanguage("camunda");
    assertThat(configuration.getDefaultInputEntryExpressionLanguage())
      .isEqualTo("camunda");
  }

  @Test
  public void shouldSetDefaultOutputEntryExpressionLanguage() {
    configuration.setDefaultOutputEntryExpressionLanguage(null);
    assertThat(configuration.getDefaultOutputEntryExpressionLanguage())
      .isNull();

    configuration.setDefaultOutputEntryExpressionLanguage("camunda");
    assertThat(configuration.getDefaultOutputEntryExpressionLanguage())
      .isEqualTo("camunda");
  }

  @Test
  public void shouldSetFluentDefaultOutputEntryExpressionLanguage() {
    configuration.defaultOutputEntryExpressionLanguage(null);
    assertThat(configuration.getDefaultOutputEntryExpressionLanguage())
      .isNull();

    configuration.defaultOutputEntryExpressionLanguage("camunda");
    assertThat(configuration.getDefaultOutputEntryExpressionLanguage())
      .isEqualTo("camunda");
  }

  @Test
  public void shouldSetTransformer() {
    configuration.setTransformer(null);
    assertThat(configuration.getTransformer())
      .isNull();

    DmnTransformer transformer = new DefaultDmnTransformer();

    configuration.setTransformer(transformer);
    assertThat(configuration.getTransformer())
      .isEqualTo(transformer);
  }

  @Test
  public void shouldSetFluentTransformer() {
    configuration.transformer(null);
    assertThat(configuration.getTransformer())
      .isNull();

    DmnTransformer transformer = new DefaultDmnTransformer();

    configuration.transformer(transformer);
    assertThat(configuration.getTransformer())
      .isEqualTo(transformer);
  }

  @Test
  public void shouldBeFluentConfigurable() {
    DefaultEngineMetricCollector metricCollector = new DefaultEngineMetricCollector();
    ArrayList<DmnDecisionTableEvaluationListener> preListeners = new ArrayList<DmnDecisionTableEvaluationListener>();
    preListeners.add(new DefaultEngineMetricCollector());
    ArrayList<DmnDecisionTableEvaluationListener> postListeners = new ArrayList<DmnDecisionTableEvaluationListener>();
    preListeners.add(new DefaultEngineMetricCollector());
    DefaultScriptEngineResolver scriptEngineResolver = new DefaultScriptEngineResolver();
    ElProvider elProvider = new JuelElProvider();
    FeelEngineFactory feelEngineFactory = new FeelEngineFactoryImpl();
    DmnTransformer transformer = new DefaultDmnTransformer();

    DmnEngine engine = configuration
      .engineMetricCollector(metricCollector)
      .customPreDecisionTableEvaluationListeners(preListeners)
      .customPostDecisionTableEvaluationListeners(postListeners)
      .scriptEngineResolver(scriptEngineResolver)
      .elProvider(elProvider)
      .feelEngineFactory(feelEngineFactory)
      .defaultInputExpressionExpressionLanguage("camunda")
      .defaultInputEntryExpressionLanguage("camunda")
      .defaultOutputEntryExpressionLanguage("camunda")
      .transformer(transformer)
      .buildEngine();

    configuration = (DefaultDmnEngineConfiguration) engine.getConfiguration();
    assertThat(configuration.getEngineMetricCollector())
      .isEqualTo(metricCollector);
    assertThat(configuration.getCustomPreDecisionTableEvaluationListeners())
      .containsExactlyElementsOf(preListeners);
    assertThat(configuration.getCustomPostDecisionTableEvaluationListeners())
      .containsExactlyElementsOf(postListeners);
    assertThat(configuration.getScriptEngineResolver())
      .isEqualTo(scriptEngineResolver);
    assertThat(configuration.getElProvider())
      .isEqualTo(elProvider);
    assertThat(configuration.getFeelEngineFactory())
      .isEqualTo(feelEngineFactory);
    assertThat(configuration.getDefaultInputExpressionExpressionLanguage())
      .isEqualTo("camunda");
    assertThat(configuration.getDefaultInputEntryExpressionLanguage())
      .isEqualTo("camunda");
    assertThat(configuration.getDefaultOutputEntryExpressionLanguage())
      .isEqualTo("camunda");
    assertThat(configuration.getTransformer())
      .isEqualTo(transformer);
  }

  @Test
  public void shouldInitDecisionTableEvaluationListeners() {
    ArrayList<DmnDecisionTableEvaluationListener> preListeners = new ArrayList<DmnDecisionTableEvaluationListener>();
    preListeners.add(new DefaultEngineMetricCollector());
    ArrayList<DmnDecisionTableEvaluationListener> postListeners = new ArrayList<DmnDecisionTableEvaluationListener>();
    postListeners.add(new DefaultEngineMetricCollector());

    configuration
      .customPreDecisionTableEvaluationListeners(preListeners)
      .customPostDecisionTableEvaluationListeners(postListeners)
      .buildEngine();

    ArrayList<DmnDecisionTableEvaluationListener> expectedListeners = new ArrayList<DmnDecisionTableEvaluationListener>();
    expectedListeners.addAll(preListeners);
    expectedListeners.add(configuration.getEngineMetricCollector());
    expectedListeners.addAll(postListeners);

    assertThat(configuration.getDecisionTableEvaluationListeners())
      .containsExactlyElementsOf(expectedListeners);
  }

  @Test
  public void shouldInitFeelEngine() {
    FeelEngineFactory feelEngineFactory = new FeelEngineFactoryImpl();
    configuration.setFeelEngineFactory(feelEngineFactory);

    configuration.buildEngine();

    assertThat(configuration.getFeelEngine())
      .isInstanceOf(FeelEngineImpl.class)
      .isNotNull();
  }

  @Test
  public void shouldBuildDefaultDmnEngine() {
    DmnEngine engine = configuration.buildEngine();
    assertThat(engine)
      .isInstanceOf(DefaultDmnEngine.class)
      .isNotNull();
  }

}
