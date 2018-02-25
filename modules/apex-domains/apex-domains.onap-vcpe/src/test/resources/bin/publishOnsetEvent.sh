#! /bin/bash

event_text=`cat events/OnsetEvent.json`

curl \
    --header "Content-type: application/json" \
    --request POST \
    --data "$event_text" \
    http://159.107.219.142:3904/events/apexDMaaP
