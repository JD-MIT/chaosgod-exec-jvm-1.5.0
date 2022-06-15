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

package com.alibaba.chaosblade.exec.plugin.jsf.provider;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.action.delay.TimeoutExecutor;
import com.alibaba.chaosblade.exec.plugin.jsf.JsfConstant;
import com.alibaba.chaosblade.exec.plugin.jsf.JsfEnhancer;
import com.alibaba.chaosblade.exec.plugin.jsf.model.JsfThreadPoolFullExecutor;

import java.lang.reflect.Method;


/**
 * @author renguangyin@jd.com
 */
public class JsfProviderEnhancer extends JsfEnhancer {

    @Override
    protected TimeoutExecutor createTimeoutExecutor(ClassLoader classLoader, long timeout, String className) {
        return null;
    }

    @Override
    protected void postDoBeforeAdvice(EnhancerModel enhancerModel) {
        enhancerModel.addMatcher(JsfConstant.PROVIDER_KEY, "true");
    }

    @Override
    protected boolean judgeThreadPoolAdvice(Method method,Object object) {
        if (method.getName().equals(HANDLER_REQUEST_METHOD)) {
            // received method for thread pool experiment
            JsfThreadPoolFullExecutor.INSTANCE.setWrappedChannelHandler(object);
            return true;
        }
        return false;
    }

    @Override
    protected int getTimeout(String method, Object instance, Object invocation) {
        return 0;
    }

}
