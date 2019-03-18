package com.qxf.hadoop.hbase.util;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;

import java.io.IOException;
/**
 * @author qiuxuefu
 * @since 2019-03-18
 * @version 0.0.1
 */
public class HBaseConnectUtil {

    private static final HBaseConnectUtil INSTANCE = new HBaseConnectUtil();

    private static Configuration configuration; //hbase配置
    private static Connection connection; //hbase connection

    private HBaseConnectUtil(){
        try{
            if (configuration==null){
                configuration = HBaseConfiguration.create();
                configuration.set("hbase.zookeeper.quorum",PropertiesUtil.getInstance().getProperty("zookeeper.quorum"));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private  Connection getConnection(){
        if (connection==null || connection.isClosed()){
            try{
                connection = ConnectionFactory.createConnection(configuration);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return connection;
    }
    public static Connection getHBaseConn(){
        return INSTANCE.getConnection();
    }

    public static Configuration getConf(){
        return configuration;
    }

    public static Table getTable(String tableName) throws IOException {
        return INSTANCE.getConnection().getTable(TableName.valueOf(tableName));
    }
    public static void closeConn(){
        if (connection!=null){
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
