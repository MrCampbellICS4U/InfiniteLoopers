# these run files lol, not compile them

SHELL := /bin/bash

shared:
	cd src ; javac shared/*.java

client: shared
	cd src ; javac client/*.java ; java client.Client

server: shared
	cd src ; javac server/*.java ; java server.Server

.PHONY: client server shared

clean:
	rm -f src/server/*.class
	rm -f src/shared/*.class
	rm -f src/client/*.class
