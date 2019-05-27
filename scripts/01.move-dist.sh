#! /bin/sh
rm -rf /home/profesor/xrepo-bin
mkdir /home/profesor/xrepo-bin

yes | cp -a /home/profesor/development/StandardIoTDataManager/scripts/run-web.sh /home/profesor/xrepo-bin/run-web.sh
yes | cp -a /home/profesor/development/StandardIoTDataManager/scripts/run-worker.sh /home/profesor/xrepo-bin/run-worker.sh
yes | cp /home/profesor/development/StandardIoTDataManager/build/libs/xrepo-0.0.1-SNAPSHOT.war /mnt/nfs/var/nfs/app/

