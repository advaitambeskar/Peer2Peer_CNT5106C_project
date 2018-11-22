#!/bin/bash

set -e
javac *.java
java -Xmx1024m -cp . peerProcess $1
