title: Akka BoundedMailbox backpressure
class: animation-fade
layout: true

.bottom-bar[
  {{title}} <span class="style1">[github.com/ralphschaefer/testMBFlood](https://github.com/ralphschaefer/testMBFlood)</span>
]

---

class: impact

# {{title}}
## Investigate backpressure with dead letters and bounded mailboxes

---

# Test setup

The test is done within a cluster:
* one seed node
* one conversation node
* n worker nodes

in the example you can start a cluster with 2 worker nodes with `startup.sh` script

---

# Test setup

* The conversation node injects a message into the cluster. The message 
is send to the `round-robin-group` of worker nodes (named "module2")
* One single worker node consists of a spread actor, receiving messages from
the *conversation node*, and an worker actor. (named "module1")

--
* The spread actor sends the message n-time to all worker nodes of the
cluster
* The worker actor has a bounded mailbox and is responding very slow

--

In precise figures:
* worker actor mailbox size is bounded to **20**
* worker actor response time is **1/5s**
* spread actor sends message **35 times**

---

# Generating backpressure

When the mailbox of an worker actor is full a notification to 
sending actor is generated. 
--

## Use of DeadLetter
A dead letter is received by the node when mailbox size exceeds. 
Then a notification can be send to the origin of the message
```scala
  def receive: Receive = {
    case DeadLetter(message:SimpleMessage, sender, recipient) =>
      sender ! Unprocessable(message)
    case Unprocessable(item) =>
      println("not processed message :" + item)
  } 
```
```scala
case class SimpleMessage(message: String) 
case class Unprocessable(item:SimpleMessage)
```

---
# Processing backpressure

Example:
--

## React qualitative

The sending actor get a message every time a message is dropped by the receiver.

* the sender actor can reduce the amount of message generated
 
--

## React quantitativ

The sending actor collects all successfull and dropped messages
and sends it as a ratio back to the conversation actor.
 
* the conversation actor can stop conversation, when it get bad
ratios
 
---

# Overload behaviour
--

With bounded mailboxes the system recovers quite fast from an overload 
situations. 

Recovery time depends on amount of worker nodes and - of course -
maximum mailbox size.

For exact figures on how recovery time depends exactly on amount of nodes,
further investigation is required.
 
