package com.qxf.hadoop.zookeeper.util.zkclientapi;

import com.qxf.hadoop.zookeeper.util.zkclientapi.model.User;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.CreateMode;

/**
 * 创建节点
 *
 */
public class _2CreateNode {

	public static void main(String[] args) {
		// SerializableSerializer序列化器,可以直接传入java对象
		ZkClient zc = new ZkClient("192.168.10.5:2181", 10000, 10000, new SerializableSerializer());
		System.out.println("conneted ok!");

		User u = new User();
		u.setId(1);
		u.setName("test");
		String path = zc.create("/node1", u, CreateMode.PERSISTENT);
		System.out.println("created path:" + path);
	}

	/*
	console:
	conneted ok!
	created path:/node2	
	*/
}
