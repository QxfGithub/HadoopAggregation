package com.qxf.hadoop.hbase.service.impl;

import com.qxf.hadoop.hbase.service.BaseOperateService;
import com.qxf.hadoop.hbase.util.HBaseConnectUtil;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.regionserver.BloomType;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qiuxuefu
 * @since 2019-03-18
 * @version 0.0.1
 */
@Service
public class BaseOperateServiceImpl implements BaseOperateService {
    @Override
    public void deleteRow(String tableName, String rowKey) throws Exception {
        HTable htable=new HTable(HBaseConnectUtil.getConf(), tableName);
        Delete de =new Delete(Bytes.toBytes(rowKey));
        htable.delete(de);
        HBaseConnectUtil.closeConn();
    }

    @Override
    public void deleteColumn(String tableName, String rowKey, String familyName, String columnName) throws Exception {
        HTable htable=new HTable(HBaseConnectUtil.getConf(), tableName);
        Delete de =new Delete(Bytes.toBytes(rowKey));
        de.deleteColumn(Bytes.toBytes(familyName), Bytes.toBytes(columnName));
        htable.delete(de);
        HBaseConnectUtil.closeConn();
    }

    @Override
    public ResultScanner getResultScanner(String tableName) throws Exception {
        Scan scan=new Scan();
        ResultScanner rs =null;
        HTable htable=new HTable(HBaseConnectUtil.getConf(), tableName);
        try{
            rs=htable.getScanner(scan);
            for(Result r: rs){
                for(KeyValue kv:r.list()){
                    System.out.println(Bytes.toString(kv.getRow()));
                    System.out.println(Bytes.toString(kv.getFamily()));
                    System.out.println(Bytes.toString(kv.getQualifier()));
                    System.out.println(Bytes.toString(kv.getValue()));
                    System.out.println(kv.getTimestamp());
                }
            }
        }finally{
            rs.close();
        }
        return rs;
    }

    @Override
    public Result getColResult(String tableName, String rowKey, String familyName, String columnName) throws Exception {
        Get get=new Get(Bytes.toBytes(rowKey));
        HTable htable=new HTable(HBaseConnectUtil.getConf(), Bytes.toBytes(tableName));
        get.addColumn(Bytes.toBytes(familyName),Bytes.toBytes(columnName));
        Result result=htable.get(get);
        for(KeyValue k:result.list()){
            System.out.println(Bytes.toString(k.getFamily()));
            System.out.println(Bytes.toString(k.getQualifier()));
            System.out.println(Bytes.toString(k.getValue()));
            System.out.println(k.getTimestamp());
        }
        return result;
    }

    @Override
    public String[] getAllTables() throws Exception {
        // 从连接中构造一个DDL操作器
        HBaseAdmin admin =new HBaseAdmin(HBaseConnectUtil.getConf());
        String[] tableNames = admin.getTableNames();
        for(int i=0;i<tableNames.length;i++){
            System.out.println(tableNames[i]);
        }
        return tableNames;
    }

    @Override
    public void putRows(String tableName, List<Put> puts) throws Exception {
        Connection HBaseConn = HBaseConnectUtil.getHBaseConn();
        Table table = HBaseConn.getTable(TableName.valueOf(tableName));
        table.put(puts);
    }

    @Override
    public Result  getRow(String tableName, String rowKey) throws Exception {
        Connection HBaseConn = HBaseConnectUtil.getHBaseConn();
        Table table = HBaseConn.getTable(TableName.valueOf(tableName)) ;
        Get get = new Get(Bytes.toBytes(rowKey));
        Result result = table.get(get);

        System.out.println(result.toString());
        List<KeyValue> list = result.list();
        for (int i = 0; i < list.size(); i++) {
            KeyValue kv = list.get(i);
            printKeyValye(kv);
        }

        return table.get(get);
    }

    @Override
    public void putRow(String tableName,String rowKey,String familyName,String columnName,String value) throws Exception {
        /*Connection HBaseConn = HBaseConnectUtil.getHBaseConn();
        Table table = HBaseConn.getTable(TableName.valueOf(tableName));
        Put put = new Put(Bytes.toBytes(rowKey));
        put.addColumn(Bytes.toBytes(familyName),Bytes.toBytes(columnName),Bytes.toBytes(value));
        table.put(put);
        HBaseConnectUtil.closeConn();*/

        HTable htable=new HTable(HBaseConnectUtil.getConf(), Bytes.toBytes(tableName));
        Put put=new Put(Bytes.toBytes(rowKey));
        put.add(Bytes.toBytes(familyName), Bytes.toBytes(columnName), Bytes.toBytes(value));
        htable.put(put);
        HBaseConnectUtil.closeConn();
    }

    @Override
    public void alterTable(String tableName, String[] columnDescriptors) throws Exception {
        // 从连接中构造一个DDL操作器
        Admin admin = HBaseConnectUtil.getHBaseConn().getAdmin();

        // 取出旧的表定义信息
        HTableDescriptor tableDescriptor = admin.getTableDescriptor(TableName.valueOf(tableName));

        for (String col : columnDescriptors){
            tableDescriptor.addFamily(new HColumnDescriptor(col).setBloomFilterType(BloomType.ROWCOL));//设置该列族的布隆过滤器类型
        }

        // 将修改过的表定义交给admin去提交
        admin.modifyTable(TableName.valueOf(tableName), tableDescriptor);

        admin.close();
        HBaseConnectUtil.closeConn();
    }

    @Override
    public void dropTable(String tableName) throws Exception {
        // 从连接中构造一个DDL操作器
        Admin admin = HBaseConnectUtil.getHBaseConn().getAdmin();
        // 停用表
        admin.disableTable(TableName.valueOf(tableName));
        // 删除表
        admin.deleteTable(TableName.valueOf(tableName));

        admin.close();

        HBaseConnectUtil.closeConn();
    }

    @Override
    public void createTable(String tableName, String[] columnDescriptors) throws Exception{

        // 从连接中构造一个DDL操作器
        Admin admin = HBaseConnectUtil.getHBaseConn().getAdmin();

        // 创建一个表定义描述对象
        HTableDescriptor hTableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));

        for (String col : columnDescriptors){
            hTableDescriptor.addFamily(new HColumnDescriptor(col));
        }

        // 用ddl操作器对象：admin 来建表
        admin.createTable(hTableDescriptor);

        // 关闭连接
        admin.close();

        HBaseConnectUtil.closeConn();
    }

    public static void printKeyValye(KeyValue kv) {

        System.out.println(Bytes.toString(kv.getRow()) + "\t" + Bytes.toString(kv.getFamily()) + "\t" + Bytes.toString(kv.getQualifier()) + "\t" + Bytes.toString(kv.getValue()) + "\t" + kv.getTimestamp());

    }
}
