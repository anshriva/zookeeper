package com.anubhav;

import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static org.apache.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE;

public class ZookeeperMonitor {
    private static String MembersNode = "/members";
    private static final Logger LOG = LoggerFactory.getLogger(ZookeeperMonitor.class);
    private static ZooKeeper zookeeper;

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        zookeeper = new ZooKeeper("localhost:2181", 15000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                LOG.info("*********************************************************");
                LOG.info("got the event for node = "+ watchedEvent.getPath());
                LOG.info("the event type = "+ watchedEvent.getType());
                LOG.info("*********************************************************");
                LOG.info("*********************************************************");
                try {
                    startWatch();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (KeeperException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // CREATE
        if(zookeeper.exists(MembersNode, false) == null){
            zookeeper.create(MembersNode, "data".getBytes(), OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, null);
        }

        // WATCH FOR CHILD NODES
        startWatch();

        Thread.sleep(100_000_000);
    }

    private static void startWatch() throws InterruptedException, KeeperException {
        if(zookeeper!=null){
            List<String> children  =zookeeper.getChildren(MembersNode, true, null);
            System.out.println("List of children = ");
            children.forEach(c -> System.out.print(c+" "));
            System.out.println();
        }
    }
}