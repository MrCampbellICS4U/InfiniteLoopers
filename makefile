SHELL := /bin/bash

compclient:
	cd src ; javac --release 8 client/*.java

client: compclient
	java -cp src client.Client

compserver:
	cd src ; javac --release 8 server/*.java

server: compserver
	java -cp src server.Server

.PHONY: client server clientjar serverjar clean compclient compserver

clean:
	find . -name "*.class" -type f -delete
	find . -name "*.jar" -type f -delete

clientjar: compclient
	jar cfe Client.jar client.Client -C ./src .

serverjar: compserver
	jar cfe Server.jar server.Server -C ./src .
