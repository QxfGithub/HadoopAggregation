package com.qxf.hadoop.hdfs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author qiuxuefu
 * @since 2019-03-18
 * @version 0.0.1
 */
@Configuration
public class HdfsConfig {
    @Value("${hdfs.path}")
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

