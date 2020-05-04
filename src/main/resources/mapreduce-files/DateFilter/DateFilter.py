#!/usr/bin/env python3.6
"""DateFilter.py"""

import sys

for line in sys.stdin:
    line = line.strip()
    sampleData = line.split(',')
    print(f'{line}\t{sampleData[2]}')
