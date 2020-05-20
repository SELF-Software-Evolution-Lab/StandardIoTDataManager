#!/bin/sh

usage()
{
    echo "usage: shellsample [[-i hdfsFileLocation ] [-s startDate] [-e endDate]]"
}

##### Main

inputFilename=
startDate=
endDate=

if [ "$1" = "" ]; then
    echo "ERROR: not enough parameters"
	usage
	exit 1
fi

while [ "$1" != "" ]; do
    case $1 in
        -i | --input )          shift
                                inputFilename=$1
                                ;;
        -s | --start )          shift
                                startDate=$1
                                ;;
        -e | --end )          shift
                                endDate=$1
                                ;;
        * )                     usage
                                exit 1
    esac
    shift
done

ssh hadoop@xrhdfsserver1.westus2.cloudapp.azure.com -i ~/.ssh/privateKeyHadoop.ppk \
/home/hadoop/xrepo/DateFilter/RunMR.sh --input "$inputFilename" --start "$startDate" --end "$endDate"
