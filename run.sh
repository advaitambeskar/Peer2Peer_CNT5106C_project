#!/bin/bash

for i in *.java; do
    javac $i
done

java -cp . Main