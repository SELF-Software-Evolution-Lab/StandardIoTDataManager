#!/bin/sh

#this is intended to be placed and run on the HDFS server.

usage()
{
    echo "usage: shellsample [[-i hdfsFileLocation ] [-s startDate] [-e endDate] [-o outputFolder] [-d dateFolder] [-t timeFolder]]"
}

##### Main

inputFilename=
startDate=
endDate=
outputFolder=
dateFolder=
timeFolder=

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
        -e | --end )            shift
                                endDate=$1
                                ;;
        -o | --output )         shift
                                outputFolder=$1
                                ;;
        -d | --date )           shift
                                dateFolder=$1
                                ;;
        -t | --time )           shift
                                timeFolder=$1
                                ;;
        * )                     usage
                                exit 1
    esac
    shift
done

echo "/home/hadoop/ejemplos/remoto/DateFilter/dateReducer.py -s $startDate -e $endDate"

~/hadoop/bin/mapred streaming \
-mapper /home/hadoop/xrepo/DateFilter/mapper.py -file /home/hadoop/xrepo/DateFilter/mapper.py \
-reducer "/home/hadoop/xrepo/DateFilter/reducer.py -s $startDate -e $endDate" -file /home/hadoop/xrepo/DateFilter/reducer.py \
-input "$inputFilename" -output /user/andes/search-result/"$outputFolder"/"$dateFolder"/"$timeFolder"
