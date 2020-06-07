#!/bin/sh

#this is intended to be placed and run on the HDFS server.

usage()
{
    echo "usage: RunMRAlgorithm [[-i hdfsFileLocation ] [-m mapper] [-r reducer] [-o outputFolder] [-d dateFolder] [-t timeFolder]]"
}

##### Main

inputFilename=
mapper=
reducer=
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
        -m | --mapper )          shift
                                mapper=$1
                                ;;
        -r | --reducer )            shift
                                reducer=$1
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

~/hadoop/bin/mapred streaming \
-mapper "$mapper" -file "$mapper" \
-reducer "$reducer" -file "$reducer" \
-input "$inputFilename" -output "$outputFolder"/"$dateFolder"/"$timeFolder"
