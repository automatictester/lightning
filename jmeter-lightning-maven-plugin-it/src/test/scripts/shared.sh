#!/usr/bin/env bash

if [ `whoami` = 'jenkins' ]
then
   export MVN='/Users/Shared/Jenkins/Home/tools/hudson.tasks.Maven_MavenInstallation/M3/bin/mvn'
else
   export MVN='mvn'
fi