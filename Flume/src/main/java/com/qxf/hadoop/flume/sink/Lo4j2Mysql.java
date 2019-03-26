package com.qxf.hadoop.flume.sink;

import org.apache.log4j.Logger;

/**
 * log4j输出直接保存到数据库。
 *
 */
public class Lo4j2Mysql {
    protected static final Logger logger = Logger.getLogger(Lo4j2Mysql.class);

    public static void main(String[] args) throws Exception {
        int name = 0;
        while (true) {
            logger.info("log4j-flume-name-"+name+","+(name++));
            Thread.sleep(6000);
        }
    }

}
