package com.qxf.hadoop.zookeeper.util.nameservice;

/**
 * 删除策略
 *
 */
public enum RemoveMethodEnum {
    NONE,
    /** 立刻删除 */
    IMMEDIATELY,
    /** 延迟删除 */
    DELAY
}
