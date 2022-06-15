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
package com.alibaba.chaosblade.exec.plugin.jsf.consumer;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.action.delay.BaseTimeoutExecutor;
import com.alibaba.chaosblade.exec.common.model.action.delay.TimeoutExecutor;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import com.alibaba.chaosblade.exec.plugin.jsf.JsfConstant;
import com.alibaba.chaosblade.exec.plugin.jsf.JsfEnhancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;


/**
 * @author renguangyin@jd.com
 */
public class JsfConsumerEnhancer extends JsfEnhancer {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsfConsumerEnhancer.class);
    // There is no configuration timeout in the jsf configuration file; the default value is 5000 milliseconds
    public static final int DEFAULT_TIMEOUT = 5000;
    private static final String JSF_TIMEOUT_EXCEPTION = "com.jd.jsf.gd.error.ClientTimeoutException";
    private static final String CONSUMER_CONFIG = "consumerConfig";
    private static final String TIMEOUT_METHOD_NAME = "getTimeout";

    @Override
    protected TimeoutExecutor createTimeoutExecutor(ClassLoader classLoader, long timeout, String className) {
        return new BaseTimeoutExecutor(classLoader, timeout) {
            @Override
            public Exception generateTimeoutException(ClassLoader classLoader) {
                Class timeOutExceptionClass;
                String exceptionClassName = JSF_TIMEOUT_EXCEPTION;
                try {
                    timeOutExceptionClass = classLoader.loadClass(exceptionClassName);
                    Class[] paramTypes = {String.class};
                    Object[] params = {"origin-chaos-found-jsf-TimeoutException,timeout=" + timeoutInMillis};
                    Constructor con = timeOutExceptionClass.getConstructor(paramTypes);
                    return (Exception) con.newInstance(params);
                } catch (ClassNotFoundException e) {
                    LOGGER.error("origin-chaos-jsf {}", "Can not find " + exceptionClassName, e);
                } catch (Exception e) {
                    LOGGER.error("origin-chaos-jsf {}", "Can not generate " + exceptionClassName, e);
                }
                return new RuntimeException(JsfConstant.TIMEOUT_EXCEPTION_MSG);
            }
        };
    }

    @Override
    protected void postDoBeforeAdvice(EnhancerModel enhancerModel) {
        enhancerModel.addMatcher(JsfConstant.CONSUMER_KEY, "true");
    }

    @Override
    protected int getTimeout(String method, Object instance, Object invocation) {
        try {
            Object consumerConfig = ReflectUtil.getFieldValue(instance, CONSUMER_CONFIG, false);
            if (consumerConfig != null) {
                return ReflectUtil.invokeMethod(consumerConfig, TIMEOUT_METHOD_NAME, new Object[0], false);
            }
        } catch (Exception e) {
            LOGGER.warn("Getting timeout from url occurs exception. return default value " + DEFAULT_TIMEOUT, e);
        }
        return DEFAULT_TIMEOUT;
    }

}
