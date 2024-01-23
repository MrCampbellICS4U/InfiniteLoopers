SHELL := /bin/bash

client:
	cd src ; javac client/*.java
	java -cp src client.Client

server:
	cd src ; javac server/*.java
	java -cp src server.Server

.PHONY: client server jar clean

clean:
	find . -name "*.class" -type f -delete

jar:
	cd src ; javac client/*.java server/*.java
	jar cfvm test.jar Manifest.txt -C ./src .
	jar uf test.jar -C ./res .
