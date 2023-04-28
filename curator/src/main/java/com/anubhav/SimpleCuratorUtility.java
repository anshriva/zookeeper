package com.anubhav;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;
import org.apache.curator.test.TestingServer;
import org.apache.zookeeper.CreateMode;

import java.nio.charset.StandardCharsets;

public class SimpleCuratorUtility {
    public static void main(String[] args) throws Exception {
        try (var testingServer = new TestingServer()) {
            CuratorFramework client = CuratorFrameworkFactory.newClient(
                    testingServer.getConnectString(),
                    new RetryOneTime(1000));

            client.start();

            var nodeCreated = client.create().
                    withMode(CreateMode.EPHEMERAL).
                    forPath("/test_path", "".getBytes(StandardCharsets.UTF_8));

            System.out.println("node created ="+nodeCreated);
            client.close();
        }
    }
}
