package com.alibaba.chaosblade.exec.plugin.jmq2.producer;

import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.ClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.MethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.NameMethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.OrMethodMatcher;
import com.alibaba.chaosblade.exec.plugin.jmq2.Jmq2Constant;

/**
 * @author renguangyin@jd.com
 */
public class Jmq2ProducerPointCut implements PointCut, Jmq2Constant {

    @Override
    public ClassMatcher getClassMatcher() {
        return new NameClassMatcher("com.jd.jmq.common.network.netty.failover.FailoverNettyClient");
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return new OrMethodMatcher().or(new NameMethodMatcher("sync")).
                or(new NameMethodMatcher("async")).or(new NameMethodMatcher("oneway"));
    }
}
