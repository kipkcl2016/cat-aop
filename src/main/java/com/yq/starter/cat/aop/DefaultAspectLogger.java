/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yq.starter.cat.aop;
/**
 *
 */


import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.yq.starter.cat.config.CatConfig;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Objects.isNull;

/**
 * @author andersen
 *
 */
public abstract class DefaultAspectLogger {
    protected boolean effective = true;
    protected boolean reentrant = true;
    private static final ThreadLocal<AtomicBoolean> entered = ThreadLocal.withInitial(() -> new AtomicBoolean(false));

    public DefaultAspectLogger() {
    }


    public DefaultAspectLogger(boolean effective) {
        this.effective = effective;
    }

    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        if (!CatConfig.getAopEffective() || !effective) {
            return pjp.proceed();
        } else {
            if (!reentrant) {
                Object obj;
                boolean result = entered.get().compareAndSet(false, true);
                if (result) {
                    obj = execute(pjp);
                    entered.get().set(false);
                    return obj;
                } else {
                    return pjp.proceed();
                }
            } else {
                return execute(pjp);
            }
        }
    }

    private Object execute(ProceedingJoinPoint pjp) throws Throwable {
        Transaction tx = getTransaction(pjp);
        Object object;
        try {
            object = pjp.proceed();
            if (!isNull(tx)) {
                tx.setSuccessStatus();
            }
            return object;
        } catch (Throwable e) {
            if (!isNull(tx)) {
                tx.setStatus(e);
                Cat.logError(e);
            }
            throw e;
        } finally {
            if (!isNull(tx)) {
                tx.complete();
            }
        }
    }


    /**
     * 方法执行前开始埋点
     * @param pjp 方法执行上下文
     * @return 埋点生成的transaction对象   注：可在其中加入若干event
     */
    abstract Transaction getTransaction(ProceedingJoinPoint pjp) throws Exception;
}
