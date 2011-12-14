#!/bin/sh
FUSE4J_HOME=${HOME}/work/ubeeko/fuse/fuse4j
LAUNCHER=${FUSE4J_HOME}/native/javafs
FS_CLASS=fuse4j/hadoopfs/FuseHdfsClient
M2_REPO=${HOME}/.m2/repository

JAVA_JVM_VERSION=1.6
FUSE4J_VER=2.4.0.0-SNAPSHOT
HADOOP_VER=0.23.1-SNAPSHOT
FUSEHDFS_VER=1.0.0-SNAPSHOT

CLASSPATH=""
CLASSPATH="$CLASSPATH:$M2_REPO/commons-logging/commons-logging/1.1.1/commons-logging-1.1.1.jar"
CLASSPATH="$CLASSPATH:$M2_REPO/log4j/log4j/1.2.13/log4j-1.2.13.jar"
CLASSPATH="$CLASSPATH:$M2_REPO/fuse4j/fuse4j-core/${FUSE4J_VER}/fuse4j-core-${FUSE4J_VER}.jar"
CLASSPATH="$CLASSPATH:$M2_REPO/fuse4j/fuse4j-hdfs/${HADOOPFS_VER}/fuse4j-hdfs-${HADOOPFS_VER}.jar"
CLASSPATH="$CLASSPATH:$M2_REPO/org/apache/hadoop/hadoop-common/${HADOOP_VER}/hadoop-common-${HADOOP_VER}.jar"
CLASSPATH="$CLASSPATH:$M2_REPO/org/apache/hadoop/hadoop-hdfs/${HADOOP_VER}/hadoop-hdfs-${HADOOP_VER}.jar"

export JAVA_JVM_VERSION
export CLASSPATH

export LD_LIBRARY_PATH=/usr/java/default/jre/lib/amd64/server

$LAUNCHER $* class=${FS_CLASS} "jvm=-Djava.class.path=$CLASSPATH"
