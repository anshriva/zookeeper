package com.anubhav;

// Refer: https://curator.apache.org/curator-recipes/index.html

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
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
        client.usingNamespace("counter");
        SharedCount sharedCount = new SharedCount(client, "/counter", 1);
        sharedCount.start();
        System.out.println(sharedCount.getCount());
        sharedCount.trySetCount(sharedCount.getVersionedValue(), sharedCount.getCount() + 1 );
        System.out.println(sharedCount.getCount());
    }

    private static void Queues(CuratorFramework client) throws Exception {
        client.usingNamespace("queue");

        SimpleDistributedQueue simpleDistributedQueue = new SimpleDistributedQueue(
                client,
                "/queue");


        simpleDistributedQueue.offer("item1".getBytes());

        System.out.println(new String(simpleDistributedQueue.take()));
    }
}
