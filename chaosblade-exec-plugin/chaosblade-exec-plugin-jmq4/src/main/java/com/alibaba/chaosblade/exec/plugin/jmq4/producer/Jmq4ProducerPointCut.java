package com.alibaba.chaosblade.exec.plugin.jmq4.producer;

import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.ClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.AndMethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.MethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.NameMethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.OrMethodMatcher;
import com.alibaba.chaosblade.exec.plugin.jmq4.Jmq4Constant;

/**
 * @author renguangyin@jd.com
 */
public class Jmq4ProducerPointCut implements PointCut, Jmq4Constant {

    @Override
    public ClassMatcher getClassMatcher() {
        return new NameClassMatcher("org.joyqueue.client.internal.producer.support.DefaultMessageProducer");
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        AndMethodMatcher doSend = new AndMethodMatcher();
        doSend.and(new NameMethodMatcher("doSend"));
        AndMethodMatcher doBatchSend = new AndMethodMatcher();
        doBatchSend.and(new NameMethodMatcher("doBatchSend"));
        OrMethodMatcher orMatcher = new OrMethodMatcher();
        orMatcher.or(doSend).or(doBatchSend);
        return orMatcher;
    }
}
