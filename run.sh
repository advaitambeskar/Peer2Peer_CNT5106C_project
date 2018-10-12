#!/bin/bash

javac *.java
java -cp . peerProcess 11001 &
sleep 1
java -cp . peerProcess 11002 &
sleep 1
java -cp . peerProcess 11003 &
wait