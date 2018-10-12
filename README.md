# Workflow to make changes

1. Create a new branch for the change
2. Make changes
3. Open a pull request
4. Wait for the rest 2 of us to review
5. Make improvements according review, until approved
6. Merge pull request and delete branch

# Design doc

Each `peerProcess` has a server thread implemented in class `Server` that accepts connections from other peers. The server thead does not implement anything about the protocol.

After accepting the connection, for each connection, there will be a `HandshakeThread` that does the handshake. Once handshake is done, a `MessageStream` will be created for that peer. `MessageStream` wraps the `Socket` that allow us extract and send whole `Message`s instead of byte arrays.

Besides creating a `MessageStream`, a `PeerThread` will also be created and started which will further handle all communications.

The `peerProcess` also has a main loop that periodically choke and unchoke peers, and check if the whole process is done.