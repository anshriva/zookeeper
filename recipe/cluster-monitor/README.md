# Cluster monitoring using zookeeper 

### Prerequisite: 
Launch zookeeper server on local on port 2181 <br>
Maven <br>
Java

### Local running steps: 

mvn exec:java -Dexec.mainClass=com.anubhav.ZookeeperMonitor
mvn exec:java -Dexec.mainClass=com.anubhav.ServersToBeMonitored

### screen shots
#### Plan
![title](images/title.png)

#### problem statement
![problem statement](images/Problem%20Statement.png)

#### problem with Polling 
![polling mechanism](images/polling%20mechanism.png)

#### Solution
![solution](images/Solution%20approach.png)

