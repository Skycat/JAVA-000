package io.github.skycat.geekstudy.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;

/**
 * 可命名的线程工厂
 * @author Skycat
 * @email skycat2004@gmail.com
 * @version 1.0 2019-04-13 22:49:51
 */
public class NamedThreadFactory implements ThreadFactory {
    /** 名称前缀 */
    private final String prefix;
    /** 线程组 */
    private final ThreadGroup threadGroup;
    /** 序号 */
    private final AtomicInteger sn = new AtomicInteger(1);
    
    /**
     * 构造方法
     * @param prefix 线程名称前缀
     */
    public NamedThreadFactory(String prefix) {
        StringBuilder sb = new StringBuilder(prefix);
        sb.append("-");
        this.prefix = sb.toString();
        this.threadGroup = new ThreadGroup(prefix);
    }
    
    /**
     * 构造方法
     * @param prefix 线程名称前缀
     * @param threadGroup 线程组
     */
    public NamedThreadFactory(String prefix, ThreadGroup threadGroup) {
        StringBuilder sb = new StringBuilder();
        String groupName = threadGroup.getName();
        if (StringUtils.isNotBlank(groupName)) {
            sb.append(groupName).append(":");
        }
        sb.append(prefix).append("-");
        this.prefix = sb.toString();
        this.threadGroup = threadGroup;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        StringBuilder sb = new StringBuilder(prefix);
        sb.append(sn.getAndIncrement());
        return new Thread(threadGroup, runnable, sb.toString());
    }
}
