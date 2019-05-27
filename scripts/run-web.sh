#! /bin/sh
# Run with sudo to bind to port 80
rm -rf /mnt/nfs/var/nfs/xrepo-bin
cp -a /mnt/nfs/var/nfs/xrepo-bin/ /home/profesor
cd /home/profesor/xrepo-bin
nohup java -jar ./xrepo-0.0.1-SNAPSHOT.war --server.port=80 >/dev/null 2>&1 &
echo "go to cd /home/profesor/xrepo-bin/logs to check app status"
