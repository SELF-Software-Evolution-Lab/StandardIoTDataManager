#!/bin/sh

chmod +x /home/hadoop/ejemplos/remoto/ejemplos/mapper.py
chmod +x /home/hadoop/ejemplos/remoto/ejemplos/reducer.py

~/hadoop/bin/mapred streaming \
-mapper /home/hadoop/ejemplos/remoto/ejemplos/mapper.py \
-reducer /home/hadoop/ejemplos/remoto/ejemplos/reducer.py \
-input /user/hadoop/input/libros/* -output /user/hadoop/"$(date +"%d-%m-%Y")"-output/job-"$(date +"%H-%M-%S")"