package org.noear.nami;

import org.noear.nami.common.Result;

/**
 * Nami - 拦截器
 *
 * @author noear
 * @since 1.4
 */
public interface NamiInterceptor {
    /**
     * 拦截
     *
     * @param inv 调用者
     * */
    Result doIntercept(NamiInvocation inv) throws Throwable;
}