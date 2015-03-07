import pika
import sys
import logging
import json
import datetime

logging.basicConfig()

# Demo Data
channels = ["channel_1", "channel_2", "channel_3", "channel_4"]

class SimpleMessage:
    def __init__(self, message, channel):
        self.message = message
        self.channel = channel
        self.timestamp = datetime.datetime.now()
    
    def getJSONDump(self):
        jsondata = {"message":self.message,
                    "channel":self.channel,
                    "timestamp":str(self.timestamp)}
        return json.dumps(jsondata)

# Create AMQP Connection
connection = pika.BlockingConnection(pika.ConnectionParameters(host='localhost'))
channel = connection.channel()
exchange = 'group_messages'
channel.exchange_declare(exchange=exchange, type='direct')

# Run a while loop and get data from command line
while True:
    try:
        data = raw_input()
        msg, channel_index = data.split(":")
        channel_id = channels[int(channel_index)]
        
        simpleMessage = SimpleMessage(msg, channel_id)
        
        # Publish to the queue
        channel.basic_publish(exchange=exchange, routing_key=channel_id, body=simpleMessage.getJSONDump())
        print "[x] sent", simpleMessage.getJSONDump()
    
    except KeyboardInterrupt:
        print "closing queue"
        connection.close()
        break

# Usage
"""
python emit_group_messages.py
# Input style - message:channel_index_in_list
>>> Input prompt
This is a simple message.:1
>>> Input prompt
This is another simple message.:2
"""