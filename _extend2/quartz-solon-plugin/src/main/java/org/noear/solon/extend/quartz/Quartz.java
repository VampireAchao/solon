package org.noear.solon.extend.quartz;

import org.noear.solon.annotation.XNote;

import java.lang.annotation.*;

/**
 * Quartz 任务注解，支持：java.lang.Runnable 或 org.quartz.Job
 *
 * <pre><code>
 * @Quartz(cron7x = "0 0/1 * * * ? *")
 * public class QuartzJob implements Job {
 *     @Override
 *     public void execute(JobExecutionContext ctx) throws JobExecutionException {
 *         ...
 *     }
 * }
 *
 * @Quartz(cron7x = "200ms")
 * public class QuartzRun1 implements Runnable {
 *     @Override
 *     public void run() {
 *         ...
 *     }
 * }
 * </code></pre>
 *
 * @author noear
 * @since 1.0
 * */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Quartz {
    @XNote("cronx = 或cron：支持7位（秒，分，时，天，月，周，年） 或速配： 100ms,2s,1m,1h,1d(ms:毫秒；s:秒；m:分；h:小时；d:天)")
    String cron7x();
    boolean enable() default true;
    String name() default "";
}

