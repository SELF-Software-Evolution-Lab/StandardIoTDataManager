#!/bin/sh

usage()
{
    echo "usage: scpSendFile [[-l localPath ] [-r remotePath]]"
}

##### Main

localPath=
remotePath=

if [ "$1" = "" ]; then
    echo "ERROR: not enough parameters"
	usage
	exit 1
fi

while [ "$1" != "" ]; do
    case $1 in
        -l | --local )          shift
                                localPath=$1
                                ;;
        -r | --remote )          shift
                                remotePath=$1
                                ;;
        * )                     usage
                                exit 1
    esac
    shift
done

chmod +x "$localPath"/*

scp -r -i ~/.ssh/privateKeyHadoop.ppk "$localPath" \
hadoop@xrhdfsserver1.westus2.cloudapp.azure.com:"$remotePath"
