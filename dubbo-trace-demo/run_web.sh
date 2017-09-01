#!/usr/bin/env bash

export MAVEN_OPTS="-DCODI_HOME=D:\gitlab\codi-product\config\CODI_HOME"

cd demo-web
mvn clean jetty:run -DskipTest