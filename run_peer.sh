#!/bin/bash

set -e
javac *.java
java -cp . peerProcess $1
