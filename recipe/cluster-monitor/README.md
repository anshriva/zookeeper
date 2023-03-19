# Cluster monitoring using zookeeper. 

Launch zookeeper server on local on port 2181

mvn exec:java -Dexec.mainClass=com.anubhav.ZookeeperMonitor
mvn exec:java -Dexec.mainClass=com.anubhav.ServersToBeMonitored
