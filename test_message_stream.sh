#!/bin/bash

set -e
javac *.java
java -cp . TestMessageStream