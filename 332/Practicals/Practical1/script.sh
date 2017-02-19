#!/bin/bash

rm listen.txt
sudo tcpdump host $1 &
traceroute $1 >> listen.txt

sudo killall tcpdump
