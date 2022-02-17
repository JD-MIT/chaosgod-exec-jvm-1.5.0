package com.alibaba.chaosblade.exec.plugin.jmq4.consumer;

import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.ClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.MethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.NameMethodMatcher;
import com.alibaba.chaosblade.exec.plugin.jmq4.Jmq4Constant;

/**
 * @author wanyong10
 * @date 2020/10/30 - 2:59 下午
 */
public class Jmq4ConsumerPointCut implements PointCut, Jmq4Constant {

    @Override
    public ClassMatcher getClassMatcher() {
        return new NameClassMatcher("org.joyqueue.client.internal.consumer.support.TopicMessageConsumerDispatcher");
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return new NameMethodMatcher("doDispatch");
    }
}
