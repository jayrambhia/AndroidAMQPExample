# AndroidAMQPExample
Demo android app for receiving group messages using RabbitMQ. Python RabbitMQ server/client included

## Description
This is a demo application which shows how RabbitMQ can be used for a group chat/messaging app.
This uses RabbitMQ java bindings. A connection is set up and the queue is bound to group ids. MainActivity starts a service in background which handles the connection with the server. I'm using `EventBus` to communicate between Service and Activity.

A naive Python implementation of the server is provided. As this is just a demo application, it uses some demo data. It can be extended to add users, events and other things. A python client is also available using which one can test if the server is working or not.

### How To Use:
 - Change `HOSTNAME` based on your server IP.
 - Change channels/group ids
 - setup [RabbitMQ Access](https://www.rabbitmq.com/access-control.html)
 - Create a new user with username - "android" and password "android" and add it as admin in the virtual host
 - For Mac Users - http://superuser.com/questions/464311/open-port-5672-tcp-for-access-to-rabbitmq-on-mac

### Server

    python emit_group_messages.py
    # Input style - message:channel_index_in_list
    >>> Input prompt
    This is a simple message.:1
    >>> Input prompt
    This is another simple message.:2
    
### Client

    python receive_group_messages.py <some_group> <another_group> ..

![alt tag](https://raw.githubusercontent.com/jayrambhia/AndroidAMQPExample/master/screenshots/Screenshot_2015-03-07-15-41-51.png)
