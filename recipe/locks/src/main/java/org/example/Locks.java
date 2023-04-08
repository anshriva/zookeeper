package org.example;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class Locks {
    static ZooKeeper zooKeeper;
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        String sessionId = UUID.randomUUID().toString();
        String rootNode = "/locks";
        Watcher watcher = new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                if(watchedEvent.getType() == Event.EventType.NodeChildrenChanged){
                    try {
                        getLockOrWait(sessionId, rootNode);
                    } catch (KeeperException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };

        zooKeeper = new ZooKeeper("localhost:2181", 20000, watcher);
        if(zooKeeper.exists(rootNode, false) ==  null){
            zooKeeper.create(rootNode, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        zooKeeper.create(rootNode+ "/"+ "lock-", sessionId.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        getLockOrWait(sessionId, rootNode);
    }

    private static void getLockOrWait(String sessionId, String rootNode) throws KeeperException, InterruptedException {
        List<String> children = zooKeeper.getChildren(rootNode, false);
        children.sort(String::compareTo);
        byte[] data = zooKeeper.getData(rootNode+ "/"+children.get(0), false, null);
        if(data!=null && new String(data).equalsIgnoreCase(sessionId)){
            System.out.println("I acquired a lock :). will leave it in 10 seconds");
            for (int i=0;i<10;i++){
                System.out.println("leaving in "+ i + "seconds");
                Thread.sleep(1000);
            }
            zooKeeper.delete(rootNode+ "/"+children.get(0), -1);
        }else{
            System.out.println("i could not acquire a lock. So will wait");
            zooKeeper.getChildren(rootNode, true);
        }

        Thread.sleep(100_100_100);
    }
}
