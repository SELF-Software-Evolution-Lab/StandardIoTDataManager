#! /bin/sh
cd /home/profesor/development/StandardIoTDataManager

chmod 777 /home/profesor/development/StandardIoTDataManager/.gradlew

./gradlew clean -Pprod bootWar
