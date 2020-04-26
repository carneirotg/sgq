#!/bin/bash

cd ../..

POMS=$(find -L . -maxdepth 2 -name "pom.xml")

for pom in $POMS
do
    mvn -f $pom sonar:sonar
done

cd -