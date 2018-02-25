#! /bin/bash

while true
do
    echo curl \
	 --header "Content-type: application/json" \
	 --request GET \
	http://159.107.219.142:3904/events/apexDMaaP/SubscriberGroup/Subscriber?timeout=60000

    curl \
	--header "Content-type: application/json" \
	--request GET \
	http://159.107.219.142:3904/events/apexDMaaP/SubscriberGroup/Subscriber?timeout=60000
done
