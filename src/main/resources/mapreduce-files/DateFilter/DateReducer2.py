#!/home/nestor/PycharmProjects/testing/venv/bin/python3

import getopt
import sys
import datetime
import dateutil.parser


def main(arguments):
    minDateTime = datetime.datetime.min
    maxDateTime = datetime.datetime.max

    try:
        args, values = getopt.getopt(arguments, "s:e:", ["sdate=", "edate="])
    except getopt.error as err:
        errorHandler(str(err))
    if(len(args) == 0):
        errorHandler(f'The argument list is not recognized: {str(arguments)}')

    for current_arg, current_value in args:
        if current_arg in ("-s", "--sdate"):
            minDateTime = dateutil.parser.isoparse(current_value)
        elif current_arg in ("-e", "--edate"):
            maxDateTime = dateutil.parser.isoparse(current_value)

    sampleDate = datetime.datetime.now()

    for line in sys.stdin:
        line = line.strip()
        data = line.split('\t', 1)

        # parse the date to enable compasissons
        try:
            sampleDate = dateutil.parser.isoparse(data[1])
        except ValueError:
            # silently fail.
            #WARNIGN this print will show up in the output
            print("Date Parsing Error: invalid format on sample")
            continue

        if maxDateTime > sampleDate > minDateTime:
            print(f'{data[0]}')

def errorHandler(message):
    print(message)
    print("ConsoleTesting.py -s <Start Date> -e <End Date>")
    exit(2)

if __name__ == "__main__":
    main(sys.argv[1:])