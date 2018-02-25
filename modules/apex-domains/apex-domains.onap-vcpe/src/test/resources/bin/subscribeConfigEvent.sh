#! /bin/bash

while true
do
    echo curl \
	 --header "Content-type: application/json" \
	 --request GET \
	 http://131.160.203.101:3904/events/APPC-LCM-READ/SubscriberGroup/Subscriber?timeout=60000

    curl \
	--header "Content-type: application/json" \
	--request GET \
	http://131.160.203.101:3904/events/APPC-LCM-READ/SubscriberGroup/Subscriber?timeout=60000
done
