package com.alibaba.chaosblade.exec.plugin.jmq4.consumer;

import com.alibaba.chaosblade.exec.common.aop.Enhancer;
import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.plugin.jmq4.Jmq4Plugin;

/**
 * @author renguangyin@jd.com
 */
public class Jmq4ConsumerPlugin extends Jmq4Plugin {

    @Override
    public String getName() {
        return CONSUMER_KEY;
    }

    @Override
    public PointCut getPointCut() {
        return new Jmq4ConsumerPointCut();
    }

    @Override
    public Enhancer getEnhancer() {
        return new Jmq4ConsumerEnhancer();
    }
}
