#!/bin/bash

BINDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
FUSE_HOME=/usr/local

LD_LIBRARY_PATH="$FUSE_HOME/lib:$BINDIR:$LD_LIBRARY_PATH"
export LD_LIBRARY_PATH

CP=""
for JAR in $BINDIR/../*.jar; do
	CP="$JAR:$CP";
done
for JAR in $BINDIR/../lib/*.jar; do
	CP="$JAR:$CP";
done

MOUNT_POINT=${HOME}/Ubeeko
if test ! -d $MOUNT_POINT; then
	mkdir $MOUNT_POINT
fi

USERNAME=`whoami`

FUSEOPTS="-o debug"
HDFS_URL="hdfs://$MYSERVER:8020"
#echo $CP

echo "will mount $HDFS_URL to $MOUNT_POINT as $USERNAME ..."
java -cp "$CP" -Djava.library.path=$LD_LIBRARY_PATH fuse4j.hadoopfs.FuseHdfsClient $HDFS_URL $USERNAME $MOUNT_POINT -f $FUSEOPTS

#$BINDIR/javafs $MOUNT_POINT $HDFS_URL -f class=fuse4j/hadoopfs/FuseHdfsClient "jvm=-Djava.class.path=$CP" 
#$BINDIR/javafs $MOUNT_POINT $HDFS_URL -f class=fuse4j/hadoopfs/FuseHdfsClient "jvm=-Djava.class.path=$CP"  
