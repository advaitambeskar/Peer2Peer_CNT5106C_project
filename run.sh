#!/bin/bash

set -e

rm PeerInfo.cfg
ln -s PeerInfo-localhost.cfg PeerInfo.cfg

rm -rf *.log *.class
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