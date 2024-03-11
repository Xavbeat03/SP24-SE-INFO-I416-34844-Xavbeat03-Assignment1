# SP24-SE-INFO-I416-34844-Xavbeat03-Assignment1

## Building and Running

To build the program clone the repo into a destination of your choosing then run ```./gradlew build``` within the project source directory. This should give you two resulting .jar files for both the client and server software.
You can then run the .jar files as you see fit. From the repo you can also use ```./gradlew runServer``` or ```./gradlew runClient``` to build and run a specific .jar file.
The clients connect over port 5056, and use either the ip localhost or the ip stated in ip.txt within the rootdirectory of the project.


## I416 Assignment 1: Memcached-lite

Note: You have an extended 2 weeks of time to do this. I expect that this is an in-depth task that will take time to complete.

## Summary

In this assignment, you will be designing and implementing a simple key-value store. The main task is to implement a server, that stores and retrieves data for different clients. For each key, there is a unique value that the server stores, using the set command, and retrieves it using a get command.

Memcached is a popular key-value store, and you will try to mimic its client protocol https://github.com/memcached/memcached/blob/master/doc/protocol.txt


A higher level explanation of the ASCII protocol can also be useful https://github.com/memcached/memcached/wiki/Commands#standard-protocol


In contrast to regular Memcached that stores data in memory, your server will use stable storage. That is, the data should persist even after the server process dies.

*Note: This is part of a semester-long series of assignments, and you will be adding more advanced functionality to the same key-value store in later assignments. It is thus important to use good abstractions and think about the general case.*

## What to Implement

You will be implementing a simpler version of memcached server, along with a client, for testing the server implementation.

### TCP-socket server

The server listens on a port, say 9889 for incoming client connections and commands, and stores all the keys and values. In this assignment, the server should store all data in a file system.

You must implement these commands:

```Bash
get <key> \r\n
set <key> <value> \r\n
```

Keys are text strings (without spaces, newlines, or other special characters). Values are text strings.

A get will fetch the data corresponding to the key and return it to the client. A set should store the value for later retrieval.

### Set

Specifically, the set command is whitespace delimited, and consists of two lines:
```Bash
set <key> <value-size-bytes> \r\n

<value> \r\n
```

Note that this is a simpler version of the memcached protocol, in which the set command also accepts flags and expiry time, which we will ignore for this assignment.

The server should respond with either ```"STORED\r\n"```, or ```"NOT-STORED\r\n"```.

### Get

Retrieving data is simpler: ```get \r\n```

The server should respond with two lines:
```Bash
VALUE <key> <bytes> \r\n

<data block>\r\n
```
After all the items have been transmitted, the server sends the string ```"END\r\n"```

### Sleep

Get/set operations can be very fast. For interesting test-cases, you should add a small random delay for all of them on the server. This is typically done by sleeping for a random time less than, say 1 second before actually reading or writing.
## Clients

You should also implement client programs, that connect to the server and make a series of get and set requests. This will be used for testing the server implementation.

Example client programs make a sequence of set/get requests to a small set of keys.

You should test with atleast 2 clients.

## Advanced : Concurrency

A concurrent server should be able to handle more than one request at a time. Each set/get request essentially spawns a new thread. This requires careful synchronization.

## Advanced : Memcached compatibility

As a bonus, you can implement the server such that regular, off-the-shelf memcached clients can connect to your server. For this, you will have to tweak the command parsing etc to support memcached protocol specification such as flags etc.

There are many Memcached clients for testing, for instance https://pymemcache.readthedocs.io/en/latest/getting_started.html


## What to Submit

You must submit your code, examples, test-cases, and a report. Please format your report as a PDF and zip all assets up before submitting.

Your code should run on a Linux server. In the future, we will deploy this to a cloud service, but it is sufficient if it runs on a local VM in this assignment. You should submit all the code in a zip file on canvas. The submission should be self contained as much as possible—if you are using a non-standard library, be sure the build system can automatically fetch and build it on a new machine.

It is strongly encouraged to use Makefiles or other build tools (if required) to compile and run the client and servers. You can use any widely available programming language (C, C++, Python, Java,…), but you must ensure that it compiles and runs.

Think carefully of how you can test the code for correctness, and provide at least one interesting test case.

The report should have describe the design details, and some experimental evaluation (what is the performance of your server? how many concurrent clients have you tested with?, etc). You should also describe the limitations of your server. What are the key and value size limits? Concurrency limits? Kind of errors that you do not handle? Future improvements etc.
## Grading scheme
| Component                        | 	Weight |
|----------------------------------|---------|
| Basic server                     | 	30%    |
| Clients and testcases            | 	30%    |
| Report                           | 	20%    |
| Advanced: Concurrency            | 	10%    |
| Advanced: Memcache compatibility | 	10%    |

## Future Assignment Tasks

These are the improvements you will make to memcached-lite (in future assignments):

* Deploy on cloud and measure the response time curves

