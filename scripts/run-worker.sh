#! /bin/sh
cd /home/profesor/xrepo-bin
# port closed by firewall
nohup java -jar ./xrepo-0.0.1-SNAPSHOT.war --server.port=18081 --spring.profiles.active=prod,worker >/dev/null 2>&1 &
echo "go to cd /home/profesor/xrepo-bin/logs to check app status"
