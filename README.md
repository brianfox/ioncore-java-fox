# ioncore-java-fox
This is a after-hours project I persued at UCSD. 

## Competing Ocean Observatories (OOI) Capability Container implemented in Java

At OOI, I helped develop a Capability Container in Python.  It received, processed, and forwarded
messages from ocean vessels, buoys, and gliders.  We used AMQP (RabbitMQ) for the messaging system.
A capability container could include extensible governance, compression, encryption, and so forth.

I felt Python was the wrong arena for this project.  But I had to convince others
that Java is accessible to developers and that Twisted (an event driven framework for
Python) impeded progress.

I wrote this architecture to show how Java could shine in the project.

## Simplified threading model

Java had been rejected previousy because of concurreny worries.  I spent a lot of time trying to
simplify threading as much as possible.

Each message gets a thread.  This keeps threading worries in the framework.  Typical programmers 
need not worry with concurrency and simply have to make their own code re-entrant, which shouldn't
be difficult for a messaging system.  

https://github.com/brianfox/ioncore-java-fox/blob/master/src/net/ooici/ion/cc/messaging/Dispatcher.java

## Other code of interest

This is probably a good place to start.

https://github.com/brianfox/ioncore-java-fox/tree/master/src/net/ooici/ion/cc/messaging
