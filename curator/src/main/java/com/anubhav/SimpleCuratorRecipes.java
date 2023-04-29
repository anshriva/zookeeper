package com.anubhav;

// Refer: https://curator.apache.org/curator-recipes/index.html

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.queue.SimpleDistributedQueue;
import org.apache.curator.framework.recipes.shared.SharedCount;
import org.apache.curator.retry.RetryNTimes;

public class SimpleCuratorRecipes {
    public static void main(String[] args) throws Exception {

        CuratorFramework client = CuratorFrameworkFactory.newClient(
                        "localhost:2181",
                        new RetryNTimes(2, 1000));

        client.start();
        client.blockUntilConnected();
        Queues(client);
        Counter(client);
        client.close();
    }

    private static void Counter(CuratorFramework client) throws Exception {
        System.out.println("********going to implement counter*******");
        client = client.usingNamespace("counter");
        SharedCount sharedCount = new SharedCount(client, "/counter", 1);
        sharedCount.start();
        InterProcessMutex lock = new InterProcessMutex(client, "/counter-lock");

        try {
            lock.acquire();
            int count = sharedCount.getCount();
            System.out.println("Current count value: " + count);
            sharedCount.trySetCount(sharedCount.getVersionedValue(), count + 1);
            System.out.println("New count value: " + sharedCount.getCount());
        } finally {
            lock.release();
        }
    }

    private static void Queues(CuratorFramework client) throws Exception {
        System.out.println("*****going to implement queue*****");
        client = client.usingNamespace("queue");

        SimpleDistributedQueue simpleDistributedQueue = new SimpleDistributedQueue(
                client,
                "/queue");

        System.out.println("going to enqueue and item = item1");
        simpleDistributedQueue.offer("item1".getBytes());

        System.out.println("dequeue = " +new String(simpleDistributedQueue.take()));
    }
}
