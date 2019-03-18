package com.qxf.hadoop.hbase.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @author qiuxuefu
 * @since 2019-03-18
 * @version 0.0.1
 */
public class PropertiesUtil {

    private Properties props;

    private PropertiesUtil(String propConfigFile){

        props = new Properties();
        try {
            BufferedReader is = new BufferedReader(
                    new InputStreamReader(this.getClass().getResourceAsStream(propConfigFile), "UTF-8"));//使用字节转转化为字符流；1字符=2字节
            props.load(is);
        } catch (IOException e) {
            System.out.println("no file: "+propConfigFile+" -->"+e.getMessage());

        }
    }

    private static PropertiesUtil configuration = null;

    /**
     * 得到Configuration的实例，可以用在util包中
     * @return
     */
    public static PropertiesUtil getInstance(){

        if (configuration == null) {
            configuration=new PropertiesUtil("/application.properties");//在resource资源路径下的application.properties配置文件的内部信息
        }

        return configuration;
    }

    /**
     * 获取指定配置文件的内容
     * @param key
     * @return
     */
    public String getProperty(String key){
        return props.getProperty(key);

    }

}
