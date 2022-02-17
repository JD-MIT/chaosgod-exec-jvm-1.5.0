package com.alibaba.chaosblade.exec.plugin.jmq2.consumer;

import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.ClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.MethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.NameMethodMatcher;
import com.alibaba.chaosblade.exec.plugin.jmq2.Jmq2Constant;

/**
 * @author wanyong10
 * @date 2020/10/30 - 2:59 下午
 */
public class Jmq2ConsumerPointCut implements PointCut, Jmq2Constant {

    @Override
    public ClassMatcher getClassMatcher() {
        return new NameClassMatcher("com.jd.jmq.client.consumer.TopicConsumer");
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return new NameMethodMatcher("dispatchMessages");
    }
}
