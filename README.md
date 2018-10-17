[![Codefresh build status]( https://g.codefresh.io/api/badges/pipeline/zasdfgbnm/zasdfgbnm%2FCNT5106C-Computer-Networks-Project%2FCNT5106C-Computer-Networks-Project?branch=master&key=eyJhbGciOiJIUzI1NiJ9.NTk5ZmEwNzI2MTNhNTMwMDAxNTY4MmJm.nnVU1i-VQQSzPcsGxKnMC0wT-y9C2i8xuBZvUjlubYg&type=cf-1)]( https://g.codefresh.io/repositories/zasdfgbnm/CNT5106C-Computer-Networks-Project/builds?filter=trigger:build;branch:master;service:5bc77050a3686e05424e1c27~CNT5106C-Computer-Networks-Project)

# Workflow to make changes

1. Create a new branch for the change
2. Make changes
3. Open a pull request
4. Wait for the rest 2 of us to review
5. Make improvements according review, until approved
6. Merge pull request and delete branch

# Design doc

The whole program includes many parts, including
- config reader
- logger
- handshake manager
- messages
- TODO

## Message Stream

The interface that java provide us for network programming is to interact with the socket through `DataInputStream` and `DataOutputStream`. This is not conventient to use because we have to deal with message formatting, etc. The message streaming subsystem is composed of two classes, `Message` and `MessageStream`, where `Message` is a class for storing a single message, and `MessageStream` wraps `DataInputStream` and `DataOutputStream` to provide API for getting a the next `Message` object in a blocking manner, as well sending a `Message` object by serializing it using the format specified in the document.

## Overall framework

Each `peerProcess` has a server thread implemented in class `Server` that accepts connections from other peers. The server thead does not implement anything about the protocol.

After accepting the connection, for each connection, there will be a `HandshakeThread` that does the handshake. Once handshake is done, a `MessageStream` will be created for that peer. `MessageStream` wraps the `Socket` that allow us extract and send whole `Message`s instead of byte arrays.

Besides creating a `MessageStream`, a `PeerThread` will also be created and started which will further handle all communications.

The `peerProcess` also has a main loop that periodically choke and unchoke peers, and check if the whole process is done.