import pika
import sys
import logging

logging.basicConfig()

channels = sys.argv[1:] # channels/groups that you want to listen to
if not channels:
    print "No channels to listen"
    sys.exit(1)

# Create AMQP Connection
connection = pika.BlockingConnection(pika.ConnectionParameters(
                    host='localhost'))
channel = connection.channel()
exchange = 'group_messages'
channel.exchange_declare(exchange=exchange, type='direct')

# get queue
result = channel.queue_declare(exclusive=True)
queue_name = result.method.queue

# bind queue to channels/groups
for c_id in channels:
    channel.queue_bind(exchange=exchange, queue=queue_name, routing_key=c_id)

print "[*] waiting for messages"

def callback(ch, method, properties, body):
    print "[x] %r %r" % (method.routing_key, body,)

# start consuming
channel.basic_consume(callback, queue=queue_name, no_ack=True)
channel.start_consuming()

# Usage
"""
python receive_group_messages.py channel_1 channel_3
"""