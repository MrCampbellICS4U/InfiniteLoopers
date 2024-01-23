# these run files lol, not compile them

SHELL := /bin/bash

client:
	cd src ; javac client/*.java
	java -cp src client.Client

server:
	cd src ; javac server/*.java
	java -cp src server.Server

.PHONY: client server jar

clean:
	find . -name "*.class" -type f -delete

jar:
	jar cfvm test.jar Manifest.txt res/ -C ./src .
