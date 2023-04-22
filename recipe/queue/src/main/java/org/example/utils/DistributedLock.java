package org.example.utils;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;


import java.util.List;

public class DistributedLock {
    // The ZooKeeper client instance
    private final ZooKeeper zk;
    // The path of the node where the lock is created
    private final String nodePath;
    // The name of the lock node
    private String lockNode;

    public DistributedLock(ZooKeeper zk, String path) throws InterruptedException, KeeperException {
        this.zk = zk;
        this.nodePath = Constants.lockRootNode + path;
        Helper.createNodeIfDoesNotExists(zk, Constants.lockRootNode);
        Helper.createNodeIfDoesNotExists(zk, this.nodePath);
    }

    public boolean lock() throws InterruptedException, KeeperException {
        // Create an ephemeral sequential znode under the lock znode
        lockNode = zk.create(nodePath + "/lock-", new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);


        // Get the list of all znodes under the lock znode
        List<String> children = zk.getChildren(nodePath, false);

        // Sort the list of znodes in ascending order
        children.sort(String::compareTo);

        // Get the index of the lock node in the list
        int index = children.indexOf(lockNode.substring(lockNode.lastIndexOf('/') + 1));

        // If the lock node is the first one in the list, then we have acquired the lock
        if(index == 0){
            return true;
        }else{
            zk.delete(lockNode, -1 );
            return false;
        }
    }

    public void unlock() throws InterruptedException, KeeperException {
        zk.delete(lockNode, -1);
    }
}
