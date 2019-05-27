#! /bin/sh
yes | cp -a /mnt/nfs/var/nfs/app/. /home/profesor/development/StandardIoTDataManager/
cd /home/profesor/development/StandardIoTDataManager
nohup java -jar /home/profesor/development/StandardIoTDataManager/xrepo-0.0.1-SNAPSHOT.war --server.port=80 >/dev/null 2>&1 &
echo "go to cd /home/profesor/development/StandardIoTDataManager/logs to check app status"
