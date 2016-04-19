pid=`ps aux | grep sustenagro | awk '{print $2}'`
kill -9 $pid
pid=`ps aux | grep blazegraph | awk '{print $2}'`
kill -9 $pid
