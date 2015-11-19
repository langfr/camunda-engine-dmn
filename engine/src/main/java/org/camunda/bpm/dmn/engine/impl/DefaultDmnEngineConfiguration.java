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

package org.camunda.bpm.dmn.engine.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.dmn.engine.DmnEngineConfiguration;
import org.camunda.bpm.dmn.engine.delegate.DmnDecisionTableEvaluationListener;
import org.camunda.bpm.dmn.engine.impl.el.DefaultScriptEngineResolver;
import org.camunda.bpm.dmn.engine.impl.el.JuelElProvider;
import org.camunda.bpm.dmn.engine.impl.metrics.DefaultEngineMetricCollector;
import org.camunda.bpm.dmn.engine.impl.spi.el.DmnScriptEngineResolver;
import org.camunda.bpm.dmn.engine.impl.spi.el.ElProvider;
import org.camunda.bpm.dmn.engine.impl.spi.transform.DmnTransformer;
import org.camunda.bpm.dmn.engine.impl.transform.DefaultDmnTransformer;
import org.camunda.bpm.dmn.engine.spi.DmnEngineMetricCollector;
import org.camunda.bpm.dmn.feel.FeelEngine;
import org.camunda.bpm.dmn.feel.FeelEngineFactory;
import org.camunda.bpm.dmn.feel.impl.FeelEngineFactoryImpl;
import org.camunda.bpm.model.dmn.impl.DmnModelConstants;

public class DefaultDmnEngineConfiguration implements DmnEngineConfiguration {

  public static final String FEEL_EXPRESSION_LANGUAGE = DmnModelConstants.FEEL_NS;
  public static final String FEEL_EXPRESSION_LANGUAGE_ALTERNATIVE = "feel";
  public static final String JUEL_EXPRESSION_LANGUAGE = "juel";

  protected DmnEngineMetricCollector engineMetricCollector;

  protected List<DmnDecisionTableEvaluationListener> customPreDecisionTableEvaluationListeners = new ArrayList<DmnDecisionTableEvaluationListener>();
  protected List<DmnDecisionTableEvaluationListener> customPostDecisionTableEvaluationListeners = new ArrayList<DmnDecisionTableEvaluationListener>();
  protected List<DmnDecisionTableEvaluationListener> decisionTableEvaluationListeners;

  protected DmnScriptEngineResolver scriptEngineResolver;
  protected ElProvider elProvider;
  protected FeelEngineFactory feelEngineFactory;
  protected FeelEngine feelEngine;

  protected String defaultInputExpressionExpressionLanguage = JUEL_EXPRESSION_LANGUAGE;
  protected String defaultInputEntryExpressionLanguage = FEEL_EXPRESSION_LANGUAGE;
  protected String defaultOutputEntryExpressionLanguage = JUEL_EXPRESSION_LANGUAGE;

  protected DmnTransformer transformer = new DefaultDmnTransformer();

  public DmnEngine buildEngine() {
    init();
    return new DefaultDmnEngine(this);
  }

  public void init() {
    initMetricCollector();
    initDecisionTableEvaluationListener();
    initScriptEngineResolver();
    initElProvider();
    initFeelEngine();
  }

  protected void initMetricCollector() {
    if (engineMetricCollector == null) {
      engineMetricCollector = new DefaultEngineMetricCollector();
    }
  }

  protected void initDecisionTableEvaluationListener() {
    List<DmnDecisionTableEvaluationListener> listeners = new ArrayList<DmnDecisionTableEvaluationListener>();
    if (customPreDecisionTableEvaluationListeners != null && !customPreDecisionTableEvaluationListeners.isEmpty()) {
      listeners.addAll(customPreDecisionTableEvaluationListeners);
    }
    listeners.addAll(getDefaultDmnDecisionTableEvaluationListeners());
    if (customPostDecisionTableEvaluationListeners != null && !customPostDecisionTableEvaluationListeners.isEmpty()) {
      listeners.addAll(customPostDecisionTableEvaluationListeners);
    }
    decisionTableEvaluationListeners = listeners;
  }

  protected Collection<? extends DmnDecisionTableEvaluationListener> getDefaultDmnDecisionTableEvaluationListeners() {
    List<DmnDecisionTableEvaluationListener> defaultListeners = new ArrayList<DmnDecisionTableEvaluationListener>();
    defaultListeners.add(engineMetricCollector);
    return defaultListeners;
  }

  protected void initElProvider() {
    if(elProvider == null) {
      elProvider = new JuelElProvider();
    }
  }

  protected void initScriptEngineResolver() {
    if (scriptEngineResolver == null) {
      scriptEngineResolver = new DefaultScriptEngineResolver();
    }
  }

  protected void initFeelEngine() {
    if (feelEngineFactory == null) {
      feelEngineFactory = new FeelEngineFactoryImpl();
    }

    if (feelEngine == null) {
      feelEngine = feelEngineFactory.createInstance();
    }
  }

  public DmnEngineMetricCollector getEngineMetricCollector() {
    return engineMetricCollector;
  }

