java -server -Xmx4g -Dbigdata.propertyFile=/www/blazegraph/RWStore2.properties -jar /www/blazegraph/blazegraph.jar &
java -Dfile.encoding=UTF-8 -Dgrails.env=prod -jar /www/sustenagro/sustenagro-1.0.jar
