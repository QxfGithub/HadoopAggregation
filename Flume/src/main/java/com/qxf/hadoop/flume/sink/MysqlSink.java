package com.qxf.hadoop.flume.sink;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import org.apache.flume.*;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;


public class MysqlSink extends AbstractSink implements Configurable {

    private Logger LOG = LoggerFactory.getLogger(MysqlSink.class);

    private String hostname;
    private String port;
    private String databaseName;
    private String tableName="t_user";
    private String user;
    private String password;
    private PreparedStatement preparedStatement;
    private Connection conn;
    private int batchSize;		//每次提交的批次大小

    public MysqlSink() {
        LOG.info("MySqlSink start...");
    }

    //**实现Configurable接口中的方法：可获取配置文件中的属性
    public void configure(Context context) {
        hostname = context.getString("hostname");
        Preconditions.checkNotNull(hostname, "hostname must be set!!");
        port = context.getString("port");
        Preconditions.checkNotNull(port, "port must be set!!");
        databaseName = context.getString("databaseName");
        Preconditions.checkNotNull(databaseName, "databaseName must be set!!");
        tableName = context.getString("tableName");
        Preconditions.checkNotNull(tableName, "tableName must be set!!");
        user = context.getString("user");
        Preconditions.checkNotNull(user, "user must be set!!");
        password = context.getString("password");
        Preconditions.checkNotNull(password, "password must be set!!");
        batchSize = context.getInteger("batchSize", 100);		//设置了batchSize的默认值
        Preconditions.checkNotNull(batchSize > 0, "batchSize must be a positive number!!");
    }

    /**
     * 服务启动时执行的代码，这里做准备工作
     */
    @Override
    public void start() {
        super.start();
        try {
            //调用Class.forName()方法加载驱动程序
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //String url = "jdbc:mysql://" + hostname + ":" + port + "/" + databaseName;
        String url = "jdbc:mysql://10.59.72.36:3306/test_flume_db";

        //调用DriverManager对象的getConnection()方法，获得一个Connection对象

        try {
            //conn = DriverManager.getConnection(url, user, password);
            conn = DriverManager.getConnection(url, "dev", "dev");
            conn.setAutoCommit(false);
            //创建一个Statement对象
            preparedStatement = conn.prepareStatement("insert into " + tableName +
                    " (name,age) values (?,?)");

        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    /**
     * 服务关闭时执行
     */
    @Override
    public void stop() {
        super.stop();
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *  执行的事情：<br/>
     （1）持续不断的从channel中获取event放到batchSize大小的数组中<br/>
     （2）event可以获取到则进行event处理，否则返回Status.BACKOFF标识没有数据提交<br/>
     （3）batchSize中有内容则进行jdbc提交<br/>
     */
    public Status process() throws EventDeliveryException {
        Status result = Status.READY;
        Channel channel = getChannel();
        Transaction transaction = channel.getTransaction();
        Event event;
        String content;

        List<Person> persons = Lists.newArrayList();
        transaction.begin();


        try {
            //event处理
            for (int i = 0; i < batchSize; i++) {
                event = channel.take();
                if (event != null) {//对事件进行处理
                    //event 的 body 为   "exec tail-event-$i , $i"
                    content = new String(event.getBody());
                    Person person=new Person();
                    if (content.contains(",")) {
                        //存储 event 的 content
                        person.setName(content.substring(0, content.indexOf(",")));
                        //存储 event 的 create  +1 是要减去那个 ","
                        person.setAge(Integer.parseInt(content.substring(content.indexOf(",")+1).trim()));
                    }else{
                        person.setName(content);
                    }
                    persons.add(person);
                } else {
                    result = Status.BACKOFF;
                    break;
                }
            }

            //jdbc提交
            if (persons.size() > 0) {
                preparedStatement.clearBatch();
                for (Person temp : persons) {
                    preparedStatement.setString(1, temp.getName());
                    preparedStatement.setInt(2, temp.getAge());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
                conn.commit();
            }
            transaction.commit();
        } catch (Exception e) {
            try {
                transaction.rollback();
            } catch (Exception e2) {
                LOG.error("Exception in rollback. Rollback might not have been.successful.", e2);
            }
            LOG.error("Failed to commit transaction.Transaction rolled back.", e);
            Throwables.propagate(e);
        } finally {
            transaction.close();
        }
        return result;
    }

}
