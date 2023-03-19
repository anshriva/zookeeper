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
![title](images/Problem Statement.png)

#### problem with Polling 
![title](images/polling mechanism.png)

#### Solution
![title](images/Solution approach.png)

