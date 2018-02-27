#! /bin/bash

while true
do
    echo curl \
	 --header "Content-type: application/json" \
	 --request GET \
	 http://localhost:3904/events/unauthenticated.DCAE_CL_OUTPUT/LFN/1?timeout=60000

    curl \
	--header "Content-type: application/json" \
	--request GET \
	 http://localhost:3904/events/unauthenticated.DCAE_CL_OUTPUT/LFN/1?timeout=60000
done
