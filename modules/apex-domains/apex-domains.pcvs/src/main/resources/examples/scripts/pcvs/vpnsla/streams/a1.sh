#!/usr/bin/env bash

MOD_SCRIPT_NAME=`basename $0`

if [ $# -eq 0 ]; then
	echo ""
	echo "$MOD_SCRIPT_NAME - run VLC that streams video"
	echo ""
	echo "       Usage:  $MOD_SCRIPT_NAME [video file]"
	echo ""
	exit
fi

vlc-wrapper -vvv $1 --sout "#duplicate{dst=rtp{dst=10.0.0.2,port=5004,mux=ts},dst=display}" --sout-keep -q

