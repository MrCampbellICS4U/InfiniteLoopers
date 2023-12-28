i can forsee that a log will be helpful when we're writing reflections, and even in debugging and collecting our thoughts

## dec 26 2023
started making a basic server using https://www.baeldung.com/a-guide-to-java-sockets  
need to remember to handle case where server closes but client is still running  
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