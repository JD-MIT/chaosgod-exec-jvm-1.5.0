package com.alibaba.chaosblade.exec.plugin.jmq4.producer;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.action.delay.BaseTimeoutExecutor;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import com.alibaba.chaosblade.exec.plugin.jmq4.Jmq4Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author renguangyin@jd.com
 */
public class Jmq4ProducerEnhancer extends BeforeEnhancer implements Jmq4Constant {

    private static final Logger LOGGER = LoggerFactory.getLogger(Jmq4ProducerEnhancer.class);

    @Override
    public EnhancerModel doBeforeAdvice(ClassLoader classLoader, String className, Object object,
                                        Method method, Object[] methodArguments) throws Exception {
        if (object == null || methodArguments == null) {
            return null;
        }
        Object messageContainer = methodArguments[0];
        if (messageContainer == null) {
            return null;
        }
        Object message = null;
        if (List.class.isInstance(messageContainer)) {
            List messageList = (List) messageContainer;
            if (messageList.isEmpty()) {
                return null;
            } else {
                message = messageList.get(0);
            }
        } else {
            message = messageContainer;
        }
        if (message == null) {
            return null;
        }
        MatcherModel matcherModel = new MatcherModel();
        String topic = ReflectUtil.invokeMethod(message, "getTopic", new Object[]{}, false);
        String app = null;
        Object producerConfig = ReflectUtil.getFieldValue(object, "config", false);
        if (producerConfig != null) {
            app = ReflectUtil.invokeMethod(producerConfig, "getApp", new Object[]{}, false);
        }
        matcherModel.add(TOPIC_KEY, topic);
        matcherModel.add(APP_KEY, app);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("app: {}, topic: {}", app, topic);
        }
        EnhancerModel enhancerModel = new EnhancerModel(classLoader, matcherModel);
        enhancerModel.addMatcher(PRODUCER_KEY, "true");
        final long timeout = getTimeout((Long) methodArguments[1], (TimeUnit) methodArguments[2]);
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

    private long getTimeout(long timeout, TimeUnit timeoutUnit) throws Exception {
        return timeoutUnit.toMillis(timeout);
    }

}
