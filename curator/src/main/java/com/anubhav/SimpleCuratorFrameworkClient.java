package com.anubhav;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;
import org.apache.zookeeper.CreateMode;

import java.nio.charset.StandardCharsets;

public class SimpleCuratorFrameworkClient {

    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient(
                "localhost:2181",
                new RetryOneTime(1));


        client.start();
        client.blockUntilConnected();

        /*
        Curator framework has concept of namespaces.
        So, even if the subsequent code creates a path named /test_path
        The actual path created will be /App/test_path.
        It will prepend /App to all the paths
         */

        client = client.usingNamespace("myApp");

        var path  = client.create().
                withMode(CreateMode.PERSISTENT).
                forPath("/test_path1", "".getBytes(StandardCharsets.UTF_8));

        System.out.println("created path = "+ path);
    }
}
