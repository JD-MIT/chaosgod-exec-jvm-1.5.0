/*
 * Copyright 1999-2019 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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

package com.alibaba.chaosblade.exec.plugin.jsf.model;

import com.alibaba.chaosblade.exec.common.aop.PredicateResult;
import com.alibaba.chaosblade.exec.common.exception.ExperimentException;
import com.alibaba.chaosblade.exec.common.model.FrameworkModelSpec;
import com.alibaba.chaosblade.exec.common.model.Model;
import com.alibaba.chaosblade.exec.common.model.action.ActionExecutor;
import com.alibaba.chaosblade.exec.common.model.action.ActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.threadpool.ThreadPoolFullActionSpec;
import com.alibaba.chaosblade.exec.common.model.handler.PreCreateInjectionModelHandler;
import com.alibaba.chaosblade.exec.common.model.handler.PreDestroyInjectionModelHandler;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;
import com.alibaba.chaosblade.exec.common.plugin.MethodNameMatcherSpec;
import com.alibaba.chaosblade.exec.plugin.jsf.JsfConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author BoYuan Han
 */
public class JsfModelSpec extends FrameworkModelSpec implements PreCreateInjectionModelHandler,
    PreDestroyInjectionModelHandler {

    public JsfModelSpec() {
        super();
        addThreadPoolFullActionSpec();
    }

    @Override
    public String getShortDesc() {
        return "jsf experiment";
    }

    @Override
    public String getLongDesc() {
        return "Jsf experiment for testing service delay and exception.";
    }

    @Override
    protected List<MatcherSpec> createNewMatcherSpecs() {
        ArrayList<MatcherSpec> matcherSpecs = new ArrayList<MatcherSpec>();
        matcherSpecs.add(new ConsumerMatcherSpec());
        matcherSpecs.add(new ProviderMatcherSpec());
        matcherSpecs.add(new ServiceMatcherSpec());
        matcherSpecs.add(new MethodNameMatcherSpec());
        matcherSpecs.add(new AliasMatcherSpec());
        return matcherSpecs;
    }

    @Override
    protected PredicateResult preMatcherPredicate(Model model) {
        if (model == null) {
            return PredicateResult.fail("matcher not found for jsf");
        }
        MatcherModel matcher = model.getMatcher();
        Set<String> keySet = matcher.getMatchers().keySet();
        for (String key : keySet) {
            if (key.equals(JsfConstant.CONSUMER_KEY) || key.equals(JsfConstant.PROVIDER_KEY)) {
                return PredicateResult.success();
            }
        }
        return PredicateResult.fail("less necessary matcher is consumer or provider for jsf");
    }

    @Override
    public String getTarget() {
        return JsfConstant.TARGET_NAME;
    }

    @Override
    public void preCreate(String suid, Model model) throws ExperimentException {
        if (ThreadPoolFullActionSpec.NAME.equals(model.getActionName())) {
            ActionSpec actionSpec = this.getActionSpec(model.getActionName());
            ActionExecutor actionExecutor = actionSpec.getActionExecutor();
            if (actionExecutor instanceof JsfThreadPoolFullExecutor) {
                JsfThreadPoolFullExecutor threadPoolFullExecutor = (JsfThreadPoolFullExecutor)actionExecutor;
                threadPoolFullExecutor.setExpReceived(true);
            } else {
                throw new ExperimentException("actionExecutor is not instance of JsfThreadPoolFullExecutor");
            }
        }
    }

    @Override
    public void preDestroy(String suid, Model model) throws ExperimentException {
        if (ThreadPoolFullActionSpec.NAME.equals(model.getActionName())) {
            ActionSpec actionSpec = this.getActionSpec(model.getActionName());
            ActionExecutor actionExecutor = actionSpec.getActionExecutor();
            if (actionExecutor instanceof JsfThreadPoolFullExecutor) {
                JsfThreadPoolFullExecutor threadPoolFullExecutor = (JsfThreadPoolFullExecutor)actionExecutor;
                threadPoolFullExecutor.revoke();
            } else {
                throw new ExperimentException("actionExecutor is not instance of JsfThreadPoolFullExecutor");
            }
        }
    }

    private void addThreadPoolFullActionSpec() {
        ThreadPoolFullActionSpec threadPoolFullActionSpec = new ThreadPoolFullActionSpec(
            JsfThreadPoolFullExecutor.INSTANCE);
        addActionSpec(threadPoolFullActionSpec);
        // the thread pool full experiment applies to provider
        addMatcherDefToAllActions(new ProviderMatcherSpec());
    }
}
