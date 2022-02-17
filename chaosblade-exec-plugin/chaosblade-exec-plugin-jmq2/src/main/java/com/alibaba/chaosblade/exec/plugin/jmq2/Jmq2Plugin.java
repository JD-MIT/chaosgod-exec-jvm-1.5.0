package com.alibaba.chaosblade.exec.plugin.jmq2;

import com.alibaba.chaosblade.exec.common.aop.Plugin;
import com.alibaba.chaosblade.exec.common.model.ModelSpec;
import com.alibaba.chaosblade.exec.plugin.jmq2.model.Jmq2ModelSpec;

/**
 * @author wanyong10
 * @date 2020/10/30 - 2:28 下午
 */
public abstract class Jmq2Plugin implements Plugin, Jmq2Constant {

    @Override
    public ModelSpec getModelSpec() {
        return new Jmq2ModelSpec();
    }
}
