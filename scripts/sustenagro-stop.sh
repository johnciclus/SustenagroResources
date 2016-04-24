pid=`ps aux | grep sustenagro-1.0 | awk '{print $2}'`
kill -9 $pid
pid=`ps aux | grep blazegraph | awk '{print $2}'`
kill -9 $pid
