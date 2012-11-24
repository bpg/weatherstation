#!/bin/sh
old_dir=`pwd`
script_dir=`dirname $0`
cd $script_dir
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/lib/jni
java -Xmx256m -Djna.nosys=true -Dlogback.configurationFile=logging-dbg.xml -Dsnapshot.file=$script_dir/snapshot -Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,address=8998,server=y,suspend=n -Djava.compiler=NONE -jar  weatherstation.jar $@
error_code=$?
cd $old_dir
exit $error_code
