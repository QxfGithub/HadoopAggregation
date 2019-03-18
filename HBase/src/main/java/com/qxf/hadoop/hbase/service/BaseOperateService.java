package com.qxf.hadoop.hbase.service;


import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;

import java.util.List;

/**
 * @author qiuxuefu
 * @since 2019-03-18
 * @version 0.0.1
 */
public interface BaseOperateService {

    void createTable(String tableName,String[] columnDescriptors)throws Exception;

    void dropTable(String tableName) throws Exception;

    void alterTable(String tableName,String[] columnDescriptors) throws Exception;

    void putRow(String tableName,String rowKey,String familyName,String columnName,String value)throws Exception;

    Result getRow(String tableName, String rowKey)throws Exception;

    void putRows(String tableName, List<Put> puts) throws Exception;

    String[] getAllTables() throws Exception;

    Result getColResult(String tableName, String rowKey, String familyName, String columnName) throws Exception;

    ResultScanner getResultScanner(String tableName) throws Exception;

    void deleteRow(String tableName, String rowKey) throws Exception;

    void deleteColumn(String tableName, String rowKey, String falilyName, String columnName) throws Exception;
}
