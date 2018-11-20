#!/bin/bash

set -e
javac *.java
java -cp . peerProcess 1001 &
sleep 1
java -cp . peerProcess 1002 &
sleep 1
java -cp . peerProcess 1003 &
sleep 1
java -cp . peerProcess 1004 &
sleep 1
java -cp . peerProcess 1005 &
sleep 1
java -cp . peerProcess 1006 &
wait