package com.alibaba.chaosblade.exec.plugin.jmq4.producer;

import com.alibaba.chaosblade.exec.common.aop.Enhancer;
import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.plugin.jmq4.Jmq4Plugin;

/**
 * @author wanyong10
 * @date 2020/10/30 - 3:15 下午
 */
public class Jmq4ProducerPlugin extends Jmq4Plugin {

    @Override
    public String getName() {
        return PRODUCER_KEY;
    }

    @Override
    public PointCut getPointCut() {
        return new Jmq4ProducerPointCut();
    }

    @Override
    public Enhancer getEnhancer() {
        return new Jmq4ProducerEnhancer();
    }
}
