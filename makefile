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
	rm server/*.class
	rm shared/*.class
	rm client/*.class
