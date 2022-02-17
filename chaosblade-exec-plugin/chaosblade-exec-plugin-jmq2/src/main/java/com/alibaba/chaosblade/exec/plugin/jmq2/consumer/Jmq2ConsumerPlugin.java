package com.alibaba.chaosblade.exec.plugin.jmq2.consumer;

import com.alibaba.chaosblade.exec.common.aop.Enhancer;
import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.plugin.jmq2.Jmq2Plugin;

/**
 * @author wanyong10
 * @date 2020/10/30 - 2:55 下午
 */
public class Jmq2ConsumerPlugin extends Jmq2Plugin {

    @Override
    public String getName() {
        return CONSUMER_KEY;
    }

    @Override
    public PointCut getPointCut() {
        return new Jmq2ConsumerPointCut();
    }

    @Override
    public Enhancer getEnhancer() {
        return new Jmq2ConsumerEnhancer();
    }
}
