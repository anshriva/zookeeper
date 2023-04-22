package org.example;

import org.apache.zookeeper.*;
import org.example.interfaces.Queue;
import org.example.utils.Helper;

import java.util.List;

public class NoLockQueue implements Queue {
    private ZooKeeper zk;
    private String queuePath;

    public NoLockQueue(ZooKeeper zk, String queuePath) throws InterruptedException, KeeperException {
        this.zk = zk;
        this.queuePath = queuePath;
        Helper.createNodeIfDoesNotExists(zk, queuePath);
    }

    public void enqueue(String item) throws Exception {
        // Create an item node under the queue node with the item data
        byte[] data = item.getBytes();
        zk.create(queuePath + "/item", data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
    }

    public String dequeue() throws Exception {
        List<String> children = zk.getChildren(queuePath, false);
        if (children.isEmpty()) {
            // If there are no items in the queue, return null
            return null;
        }

        // Sort the list of item nodes in ascending order
        children.sort(String::compareTo);
        String nodePath = queuePath + "/" + children.get(0);
        byte[] data = zk.getData(nodePath, false, null);
        String item = new String(data);
        zk.delete(nodePath, -1);
        return item;
    }
}

