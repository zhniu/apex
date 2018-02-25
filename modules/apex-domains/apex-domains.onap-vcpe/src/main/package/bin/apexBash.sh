#!/bin/bash

# Run from the Apex home directory 
if [ ! -d /home/apexuser ]
then
    echo Apex user home directory "/home/apexuser" not found
    exit
fi

# Run the command as "apexuser"
cd /home/apexuser
su apexuser