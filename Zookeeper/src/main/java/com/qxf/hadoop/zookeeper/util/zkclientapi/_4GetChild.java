package com.qxf.hadoop.zookeeper.util.zkclientapi;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

import java.util.List;

/**
 * 获取子节点
 *
 */
public class _4GetChild {

	public static void main(String[] args) {
		ZkClient zc = new ZkClient("192.168.10.5:2181", 10000, 10000, new SerializableSerializer());
		System.out.println("conneted ok!");
		List<String> cList = zc.getChildren("/node1");
		System.out.println(cList.toString());
	}

	/*
	console:
	conneted ok!
	[node12]
	*/
}
