#! /bin/sh
rm -rf /mnt/nfs/var/nfs/app
mkdir /mnt/nfs/var/nfs/app
mkdir /mnt/nfs/var/nfs/app/scripts
yes | cp -a /home/profesor/development/StandardIoTDataManager/scripts/. /mnt/nfs/var/nfs/app/scripts/
yes | cp /home/profesor/development/StandardIoTDataManager/build/libs/xrepo-0.0.1-SNAPSHOT.war /mnt/nfs/var/nfs/app/

