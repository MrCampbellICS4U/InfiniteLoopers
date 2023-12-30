i can forsee that a log will be helpful when we're writing reflections, and even in debugging and collecting our thoughts

## dec 26 2023
started making a basic server using https://www.baeldung.com/a-guide-to-java-sockets  
need to remember to handle case where server closes but client is still running (handled)  
got serialization working, had to debug odd thing where ObjectInputStreams block until the corresponding ObjectOutputStream is opened  

## dec 27 2023
got a packet system working  
implemented ids and basic ping  
todo:
- ~~eliminate sendToClient~~ (uneliminated after further investigation, to remove it meant that server.Client had to be exposed)
- ~~make id system nice~~
- ~~handle all the exceptions~~
- ~~clean up imports~~
- ~~write docs~~
- ~~optimize ping (93 is way too high)~~

switched the shell scripts to run to a makefile (probably overkill but it's fun! + i get a way to clean the .class files)  
improved ping with help from [this stackoverflow answer](https://stackoverflow.com/a/49058389), learned about nagle's algorithm

## dec 28 2023
got eclipse to run the project (had to change the whole file structure, it was a drag)  
had weird issues where i needed to wait for the client socket to completely open before bombarding it with messages  
not to future self: debug the weird ClassCastException that i got one time and couldn't reproduce:  
```
Exception in thread "Thread-0" java.lang.ClassCastException: cannot assign instance of shared.PositionPacket to field shared.PacketTo.type of type java.lang.String in instance of shared.PongPacket
        at java.base/java.io.ObjectStreamClass$FieldReflector.setObjFieldValues(ObjectStreamClass.java:2096)
        at java.base/java.io.ObjectStreamClass$FieldReflector.checkObjectFieldValueTypes(ObjectStreamClass.java:2060)
        at java.base/java.io.ObjectStreamClass.checkObjFieldValueTypes(ObjectStreamClass.java:1349)
        at java.base/java.io.ObjectInputStream$FieldValues.defaultCheckFieldValues(ObjectInputStream.java:2697)
        at java.base/java.io.ObjectInputStream.readSerialData(ObjectInputStream.java:2498)
        at java.base/java.io.ObjectInputStream.readOrdinaryObject(ObjectInputStream.java:2284)
        at java.base/java.io.ObjectInputStream.readObject0(ObjectInputStream.java:1762)
        at java.base/java.io.ObjectInputStream.readObject(ObjectInputStream.java:540)
        at java.base/java.io.ObjectInputStream.readObject(ObjectInputStream.java:498)
        at shared.PacketLord.run(PacketLord.java:62)
```
WHAT ARE THESE RANDOM CLIENT EXCEPTION
```
IOException while reading packet
java.io.StreamCorruptedException: invalid type code: 73
        at java.base/java.io.ObjectInputStream.readClassDesc(ObjectInputStream.java:1935)
        at java.base/java.io.ObjectInputStream.readOrdinaryObject(ObjectInputStream.java:2252)
        at java.base/java.io.ObjectInputStream.readObject0(ObjectInputStream.java:1762)
        at java.base/java.io.ObjectInputStream.readObject(ObjectInputStream.java:540)
        at java.base/java.io.ObjectInputStream.readObject(ObjectInputStream.java:498)
        at shared.PacketLord.run(PacketLord.java:62)
```
```
IOException while reading packet
java.io.OptionalDataException
        at java.base/java.io.ObjectInputStream.readObject0(ObjectInputStream.java:1777)
        at java.base/java.io.ObjectInputStream.readObject(ObjectInputStream.java:540)
        at java.base/java.io.ObjectInputStream.readObject(ObjectInputStream.java:498)
        at shared.PacketLord.run(PacketLord.java:62)
```
GOT BASIC MULTIPLAYER WORKING!!!  

https://github.com/MrCampbellICS4U/InfiniteLoopers/assets/154549832/bd0d4583-2af2-456e-8e1b-b17851f25a72

but there are a ton of random exceptions from the packets that i still need to handle  
(also as i watch that video i realize that the players are kinda floating around, independent of the grid... i should fix that)

## dec 29 2023
fixed all the random exceptions  
the problem was that with multiple threads, one packet would try to be sent while another was still being sent  
this would mix the packets, making the connection at the other side just die  
my solution is to use a queue to handle sending packets  
i learned about volatile from https://stackoverflow.com/a/30060021 after having a fun bug where adding a print statement would make it work  
one problem now is that i'm opening 2 threads per connection, and it's starting to lag (you can see it pull on the fps)  
