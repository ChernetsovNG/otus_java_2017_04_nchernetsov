#!/bin/sh
mvn clean install
java -javaagent:./target/java-2017-04-HW2-1.0-SNAPSHOT.jar -jar ./target/java-2017-04-HW2-1.0-SNAPSHOT.jar