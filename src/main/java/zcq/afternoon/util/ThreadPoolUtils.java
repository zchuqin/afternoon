package zcq.afternoon.util;
/**
 * ***************************************************************************
 * Copyright (C) 2017 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 * ****************************************************************************
 */

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zhengchuqin
 * @version 1.0
 * @since 2019/10/08
 */
@Component
public class ThreadPoolUtils {
    private static ThreadPoolExecutor executor;
    private final static int CORE_SIZE = 5;
    private final static int MAX_SIZE = 10;
    private final static long KEEP_ALIVE_TIME = 1;
    private final static int CAPABILITY = 500;


    private ThreadPoolUtils() {}

    public static void execute(Runnable runnable, String threadName) {
        if (StringUtils.isNotBlank(threadName)) {
            executor.setThreadFactory(runnable1 -> new Thread(threadName));
            executor.execute(runnable);
        }
    }

    public static void submit(Callable callable, String threadName) {
        if (StringUtils.isNotBlank(threadName)) {
            executor.setThreadFactory(runnable1 -> new Thread(threadName));
            executor.submit(callable);
        }
    }

    @PostConstruct
    public void init() {
        executor = new ThreadPoolExecutor(CORE_SIZE, MAX_SIZE, KEEP_ALIVE_TIME, TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(CAPABILITY), new ThreadPoolExecutor.AbortPolicy());
    }
}
