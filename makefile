# these run files lol, not compile them

SHELL := /bin/bash

shared:
	javac shared/*.java

client: shared
	javac client/*.java
	java client.Client

server: shared
	javac server/*.java
	java server.Server

.PHONY: client server shared

clean:
	rm -f server/*.class
	rm -f shared/*.class
	rm -f client/*.class
