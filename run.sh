#!/bin/bash

set -e
javac *.java
java -cp . peerProcess 1001 | tee log/1001.stdout.txt &
sleep 1
java -cp . peerProcess 1002 | tee log/1002.stdout.txt &
sleep 1
java -cp . peerProcess 1003 | tee log/1003.stdout.txt &
sleep 1
java -cp . peerProcess 1004 | tee log/1004.stdout.txt &
sleep 1
java -cp . peerProcess 1005 | tee log/1005.stdout.txt &
sleep 1
java -cp . peerProcess 1006 | tee log/1006.stdout.txt &
wait