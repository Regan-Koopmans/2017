#!/bin/bash

# Removes old text file
rm listen.txt

# Starts tcpdump, using icmp packets (rather than UDP), in the background.
sudo tcpdump -n icmp &

# Runs traceroute with parameter 1, selects default interface, pipes output to
# text file.

sudo traceroute $1 -I > listen.txt

# Terminate instances of tcpdump
sudo killall tcpdump
