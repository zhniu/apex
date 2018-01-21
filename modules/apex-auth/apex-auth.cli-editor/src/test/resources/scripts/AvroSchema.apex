model create name=AvroSchemaPolicy


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

event create name=Test_InputEvent nameSpace=com.ericsson.apex.test source=External target=Apex

event parameter create name=Test_InputEvent parName=AAA   schemaName=SchemaLogicTest optional=false
event parameter create name=Test_InputEvent parName=BBB   schemaName=SchemaLogicTest optional=true
event parameter create name=Test_InputEvent parName=CCC   schemaName=SchemaLogicTest
