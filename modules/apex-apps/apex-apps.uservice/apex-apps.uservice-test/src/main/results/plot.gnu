set terminal postscript eps enhanced color font "Calibri" 20 size 8,4
set size 1.0,1.0
set datafile separator ","
set style data points
set output "results.ps"

set xlabel "Time"
set timefmt "%Y-%m-%d %H:%M:%S"
set xdata time
set ylabel "% or milliseconds"
plot 'cpustats.csv' using 1:2  with lines lt 1 lc 0 lw 2 title "CPU%" ,\
     'memstats.csv' using 1:2  with lines lt 1 lc 1 lw 2 title "Memory%", \
     'stats.csv'    using 1:13 with lines lt 1 lc 2 lw 2 title "Av. Policy Time (milliseconds)",\
     'stats.csv'    using 1:14 with lines lt 1 lc 3 lw 2 title "Transactions/millisecond"
