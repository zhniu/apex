#! /bin/bash

##
## COPYRIGHT (C) Ericsson 2016-2018
## 
## The copyright to the computer program(s) herein is the property of
## Ericsson Inc. The programs may be used and/or copied only with written
## permission from Ericsson Inc. or in accordance with the terms and
## conditions stipulated in the agreement/contract under which the
## program(s) have been supplied.
##

sed 's/  */,/g' cpu.log | cut -d',' -f1,2,4 | sed 's/,/ /' > cpustats.csv
sed 's/  */,/g' mem.log | cut -d',' -f1,2,5 | sed 's/,/ /' > memstats.csv
grep ,myserver test_1.log | cut -d',' -f1,3,5,7 > 1.csv
grep ,myserver test_2.log | cut -d',' -f1,3,5,7 > 2.csv
grep ,myserver test_3.log | cut -d',' -f1,3,5,7 > 3.csv

paste -d ',' 1.csv 2.csv 3.csv | awk -F',' '{av=($3+$7+$11)/3;tot=$4+$8+$12;printf("%s,%s,%s\n", $0, av, tot)}' > stats.csv