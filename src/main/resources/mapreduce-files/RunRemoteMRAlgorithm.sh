#!/bin/sh

usage()
{
    echo "usage: RunRemoteMRAlgorithm [[-i hdfsFileLocation ] [-m mapper] [-r reducer] [-o outputFolder] [-d dateFolder] [-t timeFolder]]"
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

ssh hadoop@xrhdfsserver1.westus2.cloudapp.azure.com -i ~/.ssh/privateKeyHadoop.ppk \
/home/hadoop/xrepo/algorithms/RunMRAlgorithm.sh --input "$inputFilename" --mapper "$mapper" --reducer "$reducer" \
--output "$outputFolder" --date "$dateFolder" --time "$timeFolder"

