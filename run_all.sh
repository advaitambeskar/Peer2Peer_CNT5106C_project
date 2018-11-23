#!/bin/bash

set -e

rm -rf *.class && javac *.java

ssh lin114-01.cise.ufl.edu 'cd CNT5106C-Computer-Networks-Project && java -cp . peerProcess 1 && exit' &
sleep 5
ssh lin114-02.cise.ufl.edu 'cd CNT5106C-Computer-Networks-Project && java -cp . peerProcess 2 && exit' &
sleep 5
ssh lin114-03.cise.ufl.edu 'cd CNT5106C-Computer-Networks-Project && java -cp . peerProcess 3 && exit' &
sleep 5
ssh lin114-04.cise.ufl.edu 'cd CNT5106C-Computer-Networks-Project && java -cp . peerProcess 4 && exit' &
sleep 5
ssh lin114-05.cise.ufl.edu 'cd CNT5106C-Computer-Networks-Project && java -cp . peerProcess 5 && exit' &
wait
