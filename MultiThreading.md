As far as I'm concerned, the hardest part of this project is multithreading. Since we need to concurrently communicating with each peer, and we also need a centralized control to choke and unchoke peers.

Here in this implementation, I'm suggesting the following threading model:

# How many threads? What does these threads do?

Suppose that I am one of the peer and there are `m` neighbors connected to me, then I will open `m+1` threads in total, where `m` of them are called "peer thread" and are responsible for handling communication with each connected peer and `1` of them would be for management of myself, and let's call it "management thread".

## peer thread behavior

For each peer thread, it has a never ending main loop getting the next message and take action according to message type.

The following variables will be used:

| Variable name          | Type         | Description                                 |
|------------------------|--------------|---------------------------------------------|
| peerid                 | thread local | the ID of peer that this thread is managing |
| choked_by[peerid]      | global       | whether I am choked by peerid               |
| interested_by[peerid]  | global       | whether I am interested by peerid           |
| peer_bitfields[peerid] | global       | bitfield of peerid                          |
| peers_unchoked         | global       | list of peers unchoked by me                |
| my_bitfields           | global       | my own bitfield                             |

`peerid` is thread local and all other variables are global.

actions are listed on the below table:

| Message type         | Action                                                                                                 |
|----------------------|--------------------------------------------------------------------------------------------------------|
| choke                | choked_by[peerid] = True                                                                               |
| unchoke              | choked_by[peerid] = False                                                                              |
| interested           | interested_by[peerid] = True                                                                           |
| not interested       | interested_by[peerid] = True                                                                           |
| have(index)          | peer_bitfields[peerid][index] = True; send interested or not interested based on the new bitfield; |
| bitfield(buffer)     | fill peer_bitfields[peerid] with buffer                                                                |
| request(index)       | if peerid in peers_unchoked:      send piece                                                           |
| piece(index, buffer) | store buffer to file; send have(index) to all peers; update my_bitfields;                              |

## management thread behavior

The behavior is described below:

- every `UnchokingInterval`, compute k preferred peers, and update `peers_unchoked`.
- every `OptimisticUnchokingInterval`, compute the optimically selected peer, and update `peers_unchoked`.
- every `x`, check if all peers are done, if so, terminate all peer threads.

# Critical sections, synchronization

| Variable name          | Synchronization                                                                          |
|------------------------|------------------------------------------------------------------------------------------|
| peerid                 | This is thread local, no synchronization required                                        |
| choked_by[peerid]      | Each thread only access to its own corresponding element, no synchronization needed.     |
| interested_by[peerid]  | Each thread only access to its own corresponding element, no synchronization needed.     |
| peer_bitfields[peerid] | Accessed by one peer thread and the management thread, so synchronization is required.   |
| peers_unchoked         | Accessed by one peer thread and the management thread, so synchronization is required.   |
| my_bitfields           | Accessed by many peer threads and the management thread, so synchronization is required. |