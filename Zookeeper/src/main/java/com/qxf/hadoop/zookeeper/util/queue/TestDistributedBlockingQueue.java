package com.qxf.hadoop.zookeeper.util.queue;

import com.qxf.hadoop.zookeeper.model.User;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 阻塞分布式队列

 * @author qiuxuefu
 * @since 2019-04-11
 * @version 0.0.1
 */
public class TestDistributedBlockingQueue {

    public static void main(String[] args) {

        ScheduledExecutorService delayExector = Executors.newScheduledThreadPool(1);
        int delayTime = 5;

        ZkClient zkClient = new ZkClient("10.59.74.192:2181", 5000, 5000, new SerializableSerializer());
        final DistributedBlockingQueue<User> queue = new DistributedBlockingQueue<>(zkClient, "/queue");

        final User user1 = new User();
        user1.setId("1");
        user1.setName("jerome1");

        final User user2 = new User();
        user2.setId("2");
        user2.setName("jerome2");

        try {

            delayExector.schedule(new Runnable() {

                public void run() {
                    try {
                        queue.offer(user1);
                        queue.offer(user2);
                        System.out.println("queue.offer end!");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }, delayTime, TimeUnit.SECONDS);


            User u1 = queue.poll();
            User u2 = queue.poll();
            System.out.println("queue.poll() u1 = " + u1.toString());
            System.out.println("queue.poll() u2 = " + u2.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            delayExector.shutdown();
            try {
                delayExector.awaitTermination(2, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /* console: 阻塞分布式队列
    queue.poll() u1 = User{name='jerome1', id='1'}
    queue.poll() u2 = User{name='jerome2', id='2'}
    queue.offer end!
    */
}
