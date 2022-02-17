package com.alibaba.chaosblade.exec.plugin.jmq2.consumer;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import com.alibaba.chaosblade.exec.plugin.jmq2.Jmq2Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author wanyong10
 * @date 2020/10/30 - 3:45 下午
 */
public class Jmq2ConsumerEnhancer extends BeforeEnhancer implements Jmq2Constant {

    private static final Logger LOGGER = LoggerFactory.getLogger(Jmq2ConsumerEnhancer.class);

    @Override
    public EnhancerModel doBeforeAdvice(ClassLoader classLoader, String className, Object object,
                                        Method method, Object[] methodArguments) throws Exception {
        if (object == null || methodArguments == null) {
            return null;
        }
        //jmq2.3.0版本新增了一个异构的dispatchMessages方法,这种情况下只增强新增的dispatchMessages方法
        if (methodArguments.length == 3) {
            Method[] methods = object.getClass().getMethods();
            for (Method method1 : methods) {
                if (method.getName().equals(method1.getName()) && method1.getParameterTypes().length == 4) {
                    return null;
                }
            }
        }
        List<Object> dispatches = (List<Object>) methodArguments[0];
        Object listener = methodArguments[1];
        if (dispatches == null || dispatches.isEmpty() || listener == null) {
            return null;
        }
        MatcherModel matcherModel = new MatcherModel();
        String topic = ReflectUtil.getFieldValue(object, "topic", false);
        String app = ReflectUtil.getFieldValue(object, "app", false);
        Object transportManager = ReflectUtil.getFieldValue(object, "transportManager", false);
        String user = null;
        if (transportManager != null) {
            Object transportConfig = ReflectUtil.invokeMethod(transportManager, "getConfig",
                    new Object[]{}, false);
            if (transportConfig != null) {
                user = ReflectUtil.invokeMethod(transportConfig, "getUser", new Object[]{}, false);
                if (app == null) {
                    app = ReflectUtil.invokeMethod(transportConfig, "getApp", new Object[]{}, false);
                }
            }
        }
        matcherModel.add(TOPIC_KEY, topic);
        matcherModel.add(APP_KEY, app);
        matcherModel.add(USER_KEY, user);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("app: {}, topic: {}, user: {}", app, topic, user);
        }
        EnhancerModel enhancerModel = new EnhancerModel(classLoader, matcherModel);
        enhancerModel.addMatcher(CONSUMER_KEY, "true");
        return enhancerModel;
    }
}
