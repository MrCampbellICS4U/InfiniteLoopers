# InfiniteLoopers
Ethan, Stoyan and Brian's Team
![image](https://github.com/MrCampbellICS4U/InfiniteLoopers/assets/101023105/7834bc98-da9a-45f0-9097-8cef6883c3f7)
Mockup:
![image](https://github.com/MrCampbellICS4U/InfiniteLoopers/assets/154549832/ab00224d-01c5-4b70-a005-edeff0b7ad69)
[Planning doc](https://docs.google.com/document/d/1hbj9Qk5E5bjvUshHamrvtHdDFZhmCvz4Bq9szGMPqUg/edit)

## Instructions

### running the game
1. start server
2. start client
3. ??
4. profit

if you're on the command line, `make server` and `make client` will compile and start the server and the client, respectively

### creating new packet types
to create a new packet type, make a new java class in [`shared/`](src/shared/) called [Type]Packet.java  
if the packet is going to the client, import `client.Client` and have it extend `PacketTo<Client>`  
if the packet is going to the server, import `server.Server` and have it extend `PacketTo<Server>`  
add all the data you want as fields, and an optional constructor to set that data  
the `handle` method is what gets called when this packet is received. if your packet is going to the server, it should take a `Server` as its parameter; if it's going to the client, it should take a `Client` as its parameter.  
see [`shared/`](src/shared/) for examples  

### sending packets
to send a packet from the server to a client, you can use the `sendToClient` method, which takes the id of the client to send to and a packet to send.  
to send a packet from a client to the server, use the `send` method of the `PacketLord` `pl`, which takes a packet to send.  

