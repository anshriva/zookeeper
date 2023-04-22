package org.example.tester;

import org.apache.zookeeper.ZooKeeper;
import org.example.interfaces.Queue;
import org.example.utils.Constants;
import org.example.utils.Helper;
import org.example.utils.LockType;
import org.example.utils.QueueFactory;

public class Consumer {
    public static void main(String[] args) throws Exception
    {
        LockType lockType =  LockType.NoLock;
        if(args.length > 0){
            String lock = args[0];
            lockType = LockType.valueOf(lock);
        }

        // Connect to the ZooKeeper instance running on localhost:2181
        ZooKeeper zk = new ZooKeeper(Constants.zookeeperUrl, 20000, null);

        Helper.createNodeIfDoesNotExists(zk, Constants.queuePath);

        if(lockType == LockType.Lock){
            Helper.createNodeIfDoesNotExists(zk, Constants.lockRootNode);
        }

        QueueFactory queueFactory = new QueueFactory(zk, Constants.queuePath);

        // Create a distributed queue
        Queue queue = queueFactory.GetQueue(lockType);

        while (true){
            String dequeValue = queue.dequeue();
            if(dequeValue != null){
                System.out.println(dequeValue);
            }
        }
    }
}
