#!/bin/sh
old_dir=`pwd`
script_dir=`dirname $0`
cd $script_dir
export LD_LIBRARY_PATH=./:$LD_LIBRARY_PATH
java -Xmx256m  -Dlogback.configurationFile=logging.xml -jar ${project.artifactId}.jar $@
error_code=$?
cd $old_dir
exit $error_code
