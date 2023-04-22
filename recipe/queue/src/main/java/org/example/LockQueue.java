package org.example;

import org.apache.zookeeper.*;
import org.example.interfaces.Queue;
import org.example.utils.DistributedLock;
import org.example.utils.Helper;

import java.util.List;

public class LockQueue implements Queue {
    private ZooKeeper zk;
    private String queuePath;

    public LockQueue(ZooKeeper zk, String queuePath) throws InterruptedException, KeeperException {
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
        while (true) {
            // Get the list of all item nodes under the queue node
            List<String> children = zk.getChildren(queuePath, false);
            if (children.isEmpty()) {
                return null;
            }

            // Sort the list of item nodes in ascending order
            children.sort(String::compareTo);

            // Process each item node in the list
            for (String node : children) {
                boolean lockAcquired = false;
                DistributedLock lock = null;
                try {
                    String nodePath = queuePath + "/" + node;
                    // Acquire a lock on the queue node using a distributed lock
                    lock = new DistributedLock(zk, queuePath);
                    lockAcquired = lock.lock();

                    // Check if the node still exists before processing it
                    if (lockAcquired && zk.exists(nodePath, false) != null) {

                        // Get the item data from the item node
                        byte[] data = zk.getData(nodePath, false, null);
                        String item = new String(data);

                        // Delete the item node to dequeue the item
                        zk.delete(nodePath, -1);
                        return item;
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                } finally {
                    if (lockAcquired && lock != null) {
                        lock.unlock();
                    }
                }
            }
        }
    }
}
