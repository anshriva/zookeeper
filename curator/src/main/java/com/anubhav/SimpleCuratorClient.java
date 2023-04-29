package com.anubhav;

import org.apache.curator.CuratorZookeeperClient;
import org.apache.curator.retry.RetryOneTime;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;

public class SimpleCuratorClient {
    public static void main(String[] args) throws Exception {
        CuratorZookeeperClient client = new CuratorZookeeperClient(
                "localhost:2181",
                10000,
                10000,
                null,
                new RetryOneTime(1)
        );

        /* Types of retries available:
        1. Bounded Exponential Back off retry
        2. Exponential back off retry
        3. retry n times
        4. retry 1 time
        5. retry until elapsed
         */

        client.start();
        client.blockUntilConnectedOrTimedOut();

        var nodePathCreated = client.getZooKeeper().create(
                "/test",
                "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);

        System.out.println("node created ="+ nodePathCreated);
        client.close();
    }
}