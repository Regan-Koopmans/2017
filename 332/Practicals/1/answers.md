# COS 332
## Practical 1 Questions

### Question 1

This is because all domains on the UP network are
registered under the `137.215.XXX.XXX` address prefix. This means that the
address 8.8.8.8 does not exist in the scope of this subnet and can hence not
be traced to.

### Question 2

No, you cannot trace the route to `up.ac.za` from campus. This is because this
domain is not hosted locally on the university network. This is demonstrated by
the fact that when you try to `traceroute` to the domain we see one final SYN
packet to a domain outside the network, which  is never acknowledged
(i.e. no ACK is sent in return). This means that the packet has been
refused by the university firewall and access has been denied to this outside
host.

### Question 3

I beleive that UP was possibly being hosted using Amazon Web Services (AWS) in
some form. Dyn offers DNS and internet traffic management services. This may
have rather been an attack on Amazon Elastic Compute Cloud rather than `dyn.com`
and `up.ac.za` separately. An attack on the Johannesburg Cloud Technology Hub
and the The Amazon Cape Town Development Centre would have both been good
candidates for such an attack.
