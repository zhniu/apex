executor.logger.info(executor.subject.id);
executor.logger.info(executor.inFields);

var vnf = executor.getContextAlbum("VNFAlbum").get(executor.inFields.get("vnfName"));

var genericDataRecordType = Java.type("org.apache.avro.generic.GenericData.Record");

var healthCheckBodyRecord = executor.subject.getOutFieldSchemaHelper("body").createNewInstance();
var healthCheckBodyRecordSchema = healthCheckBodyRecord.getSchema();

var inputRecord = new genericDataRecordType(healthCheckBodyRecordSchema.getField("input").schema());
var inputRecordRecordSchema = inputRecord.getSchema();

var actionIndentifiersRecord = new genericDataRecordType(inputRecordRecordSchema.getField("action_DasH_identifiers").schema());

var commonHeaderRecord = new genericDataRecordType(inputRecordRecordSchema.getField("common_DasH_header").schema());
var commonHeaderRecordSchema = commonHeaderRecord.getSchema();

var commonHeaderFlagsRecord = new genericDataRecordType(commonHeaderRecordSchema.getField("flags").schema());

healthCheckBodyRecord.put("input", inputRecord);
inputRecord.put("action_DasH_identifiers", actionIndentifiersRecord);
inputRecord.put("common_DasH_header", commonHeaderRecord);
commonHeaderRecord.put("flags", commonHeaderFlagsRecord);

inputRecord.put("action", "ConfigModify");

var payload = new Object();
payload["vnf-host-ip-address"]     = vnf.get("ipAddress").toString();
payload["configuration-file-name"] = executor.inFields.get("configurationFileName");

var payloadString = JSON.stringify(payload);

inputRecord.put("payload", payloadString);

actionIndentifiersRecord.put("vnf_DasH_id", vnf.get("uuid"));

var requestId = 0;
if (executor.getContextAlbum("IDAlbum").containsKey("Policy2APPC")) {
	requestId = executor.getContextAlbum("IDAlbum").get("Policy2APPC");
}
executor.getContextAlbum("IDAlbum").put("Policy2APPC", requestId + 1);
		
commonHeaderRecord.put("request_DasH_id", "Policy2APPC-" + requestId);
commonHeaderRecord.put("originator_DasH_id", "AFR");
commonHeaderRecord.put("api_DasH_ver", "2.15");
commonHeaderRecord.put("sub_DasH_request_DasH_id", "AFR-subrequest");

commonHeaderRecord.put("timestamp", new Date().toISOString());

commonHeaderFlagsRecord.put("ttl", "10000");
commonHeaderFlagsRecord.put("force", "TRUE");
commonHeaderFlagsRecord.put("mode", "EXCLUSIVE");

executor.outFields.put("cambria.partition", "APPC-LCM-READ\/APPC-EVENT-LISTENER-TEST");
executor.outFields.put("rpc-name", "config-modify");
executor.outFields.put("body", healthCheckBodyRecord);

executor.logger.info(executor.outFields);

var returnValue = executor.TRUE;

