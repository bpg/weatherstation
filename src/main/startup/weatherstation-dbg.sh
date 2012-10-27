#!/bin/sh
old_dir=`pwd`
script_dir=`dirname $0`
cd $script_dir
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/lib/jni
java -Xmx256m  -Dlogback.configurationFile=logging-dbg.xml -Dsnapshot.file=$script_dir/snapshot -jar  ${project.artifactId}.jar $@
error_code=$?
cd $old_dir
exit $error_code
