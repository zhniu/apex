#! /bin/bash

event_text=`cat events/APPCvCPERestartResponseOKInitialEvent.json`

curl \
    --header "Content-type: application/json" \
    --request POST \
    --data "$event_text" \
    http://localhost:3904/events/APPC_LCM_WRITE
