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
package com.alibaba.chaosblade.exec.plugin.jsf;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.action.delay.TimeoutExecutor;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import com.alibaba.chaosblade.exec.plugin.jsf.model.JsfParamsMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author BoYuan Han
 */
public abstract class JsfEnhancer extends BeforeEnhancer {

    public static final String GET_SERVICE_NAME = "getClassName";
    public static final String GET_METHOD_NAME = "getMethodName";
    public static final String HANDLER_REQUEST_METHOD = "handlerRequest";
    public static final String GET_ALIAS = "getAlias";
    private static final Logger LOGGER = LoggerFactory.getLogger(JsfEnhancer.class);
    // Jsf Internal Service
    public static final List<String> JSF_SERVICE_WHITE_LIST = new ArrayList<String>(){{
        add("com.jd.jsf.service.RegistryService");
        add("com.jd.jsf.gd.monitor.JSFMonitorService");
    }};

    @Override
    public EnhancerModel doBeforeAdvice(ClassLoader classLoader, String className, Object object,
                                        Method method, Object[]
                                            methodArguments)
        throws Exception {

        // judge Advice provider thread pool
        if (judgeThreadPoolAdvice(method,object)){
            return null;
        }

        Object invocation = methodArguments[0];
        if (object == null || invocation == null) {
            LOGGER.warn("The necessary parameter is null.");
            return null;
        }
        String methodName = null;
        String serviceName = null;
        String alias = null;
        methodName = ReflectUtil.invokeMethod(invocation, GET_METHOD_NAME, new Object[0], false);
        serviceName = ReflectUtil.invokeMethod(invocation, GET_SERVICE_NAME, new Object[0], false);
        alias = ReflectUtil.invokeMethod(invocation,GET_ALIAS,new Object[0],false);
        if (JSF_SERVICE_WHITE_LIST.contains(serviceName)){
            return null;
        }
        if (methodName == null || serviceName == null || alias == null) {
            LOGGER.warn("methodName is null, can not get necessary values.");
            return null;
        }

        MatcherModel matcherModel = new MatcherModel();
        matcherModel.add(JsfConstant.SERVICE_KEY, serviceName);
        matcherModel.add(JsfConstant.METHOD_KEY, methodName);
        matcherModel.add(JsfConstant.ALIAS_KEY,alias);
        int timeout = getTimeout(methodName, object, invocation);
        matcherModel.add(JsfConstant.TIMEOUT_KEY, timeout + "");

        if (LOGGER.isDebugEnabled()) {
//            LOGGER.debug("jsf matchers: {}", JSON.toJSONString(matcherModel));
        }

        EnhancerModel enhancerModel = new EnhancerModel(classLoader, matcherModel);
        enhancerModel.addCustomMatcher(JsfConstant.SERVICE_KEY,serviceName,JsfParamsMatcher.getInstance());
        enhancerModel.addCustomMatcher(JsfConstant.METHOD_KEY,methodName,JsfParamsMatcher.getInstance());
        enhancerModel.setTimeoutExecutor(createTimeoutExecutor(classLoader, timeout, className));
        postDoBeforeAdvice(enhancerModel);
        return enhancerModel;
    }

    protected abstract void postDoBeforeAdvice(EnhancerModel enhancerModel);

    protected boolean judgeThreadPoolAdvice(Method method,Object object){
        return false;
    }

    /**
     * Get service timeout
     *
     * @param method
     * @param instance
     * @param invocation
     * @return
     */
    protected abstract int getTimeout(String method, Object instance, Object invocation);


    /**
     * Create timeout executor
     *
     * @param classLoader
     * @param timeout
     * @param className
     * @return
     */
    protected abstract TimeoutExecutor createTimeoutExecutor(ClassLoader classLoader, long timeout,
                                                             String className);

}
