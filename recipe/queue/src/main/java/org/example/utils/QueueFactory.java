package org.example.utils;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.example.NoLockQueue;
import org.example.LockQueue;
import org.example.interfaces.Queue;

public class QueueFactory {
    private ZooKeeper zk;
    private String queuePath;

    public QueueFactory(ZooKeeper zk, String queuePath) {
        this.zk = zk;
        this.queuePath = queuePath;
    }

    public Queue GetQueue(LockType lockType) throws InterruptedException, KeeperException {
        if(LockType.Lock == lockType){
            return new LockQueue(this.zk, this.queuePath);
        }
        return new NoLockQueue(this.zk, this.queuePath);
    }
}
