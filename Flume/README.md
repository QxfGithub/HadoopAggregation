一、数据存入mysql
1、创建一个Maven项目 
2、将代码打成jar包后，上传到flume安装目录下的lib文件夹中，同时需要上传MySQL的驱动jar包 
3、给flume添加mysqlSink.conf文件: 
4、启动flume命令： flume-ng agent --conf conf --conf-file conf/example.conf --name a1 -Dflume.root.logger=INFO,console 
5、在Mysql中创建一个表： 
CREATE TABLE `t_user` (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=229 DEFAULT CHARSET=utf8;
6、生成数据到目标日志文件中： for i in {1..100};do echo "exec tail-name-$i,$i" >> /data1/opt/flume1.9.0/log;done; 
7、完成后，数据和预想中的一样，写入了数据库中。 mysql> select * from t_user;