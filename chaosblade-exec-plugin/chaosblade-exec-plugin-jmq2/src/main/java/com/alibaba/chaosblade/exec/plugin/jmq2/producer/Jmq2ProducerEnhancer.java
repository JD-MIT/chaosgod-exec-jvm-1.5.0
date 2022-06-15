package com.alibaba.chaosblade.exec.plugin.jmq2.producer;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.action.delay.BaseTimeoutExecutor;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import com.alibaba.chaosblade.exec.plugin.jmq2.Jmq2Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @author renguangyin@jd.com
 */
public class Jmq2ProducerEnhancer extends BeforeEnhancer implements Jmq2Constant {

    private static final Logger LOGGER = LoggerFactory.getLogger(Jmq2ProducerEnhancer.class);

    @Override
    public EnhancerModel doBeforeAdvice(ClassLoader classLoader, String className, Object object,
                                        Method method, Object[] methodArguments) throws Exception {
        if (object == null || methodArguments == null) {
            return null;
        }
        if (!"com.jd.jmq.client.connection.ProducerClient".equals(object.getClass().getName())) {
            return null;
        }
        Object command = methodArguments[0];
        if (command == null || !"com.jd.jmq.common.network.command.PutMessage".equals(command.getClass().getName())) {
            return null;
        }
        MatcherModel matcherModel = new MatcherModel();
        String topic = ReflectUtil.invokeMethod(object, "getTopic", new Object[]{}, false);
        String app = null;
        String user = null;
        Object transportConfig = ReflectUtil.getFieldValue(object, "config", false);
        if (transportConfig != null) {
            user = ReflectUtil.invokeMethod(transportConfig, "getUser", new Object[]{}, false);
            app = ReflectUtil.invokeMethod(transportConfig, "getApp", new Object[]{}, false);
        }
        matcherModel.add(TOPIC_KEY, topic);
        matcherModel.add(APP_KEY, app);
        matcherModel.add(USER_KEY, user);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("app: {}, topic: {}, user: {}", app, topic, user);
        }
        EnhancerModel enhancerModel = new EnhancerModel(classLoader, matcherModel);
        enhancerModel.addMatcher(PRODUCER_KEY, "true");
        final int timeout = getTimeout(method.getName(), methodArguments, transportConfig);
        if (timeout > 0) {
            enhancerModel.setTimeoutExecutor(new BaseTimeoutExecutor(classLoader, timeout) {
                @Override
                public Exception generateTimeoutException(ClassLoader classLoader) {
                    return new RuntimeException(TIMEOUT_EXCEPTION_MSG);
                }
            });
        }
        return enhancerModel;
    }

    private int getTimeout(String methodName, Object[] methodArguments, Object transportConfig) throws Exception {
        int timeout = 0;
        if ("sync".equals(methodName) || "oneway".equals(methodName)) {
            if (methodArguments.length == 2) {
                timeout = (Integer) methodArguments[1];
            }
        } else if ("async".equals(methodName)) {
            if (methodArguments.length == 3) {
                timeout = (Integer) methodArguments[1];
            }
        }
        if (timeout <= 0 && transportConfig != null) {
            timeout = ReflectUtil.invokeMethod(transportConfig, "getSendTimeout", new Object[]{}, false);
        }
        return timeout;
    }


}
