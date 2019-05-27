#! /bin/sh
cd /home/profesor/development/StandardIoTDataManager
nohup java -jar /home/profesor/development/StandardIoTDataManager/xrepo-0.0.1-SNAPSHOT.war --server.port=80 >/dev/null 2>&1 &
