#!/usr/bin/env python3.6

from operator import itemgetter
import sys
import random

#selects random data from the dataset
cutoff = 56

for line in sys.stdin:
    line = line.strip()
    data, rnumber= line.split('\t', 1)
    if int(rnumber) < cutoff:
        print(data)
