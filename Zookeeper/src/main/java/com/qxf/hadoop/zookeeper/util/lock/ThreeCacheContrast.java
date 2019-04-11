package com.qxf.hadoop.zookeeper.util.lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.UnhandledErrorListener;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * @author qiuxuefu
 * @since 2019-04-11
 * @version 0.0.1
 * 三种缓存比较 https://blog.csdn.net/Leafage_M/article/details/78735485
 */
public class ThreeCacheContrast {
    private static int num = 1;

    private static final String zkAddress = "10.59.74.192:2181";
    private static final int sessionTimeout = 2000;
    private static String parentPath = "/Curator-Recipes";//父节点

    private static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString(zkAddress)
            .sessionTimeoutMs(sessionTimeout)
            .retryPolicy(new ExponentialBackoffRetry(1000,3))
            .build();

    public static void main(String[] args) throws InterruptedException {

        init();
        treeCache();
        pathChildrenCache();
        nodeCache();
        testData();
        Thread.sleep(Integer.MAX_VALUE);

    }

    /**
     * 初始化操作，创建父节点
     */
    public static void init() {
        client.start();
        try {
            //可以用用来确保父节点存在，2.9之后弃用
//            EnsurePath ensurePath = new EnsurePath(parentPath);
//            ensurePath.ensure(client.getZookeeperClient());

            if (client.checkExists().forPath(parentPath) == null) {
                client.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(parentPath, "This is Parent Data!".getBytes());
            }

            client.create().withMode(CreateMode.EPHEMERAL).forPath(parentPath + "/c1","This is C1.".getBytes());//创建第一个节点

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 监听子节点变化
     */
    public static void pathChildrenCache() {
        final PathChildrenCache pathChildrenCache = new PathChildrenCache(client, parentPath, true);
        try {
            pathChildrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);//启动模式
        } catch (Exception e) {
            e.printStackTrace();
        }
        //添加监听
        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                System.out.println( num++ + ".pathChildrenCache------发生的节点变化类型为：" + pathChildrenCacheEvent.getType() + ",发生变化的节点内容为：" + new String(pathChildrenCacheEvent.getData().getData()) + "\n======================\n");
            }
        });
    }


    /**
     * 监听节点数据变化
     */
    public static void nodeCache() {
        final NodeCache nodeCache = new NodeCache(client, parentPath, false);
        try {
            nodeCache.start(true);//true代表缓存当前节点
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (nodeCache.getCurrentData() != null) {//只有start中的设置为true才能够直接得到
            System.out.println( num++ + ".nodeCache-------CurrentNode Data is:" + new String(nodeCache.getCurrentData().getData()) + "\n===========================\n");//输出当前节点的内容
        }

        //添加节点数据监听
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            public void nodeChanged() throws Exception {
                System.out.println( num++ + ".nodeCache------节点数据发生了改变，发生的路径为：" + nodeCache.getCurrentData().getPath() + ",节点数据发生了改变 ，新的数据为：" + new String(nodeCache.getCurrentData().getData()) + "\n===========================\n");
            }
        });
    }

    /**
     * 同时监听数据变化和子节点变化
     */
    public static void treeCache() {
        final TreeCache treeCache = new TreeCache(client, parentPath);
        try {
            treeCache.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //添加错误
        treeCache.getUnhandledErrorListenable().addListener(new UnhandledErrorListener() {
            public void unhandledError(String s, Throwable throwable) {
                System.out.println(num++ + ".错误原因：" + throwable.getMessage() + "\n==============\n");
            }
        });

        treeCache.getListenable().addListener(new TreeCacheListener() {
            public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) throws Exception {
                System.out.println( num++ + ".treeCache------当前发生的变化类型为：" +  treeCacheEvent.getType() + ",发生变化的节点内容为：" + new String(treeCacheEvent.getData().getData()) + "\n=====================\n");
            }
        });
    }

    /**
     * 创建节点、修改数据、删除节点等操作，用来给其他的监听器测试使用
     */
    public static void testData() {
        try {
            client.create().withMode(CreateMode.EPHEMERAL).forPath(parentPath + "/c2","This is C2.".getBytes());//创建第一个节点
            client.create().withMode(CreateMode.EPHEMERAL).forPath(parentPath + "/c3","This is C3.".getBytes());//创建第一个节点

            client.setData().forPath(parentPath + "/c2", "This is New C2.".getBytes());//修改节点数据

            client.delete().forPath(parentPath + "/c3");//删除一个节点

            client.delete().deletingChildrenIfNeeded().forPath(parentPath);//将父节点下所有内容删除

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}


