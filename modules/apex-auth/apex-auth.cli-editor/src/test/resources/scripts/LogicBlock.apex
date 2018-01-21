model create name=LogicTestPolicy

schema create name=SchemaLogicTest flavour=Exotic schema=LS
{
asdmasdm'asda'sdasd
}
LE

schema create name=SchemaLogicTest flavour=Avro schema=LS
{
  "type"      : "record",
  "name"      : "VPNActEvent",
  "namespace" : "com.ericsson.apex.domains.vpn.events",
  "fields"    : [
    {"name": "nameSpace", "type": "string"},
    {"name": "name",      "type": "string"},
    {"name": "version",   "type": "string"},
    {"name": "source",    "type": "string"},
    {"name": "target",    "type": "string"},
    {"name": "LinkMap", "type": {
        "type" : "record",
        "name" : "LinkMap",
        "namespace" : "com.ericsson.apex.domains.vpn",
        "fields" : [ ]
      }
    },
    {"name": "CustomerMap", "type": {
        "type" : "record",
        "name" : "CustomerMap",
        "namespace" : "com.ericsson.apex.domains.vpn",
        "fields" : [ ]
      }
    }
  ]
}
LE
 
# This creates logic as a block
task logic create name=LogicTestTask0 logicFlavour=MVEL logic=LS   			
{
	import java.util.Date;
	logger.debug("Running ConsistencyCheck");

	outFields.putAll(inFields);
	outFields["Attribute1PolicyValue"] = "POLICY_DEFINED";
	logger.debug("Finished Running ConsistencyCheck");

	return true;
}
LE

task create name=LogicTestTask0
 
# This creates logic as a block
task logic create name=LogicTestTask0 logicFlavour=MVEL logic=LS   			
{
	import java.util.Date;
	logger.debug("Running ConsistencyCheck");

	outFields.putAll(inFields);
	outFields["Attribute1PolicyValue"] = "POLICY_DEFINED";
	logger.debug("Finished Running ConsistencyCheck");

	return true;
}
LE
  
task create name=LogicTestTask1
 
# This creates logic on a single line
task logic create name=LogicTestTask1 logicFlavour=MVEL logic="import java.util.Date;"

task create name=LogicTestTask2
 
# This creates logic as a block
task logic create name=LogicTestTask2 logicFlavour=MVEL logic=LS
{
	system.out.println("{");
}LE

task create name=LogicTestTaskA

# This fails as it should
task logic create name=LogicTestTaskA logicFlavour=MVEL logic=

task create name=LogicTestTaskB

# This fails as it should, no end tag
task logic create name=LogicTestTaskB logicFlavour=MVEL logic=LS
{
