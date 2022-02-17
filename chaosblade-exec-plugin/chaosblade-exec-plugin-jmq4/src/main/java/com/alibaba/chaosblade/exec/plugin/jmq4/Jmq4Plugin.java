package com.alibaba.chaosblade.exec.plugin.jmq4;

import com.alibaba.chaosblade.exec.common.aop.Plugin;
import com.alibaba.chaosblade.exec.common.model.ModelSpec;
import com.alibaba.chaosblade.exec.plugin.jmq4.model.Jmq4ModelSpec;

/**
 * @author wanyong10
 * @date 2020/10/30 - 2:28 下午
 */
public abstract class Jmq4Plugin implements Plugin, Jmq4Constant {

    @Override
    public ModelSpec getModelSpec() {
        return new Jmq4ModelSpec();
    }
}
