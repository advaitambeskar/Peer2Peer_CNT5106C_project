# handshake and bitfield

Suppose that peer A tries to make a TCP connection to peer B. Here we describe thye behavior of peerA, but peer B should also follow the same procedure as peer A. After the TCP connection is established, peer A sends a handshake message to peer B. It also receives a handshake message from peer B and checks whether peer B is the right neighbor. The only thing to do is to check whether the handshake header is right and the peer ID is the expected one.

After handshaking, peer A sends a ‘bitfield’ message to let peer B know which file pieces it has. Peer B will also send its ‘bitfield’ message to peer A, unless it has no pieces.

<span style="color:red">Sending a handshake but get no bitfield is a case that we need to pay attention</span>

If peer A receives a ‘bitfield’ message from peer B and it finds out that peer B has pieces that it doesn’t have, peer A sends ‘interested’ message to peer B. Otherwise, it sends ‘not interested’ message.

# choke and unchoke

The number of concurrent connections on which a peer uploads its pieces is limited. At a moment, each peer uploads its pieces to at most k preferred neighbors and 1 optimistically unchoked neighbor. The value of k is given as a parameter when the program starts. Each peer uploads its pieces only to preferred neighbors and an optimistically unchoked neighbor. We say these neighbors are unchoked and all other neighbors are choked.

Each peer determines preferred neighbors every p seconds. Suppose that the unchoking interval is p. Then every pseconds, peer A reselects its preferred neighbors. To make the decision, peer A calculates the downloading rate from each of its neighbors, respectively, during the previous unchoking interval. Among neighbors that are interested in its data, peer A picks k neighbors that has fed its data at the highest rate. If more than two peers have the same rate, the tie should be broken randomly. Then it unchokes those preferred neighbors by sending ‘unchoke’ messages and it expects to receive ‘request’ messages from them. If a preferred neighbor is already unchoked, then peer A does not have to send ‘unchoke’ message to it. All other neighbors previously unchoked but not selected as preferred neighbors at this time should be choked unless it is an optimistically unchoked neighbor. To choke those neighbors, peer A sends ‘choke’ messages to them and stop sending pieces.

If peer A has a complete file, it determines preferred neighbors randomly among those that are interested in its data rather than comparing downloading rates.

Each peer determines an optimistically unchoked neighbor every m seconds. We say m is the optimistic unchoking interval. Every m seconds, peer A reselects an optimistically unchoked neighbor randomly among neighbors that are choked at that moment but are interested in its data. Then peer A sends ‘unchoke’ message to the selected neighbor and it expects to receive ‘request’ messages from it.

Suppose that peer C is randomly chosen as the optimistically unchoked neighbor of peer A. Because peer A is sending data to peer C, peer A may become one of peer C’s preferred neighbors, in which case peer C would start to send data to peer A. If the rate at which peer C sends data to peer A is high enough, peer C could then, in turn, become one of peer A’s preferred neighbors. Note that in this case, peer C may be a preferred neighbor and optimistically unchoked neighbor at the same time. This kind of situation is allowed. In the next optimistic unchoking interval, another peer will be selected as an optimistically unchoked neighbor.

# interested and not interested

Regardless of the connection state of choked or unchoked, if a neighbor has some interesting pieces, then a peer sends ‘interested’ message to the neighbor. Whenever a peer receives a ‘bitfield’ or ‘have’ message from a neighbor, it determines whether it should send an ‘interested’ message to the neighbor. For example, suppose that peer A makes a connection to peer B and receives a ‘bitfield’ message that shows peer B has some pieces not in peer A. Then, peer A sends an ‘interested’ message to peer B. In another example, suppose that peer A receives a ‘have’ message from peer C that contains the index of a piece not in peer A. Then peer A sends an ‘interested’ message to peer C.

Each peer maintains bitfields for all neighbors and updates them whenever it receives ‘have’ messages from its neighbors. If a neighbor does not have any interesting pieces, then the peer sends a ‘not interested’ message to the neighbor. Whenever a peer receives a piece completely, it checks the bitfields of its neighbors and decides whether it should send ‘not interested’ messages to some neighbors.

# request and piece

When a connection is unchoked by a neighbor, a peer sends a ‘request’ message for requesting a piece that it does not have and has not requested from other neighbors. Suppose that peer A receives an ‘unchoke’ message from peer B. Peer A selects a piece randomly among the pieces that peer B has, and peer A does not have, and peer A has not requested yet. Note that we use a random selection strategy, which is not the rarest first strategy usually used in BitTorrent. On receiving peer A’s ‘request’ message, peer B sends a ‘piece’ message that contains the actual piece. After completely downloading the piece, peer A sends another ‘request’ message to peer B. The exchange of request/piece messages continues until peer A is choked by peer B or peer B does not have any more interesting pieces. The next ‘request’ message should be sent after the peer receives the piece message for the previous ‘request’ message. Note that this behavior is different from the pipeliningapproachof BitTorrent. This is less efficient but simpler to implement. Note also that you don’t have to implement the ‘endgame mode’ used in BitTorrent. So we don’t have the ‘cancel’ message.

Even though peer A sends a ‘request’ message to peer B, it may not receive a ‘piece’ message corresponding to it. This situation happens when peer B re-determines preferred neighbors or optimistically unchoked a neighbor and peer A is choked as the result before peer B responds to peer A. Your program should consider this case.