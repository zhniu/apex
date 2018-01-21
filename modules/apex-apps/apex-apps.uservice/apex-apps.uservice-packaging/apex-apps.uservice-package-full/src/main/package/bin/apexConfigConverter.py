##
## COPYRIGHT (C) Ericsson 2016-2018
## 
## The copyright to the computer program(s) herein is the property of
## Ericsson Inc. The programs may be used and/or copied only with written
## permission from Ericsson Inc. or in accordance with the terms and
## conditions stipulated in the agreement/contract under which the
## program(s) have been supplied.
##

import sys
import json

if (len(sys.argv) != 3):
    print "usage: apexConfigConverter.py old_config_file.json new_config_file.json"
    sys.exit(0)

with open(sys.argv[1]) as data_file:    
    config = json.load(data_file)

# Get the old carrier technology parameters
oldCCTP = config["consumerCarrierTechnologyParameters"]
oldCEPP = config["consumerEventProtocolParameters"]
oldPCTP = config["producerCarrierTechnologyParameters"]
oldPEPP = config["producerEventProtocolParameters"]

# Delete the old carrier technology parameters from the configuration
del config["consumerCarrierTechnologyParameters"]
del config["consumerEventProtocolParameters"]
del config["producerCarrierTechnologyParameters"]
del config["producerEventProtocolParameters"]

# Create event inputs and event outputs from the old parameters
aConsumer = {}
aConsumer['carrierTechnologyParameters'] = oldCCTP
aConsumer['eventProtocolParameters']     = oldCEPP

aProducer = {}
aProducer['carrierTechnologyParameters'] = oldPCTP
aProducer['eventProtocolParameters']     = oldPEPP

# Create event input parameters and event output parameters
eventInputParameters  = {}
eventOutputParameters = {}

# Add the old consumer and producer to the input and putput parameters
eventInputParameters ['aConsumer'] = aConsumer
eventOutputParameters['aProducer'] = aProducer

# Add the event input parameters and event output parameters to the configuraiton
config['eventInputParameters']  = eventInputParameters
config['eventOutputParameters'] = eventOutputParameters

# Output the new configuration
with open(sys.argv[2], 'w') as outfile:
    json.dump(config, outfile, sort_keys=True, indent=4)
