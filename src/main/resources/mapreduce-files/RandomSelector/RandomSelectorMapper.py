#!/usr/bin/env python3.6

import sys
import random

for line in sys.stdin:
    line = line.strip()
    rnumber = random.randint(0, 100)
    print(f'{line}\t{rnumber}')
