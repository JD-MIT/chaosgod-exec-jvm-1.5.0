package com.alibaba.chaosblade.exec.plugin.jsf.model;

import com.alibaba.chaosblade.exec.common.model.action.threadpool.WaitingTriggerThreadPoolFullExecutor;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author BoYuan Han
 */
public class JsfThreadPoolFullExecutor extends WaitingTriggerThreadPoolFullExecutor {

    public static final JsfThreadPoolFullExecutor INSTANCE = new JsfThreadPoolFullExecutor();

    private static final Logger LOGGER = LoggerFactory.getLogger(JsfThreadPoolFullExecutor.class);

    private volatile Object wrappedChannelHandler;


    @Override
    public ThreadPoolExecutor getThreadPoolExecutor() {
        if (wrappedChannelHandler == null) {
            return null;
        }
        try {
            Object executorService = ReflectUtil.invokeMethod(wrappedChannelHandler, "getBizThreadPool",
                new Object[0], true);
            if (executorService == null) {
                LOGGER.warn("can't get executor service by getBizThreadPool method");
                return null;
            }
            if (ThreadPoolExecutor.class.isInstance(executorService)) {
                return (ThreadPoolExecutor)executorService;
            }
        } catch (Exception e) {
            LOGGER.warn("invoke getBizThreadPool method of WrappedChannelHandler exception", e);
        }
        return null;
    }

    /**
     * Set wrappedChannelHandler for getting threadPoolExecutor object.
     *
     * @param wrappedChannelHandler
     */
    public void setWrappedChannelHandler(Object wrappedChannelHandler) {
        if (isExpReceived() && this.wrappedChannelHandler == null) {
            try {
                 this.wrappedChannelHandler = wrappedChannelHandler;
                 triggerThreadPoolFull();
            } catch (Exception e) {
                LOGGER.warn("set WrappedChannelHandler exception", e);
            }
        }
    }

    @Override
    protected void doRevoke() {
        setExpReceived(false);
        wrappedChannelHandler = null;
    }
}
