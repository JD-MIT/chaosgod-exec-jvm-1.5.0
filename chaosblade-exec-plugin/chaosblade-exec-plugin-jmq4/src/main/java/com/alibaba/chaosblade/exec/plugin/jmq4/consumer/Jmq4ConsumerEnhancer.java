package com.alibaba.chaosblade.exec.plugin.jmq4.consumer;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import com.alibaba.chaosblade.exec.plugin.jmq4.Jmq4Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author renguangyin@jd.com
 */
public class Jmq4ConsumerEnhancer extends BeforeEnhancer implements Jmq4Constant {

    private static final Logger LOGGER = LoggerFactory.getLogger(Jmq4ConsumerEnhancer.class);

    @Override
    public EnhancerModel doBeforeAdvice(ClassLoader classLoader, String className, Object object,
                                        Method method, Object[] methodArguments) throws Exception {
        if (object == null || methodArguments == null) {
            return null;
        }
        List<Object> messages = (List<Object>) methodArguments[2];
        if (messages == null || messages.isEmpty()) {
            return null;
        }
        Object message = messages.get(0);
        MatcherModel matcherModel = new MatcherModel();
        Object topicName = ReflectUtil.invokeMethod(message, "getTopic", new Object[]{}, false);
        String topic = ReflectUtil.invokeMethod(topicName, "getCode", new Object[]{}, false);
        String app = ReflectUtil.invokeMethod(message, "getApp", new Object[]{}, false);
        matcherModel.add(TOPIC_KEY, topic);
        matcherModel.add(APP_KEY, app);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("app: {}, topic: {}", app, topic);
        }
        EnhancerModel enhancerModel = new EnhancerModel(classLoader, matcherModel);
        enhancerModel.addMatcher(CONSUMER_KEY, "true");
        return enhancerModel;
    }
}
