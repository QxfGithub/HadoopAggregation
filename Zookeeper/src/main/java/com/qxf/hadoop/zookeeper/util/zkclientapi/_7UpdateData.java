package com.qxf.hadoop.zookeeper.util.zkclientapi;

import com.qxf.hadoop.zookeeper.util.zkclientapi.model.User;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

/**
 * 修改节点
 *
 */
public class _7UpdateData {

	public static void main(String[] args) {
		ZkClient zc = new ZkClient("192.168.10.5:2181", 10000, 10000, new SerializableSerializer());
		System.out.println("conneted ok!");
		
		User u = new User();
		u.setId(2);
		u.setName("test2");
		zc.writeData("/node1", u, -1);
	}

	/*
	console:
	conneted ok!
	*/
}
