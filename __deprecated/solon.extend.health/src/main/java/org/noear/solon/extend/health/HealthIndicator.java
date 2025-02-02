package org.noear.solon.extend.health;

import org.noear.solon.core.handle.Result;

/**
 * 健康指示器
 *
 * @author noear
 * @since 1.5
 */
@Deprecated
@FunctionalInterface
public interface HealthIndicator {
    /**
     * 获取结果
     * */
    Result get();
}
