#!/usr/bin/env python3.6
"""DateReducer.py"""

from operator import itemgetter
import sys
import datetime

minDateTime = datetime.datetime(2020,1,20,14,13,15,1)
maxDateTime = datetime.datetime(2020,1,20,14,13,15,600000)

sample = None
date = datetime.datetime.now()
timestamp = 0.0

for line in sys.stdin:
    line = line.strip()
    data = line.split('\t',1)

    #parse the date to enable compasissons
    try:
        timestamp = float(data[1])
        date = datetime.datetime.fromtimestamp(timestamp)
    except ValueError:
        #silently fail.
        continue

    if maxDateTime > date > minDateTime:
        print(f'{data[0]}')