  public void setEngineMetricCollector(DmnEngineMetricCollector engineMetricCollector) {
    this.engineMetricCollector = engineMetricCollector;
  }

  public DmnEngineConfiguration engineMetricCollector(DmnEngineMetricCollector engineMetricCollector) {
    setEngineMetricCollector(engineMetricCollector);
    return this;
  }

  public List<DmnDecisionTableEvaluationListener> getCustomPreDecisionTableEvaluationListeners() {
    return customPreDecisionTableEvaluationListeners;
  }

  public void setCustomPreDecisionTableEvaluationListeners(List<DmnDecisionTableEvaluationListener> decisionTableEvaluationListeners) {
    this.customPreDecisionTableEvaluationListeners = decisionTableEvaluationListeners;
  }

  public DmnEngineConfiguration customPreDecisionTableEvaluationListeners(List<DmnDecisionTableEvaluationListener> decisionTableEvaluationListeners) {
    setCustomPreDecisionTableEvaluationListeners(decisionTableEvaluationListeners);
    return this;
  }

  public List<DmnDecisionTableEvaluationListener> getCustomPostDecisionTableEvaluationListeners() {
    return customPostDecisionTableEvaluationListeners;
  }

  public void setCustomPostDecisionTableEvaluationListeners(List<DmnDecisionTableEvaluationListener> decisionTableEvaluationListeners) {
    this.customPostDecisionTableEvaluationListeners = decisionTableEvaluationListeners;
  }

  public DmnEngineConfiguration customPostDecisionTableEvaluationListeners(List<DmnDecisionTableEvaluationListener> decisionTableEvaluationListeners) {
    setCustomPostDecisionTableEvaluationListeners(decisionTableEvaluationListeners);
    return this;
  }

  public List<DmnDecisionTableEvaluationListener> getDecisionTableEvaluationListeners() {
    return decisionTableEvaluationListeners;
  }

  public DmnScriptEngineResolver getScriptEngineResolver() {
    return scriptEngineResolver;
  }

  public void setScriptEngineResolver(DmnScriptEngineResolver scriptEngineResolver) {
    this.scriptEngineResolver = scriptEngineResolver;
  }

  public DmnEngineConfiguration scriptEngineResolver(DmnScriptEngineResolver scriptEngineResolver) {
    setScriptEngineResolver(scriptEngineResolver);
    return this;
  }

  public ElProvider getElProvider() {
    return elProvider;
  }

  public void setElProvider(ElProvider elProvider) {
    this.elProvider = elProvider;
  }

  public DmnEngineConfiguration elProvider(ElProvider elProvider) {
    setElProvider(elProvider);
    return this;
  }


  public FeelEngineFactory getFeelEngineFactory() {
    return feelEngineFactory;
  }

  public void setFeelEngineFactory(FeelEngineFactory feelEngineFactory) {
    this.feelEngineFactory = feelEngineFactory;
    this.feelEngine = null; // clear cached FEEL engine
  }

  public DmnEngineConfiguration feelEngineFactory(FeelEngineFactory feelEngineFactory) {
    setFeelEngineFactory(feelEngineFactory);
    return this;
  }

  public FeelEngine getFeelEngine() {
    return feelEngine;
  }

  public String getDefaultInputEntryExpressionLanguage() {
    return defaultInputEntryExpressionLanguage;
  }

  public void setDefaultInputEntryExpressionLanguage(String defaultInputEntryExpressionLanguage) {
    this.defaultInputEntryExpressionLanguage = defaultInputEntryExpressionLanguage;
  }

  public DmnEngineConfiguration defaultInputEntryExpressionLanguage(String expressionLanguage) {
    setDefaultInputEntryExpressionLanguage(expressionLanguage);
    return this;
  }

  public String getDefaultOutputEntryExpressionLanguage() {
    return defaultOutputEntryExpressionLanguage;
  }

  public void setDefaultOutputEntryExpressionLanguage(String defaultOutputEntryExpressionLanguage) {
    this.defaultOutputEntryExpressionLanguage = defaultOutputEntryExpressionLanguage;
  }

  public DmnEngineConfiguration defaultOutputEntryExpressionLanguage(String expressionLanguage) {
    setDefaultOutputEntryExpressionLanguage(expressionLanguage);
    return this;
  }

  public String getDefaultInputExpressionExpressionLanguage() {
    return defaultInputExpressionExpressionLanguage;
  }

  public void setDefaultInputExpressionExpressionLanguage(String defaultInputExpressionExpressionLanguage) {
    this.defaultInputExpressionExpressionLanguage = defaultInputExpressionExpressionLanguage;
  }

  public DmnEngineConfiguration defaultInputExpressionExpressionLanguage(String expressionLanguage) {
    setDefaultInputExpressionExpressionLanguage(expressionLanguage);
    return this;
  }

  public DmnTransformer getTransformer() {
    return transformer;
  }

  public void setTransformer(DmnTransformer transformer) {
    this.transformer = transformer;
  }

  public DmnEngineConfiguration transformer(DmnTransformer transformer) {
    setTransformer(transformer);
    return this;
  }

}