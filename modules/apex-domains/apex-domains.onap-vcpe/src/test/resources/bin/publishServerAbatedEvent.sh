#! /bin/bash

event_text=`cat events/AbatedEvent.json`

curl \
    --header "Content-type: application/json" \
    --request POST \
    --data "$event_text" \
    http://131.160.203.101:3904/events/unauthenticated.DCAE_CL_OUTPUT
