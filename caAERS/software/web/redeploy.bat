#!/bin/sh
# Developer shortcut script for redeploy
#mvn -o compile war:inplace catman:redeploy
ant -f ivy-build.xml undeploy-caaers -Doffline=true 
ant -f ivy-build.xml deploy-caaers -Doffline=true %1 %2
