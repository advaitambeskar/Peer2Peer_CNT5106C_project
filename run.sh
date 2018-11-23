#!/bin/bash

set -e
javac *.java
java -cp . peerProcess 1 &
sleep 1
java -cp . peerProcess 2 &
sleep 1
java -cp . peerProcess 3 &
sleep 1
java -cp . peerProcess 4 &
sleep 1
java -cp . peerProcess 5 &
wait