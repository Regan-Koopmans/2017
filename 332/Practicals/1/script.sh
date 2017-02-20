#!/bin/bash

rm listen.txt
sudo tcpdump -n icmp &
sudo traceroute $1 -I > listen.txt
sudo killall tcpdump
