package com.alibaba.chaosblade.exec.plugin.jmq2.producer;

import com.alibaba.chaosblade.exec.common.aop.Enhancer;
import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.plugin.jmq2.Jmq2Plugin;

/**
 * @author wanyong10
 * @date 2020/10/30 - 3:15 下午
 */
public class Jmq2ProducerPlugin extends Jmq2Plugin {

    @Override
    public String getName() {
        return PRODUCER_KEY;
    }

    @Override
    public PointCut getPointCut() {
        return new Jmq2ProducerPointCut();
    }

    @Override
    public Enhancer getEnhancer() {
        return new Jmq2ProducerEnhancer();
    }
}
