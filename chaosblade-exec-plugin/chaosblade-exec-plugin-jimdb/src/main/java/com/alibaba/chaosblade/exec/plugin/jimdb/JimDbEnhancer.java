/*
 * Copyright 1999-2019 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.chaosblade.exec.plugin.jimdb;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author guoping.yao <a href="mailto:bryan880901@qq.com">
 */
public class JimDbEnhancer extends BeforeEnhancer {

    public static final String CHARSET = "UTF-8";
    private static final Logger LOGGER = LoggerFactory.getLogger(JimDbEnhancer.class);

    /**
     * final RedisOutputStream os,
     * final byte[] command,
     * final byte[]... args
     */
    @Override
    public EnhancerModel doBeforeAdvice(ClassLoader classLoader, String className, Object object,
                                        Method method, Object[] methodArguments)
        throws Exception {
        if (className.equals("com.jd.jim.cli.cache.MultiLevelLocalCache")){
            JimDbModelSpec.loseCache(object);
            return null;
        }
        if (methodArguments == null || methodArguments.length != 3) {
            LOGGER.info("The necessary parameters is null or length is not equal 3, {}",
                    methodArguments != null ? methodArguments.length : null);
            return null;
        }
        if (className.equals("com.jd.jim.cli.protocol.JimCommandBuilder")){
            return enhanceJimDb2(classLoader,className,object,method,methodArguments);
        }
        if(className.equals("com.jd.jim.cli.redis.jedis.Protocol")){
            return enhanceJimDb1(classLoader,methodArguments);
        }

        return null;
    }
    private EnhancerModel enhanceJimDb2(ClassLoader classLoader, String className, Object object, Method method, Object[] methodArguments) throws Exception {

        MatcherModel matcherModel = new MatcherModel();
        Object cmd = methodArguments[0];
        if (null == cmd){
            return null;
        }else {
            String cmdName = ReflectUtil.invokeMethod(cmd,"name",new Object[0],false);
            if (null == cmdName || commandIgnoreSet.contains(cmdName.toUpperCase())){
                return null;
            }
            matcherModel.add(JimDbConstant.COMMAND_TYPE_MATCHER_NAME,cmdName);
        }

        Object args = methodArguments[2];
        Object key = ReflectUtil.getFieldValue(args,"firstKey",false);
        if (key instanceof String){
            matcherModel.add(JimDbConstant.KEY_MATCHER_NAME, key);
            if (LOGGER.isDebugEnabled()) {
//                LOGGER.debug("jimdb matchers: {}", JSON.toJSONString(matcherModel));
            }
            return new EnhancerModel(classLoader, matcherModel);
        }
        return null;
    }

    private EnhancerModel enhanceJimDb1(ClassLoader classLoader, Object[] methodArguments) throws UnsupportedEncodingException {
        Object command = methodArguments[1];
        if (!(command instanceof byte[])) {
            return null;
        }

        Object args = methodArguments[2];
        if (!args.getClass().isArray() || !(args instanceof byte[][])) {
            return null;
        }
        String cmd = new String((byte[])command, CHARSET);
        if (null == cmd || commandIgnoreSet.contains(cmd.toUpperCase())){
            return null;
        }
        List<String> sargs = new ArrayList<String>();

        byte[][] bargs = (byte[][])args;
        for (int i = 0; i < bargs.length; i++) {
            sargs.add(new String(bargs[i], CHARSET));
        }

        String key = null;
        if (sargs.size() > 0) {
            key = sargs.get(0);
        }

        MatcherModel matcherModel = new MatcherModel();
        matcherModel.add(JimDbConstant.COMMAND_TYPE_MATCHER_NAME, cmd.toLowerCase());
        if (key != null) {
            matcherModel.add(JimDbConstant.KEY_MATCHER_NAME, key);
        }
        if (LOGGER.isDebugEnabled()) {
//            LOGGER.debug("jimdb matchers: {}", JSON.toJSONString(matcherModel));
        }
        return new EnhancerModel(classLoader, matcherModel);
    }

    private static Set<String> commandIgnoreSet = new HashSet<String>(){
        {
            add("AUTH");
            add("ECHO");
            add("PING");
            add("QUIT");
            add("READONLY");
            add("READWRITE");
            add("SELECT");
            add("BGREWRITEAOF");
            add("BGSAVE");
            add("CLIENT");
            add("COMMAND");
            add("CONFIG");
            add("DBSIZE");
            add("DEBUG");
            add("FLUSHALL");
            add("FLUSHDB");
            add("INFO");
            add("MYID");
            add("LASTSAVE");
            add("ROLE");
            add("MONITOR");
            add("SAVE");
            add("SHUTDOWN");
            add("SLAVEOF");
            add("SLOWLOG");
            add("SYNC");
            add("PFADD");
            add("PFCOUNT");
            add("PFMERGE");
            add("PSUBSCRIBE");
            add("PUBLISH");
            add("PUNSUBSCRIBE");
            add("SUBSCRIBE");
            add("UNSUBSCRIBE");
            add("PUBSUB");
            add("EVAL");
            add("EVALSHA");
            add("SCRIPT");
            add("LOAD");
            add("TIME");
            add("WAIT");
            add("SENTINEL");
            add("SYNCEX");
            add("ASKING");
            add("CLUSTER");
            add("HOTKEY");
            add("NOHOTKEY");
            add("IDLETIME");
        }
    };
}
