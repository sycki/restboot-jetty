#!/bin/bash

cd `dirname $0`

help() {
  cat <<EOF
  Usage: $0 [build|start|stop|status]
EOF
exit 255
}

get::install::home() {
  if [ -f pom.xml ]; then
    local base=`cd ./target; pwd`
    local tarfile=`cd ./target; ls *-releases.tar.gz`
    local tardir=${tarfile%*-releases.tar.gz}
    echo $base/$tardir
  else
    echo $(cd ..; pwd)
  fi
}

cmd::build() {
  mvn clean
  mvn dependency:copy-dependencies -DoutputDirectory=./target/lib || return $?
  mvn package || return $?
  
  local tarfile=`ls ./target/*.tar.gz`
  tar -zxf $tarfile -C ./target/  || return $?
  echo "Run: $0 start"
}

cmd::start() {
  local home=`get::install::home`
  
  # set env for java app
  export RESTBOOT_HOME=$home
  export LOG4J_CONFIGURATION_FILE=${RESTBOOT_HOME}/config/log4j2.properties
  
  # launch
  java -Dfile.encoding=UTF-8 -cp "$RESTBOOT_HOME/lib/*" com.sycki.restboot.server.Server &
  sleep 3
  cmd::status
}

cmd::stop() {
  pid=`ps -ef | grep com.sycki.restboot.server.Server | grep -v grep | awk '{print $2}'`
  [ x$pid = x ] && {
    echo "restboot is not started."
    return 3
  }
  kill "$pid"
  sleep 2
  cmd::status
}

cmd::status() {
  pid=`ps -ef | grep com.sycki.restboot.server.Server | grep -v grep | awk '{print $2}'`
  [ x$pid = x ] && {
    echo "STOPPED"
    return 1
  }
  echo "RUNNING"
}


[ $# = 0 ] && help
while [ $# -gt 0 ]; do
    case $1 in
        build)
            shift
            cmd::build
            ;;
        start)
            shift
            cmd::start
            ;;
        stop)
            shift
            cmd::stop
            ;;
        restart)
            shift
            cmd::stop
            cmd::start
            ;;
        status)
            shift
            cmd::status
            ;;
        -h|--help)
            help
            ;;
        *)
            help
            ;;
    esac
    shift
done


