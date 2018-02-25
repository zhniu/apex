model create name=ONAPTLGoalPolicyModel

schema create name=SimpleStringType  flavour=Java schema=java.lang.String
schema create name=SimpleLongType    flavour=Java schema=java.lang.Long
schema create name=SimpleBooleanType flavour=Java schema=java.lang.Boolean

schema create name=AAIType flavour=Avro schema=LS
#MACROFILE:"src/main/resources/schemas/AAIType.avsc"
LE

schema create name=HealthCheckBodyType flavour=Avro schema=LS
#MACROFILE:"src/main/resources/schemas/HealthCheckBodyType.avsc"
LE

schema create name=VNFType flavour=Avro schema=LS
#MACROFILE:"src/main/resources/schemas/VNFType.avsc"
LE

album create name=VNFAlbum scope=policy writable=true schemaName=VNFType
album create name=IDAlbum scope=policy writable=true schemaName=SimpleLongType

event create name=VNFInitEvent nameSpace=com.ericsson.apex.onap.tlgoal2017 source=DCAE target=apex
event parameter create name=VNFInitEvent parName=vnf schemaName=VNFType

event create name=VNFInitResultEvent nameSpace=com.ericsson.apex.onap.tlgoal2017 source=DCAE target=apex
event parameter create name=VNFInitResultEvent parName=vnf     schemaName=VNFType
event parameter create name=VNFInitResultEvent parName=message schemaName=SimpleStringType

event create name=DCAE.Config_tca-hi-lo version=v0.0.1 nameSpace=com.ericsson.apex.onap.tlgoal2017 source=DCAE target=apex

event parameter create name=DCAE.Config_tca-hi-lo parName=closedLoopEventClient schemaName=SimpleStringType
event parameter create name=DCAE.Config_tca-hi-lo parName=policyVersion         schemaName=SimpleStringType
event parameter create name=DCAE.Config_tca-hi-lo parName=policyName            schemaName=SimpleStringType
event parameter create name=DCAE.Config_tca-hi-lo parName=policyScope           schemaName=SimpleStringType
event parameter create name=DCAE.Config_tca-hi-lo parName=target_type           schemaName=SimpleStringType
event parameter create name=DCAE.Config_tca-hi-lo parName=AAI                   schemaName=AAIType
event parameter create name=DCAE.Config_tca-hi-lo parName=closedLoopAlarmStart  schemaName=SimpleLongType
event parameter create name=DCAE.Config_tca-hi-lo parName=closedLoopEventStatus schemaName=SimpleStringType
event parameter create name=DCAE.Config_tca-hi-lo parName=closedLoopControlName schemaName=SimpleStringType
event parameter create name=DCAE.Config_tca-hi-lo parName=version               schemaName=SimpleStringType
event parameter create name=DCAE.Config_tca-hi-lo parName=target                schemaName=SimpleStringType
event parameter create name=DCAE.Config_tca-hi-lo parName=closedLoopAlarmEnd    schemaName=SimpleLongType   optional=true
event parameter create name=DCAE.Config_tca-hi-lo parName=requestID             schemaName=SimpleStringType
event parameter create name=DCAE.Config_tca-hi-lo parName=from                  schemaName=SimpleStringType

event create name=EventReceivedMatch2EstablishEvent nameSpace=com.ericsson.apex.onap.tlgoal2017 source=apex target=apex

event parameter create name=EventReceivedMatch2EstablishEvent parName=genericVNFName schemaName=SimpleStringType
event parameter create name=EventReceivedMatch2EstablishEvent parName=alarmStartTime schemaName=SimpleLongType
event parameter create name=EventReceivedMatch2EstablishEvent parName=alarmEndTime   schemaName=SimpleLongType
event parameter create name=EventReceivedMatch2EstablishEvent parName=status         schemaName=SimpleStringType

event create name=VNFEstablish2DecideEvent nameSpace=com.ericsson.apex.onap.tlgoal2017 source=apex target=apex

event parameter create name=VNFEstablish2DecideEvent parName=vnfName   schemaName=SimpleStringType
event parameter create name=VNFEstablish2DecideEvent parName=onsetFlag schemaName=SimpleBooleanType

event create name=ConfigModifyDecide2ActEvent nameSpace=com.ericsson.apex.onap.tlgoal2017 source=apex target=apex

event parameter create name=ConfigModifyDecide2ActEvent parName=vnfName               schemaName=SimpleStringType
event parameter create name=ConfigModifyDecide2ActEvent parName=configurationFileName schemaName=SimpleStringType

event create name=ConfigModifyOutputEvent nameSpace=com.ericsson.apex.onap.tlgoal2017 source=apex target=external

event parameter create name=ConfigModifyOutputEvent parName=cambria.partition schemaName=SimpleStringType
event parameter create name=ConfigModifyOutputEvent parName=rpc-name         schemaName=SimpleStringType
event parameter create name=ConfigModifyOutputEvent parName=body                  schemaName=HealthCheckBodyType

task create name=VNFInitTask
task inputfield  create name=VNFInitTask fieldName=vnf schemaName=VNFType

task outputfield create name=VNFInitTask fieldName=vnf schemaName=VNFType
task outputfield create name=VNFInitTask fieldName=message schemaName=SimpleStringType

task contextref create name=VNFInitTask albumName=VNFAlbum

task logic create name=VNFInitTask logicFlavour=JAVASCRIPT logic=LS
#MACROFILE:"src/main/resources/logic/VNFInitTask.js"
LE

task create name=InputMatchTask

task inputfield create name=InputMatchTask fieldName=closedLoopEventClient schemaName=SimpleStringType
task inputfield create name=InputMatchTask fieldName=policyVersion         schemaName=SimpleStringType
task inputfield create name=InputMatchTask fieldName=policyName            schemaName=SimpleStringType
task inputfield create name=InputMatchTask fieldName=policyScope           schemaName=SimpleStringType
task inputfield create name=InputMatchTask fieldName=target_type           schemaName=SimpleStringType
task inputfield create name=InputMatchTask fieldName=AAI                   schemaName=AAIType
task inputfield create name=InputMatchTask fieldName=closedLoopAlarmStart  schemaName=SimpleLongType
task inputfield create name=InputMatchTask fieldName=closedLoopEventStatus schemaName=SimpleStringType
task inputfield create name=InputMatchTask fieldName=closedLoopControlName schemaName=SimpleStringType
task inputfield create name=InputMatchTask fieldName=version               schemaName=SimpleStringType
task inputfield create name=InputMatchTask fieldName=target                schemaName=SimpleStringType
task inputfield create name=DCAE.Config_tca-hi-lo fieldName=closedLoopAlarmEnd    schemaName=SimpleLongType
task inputfield create name=InputMatchTask fieldName=requestID             schemaName=SimpleStringType
task inputfield create name=InputMatchTask fieldName=from                  schemaName=SimpleStringType

task outputfield create name=InputMatchTask fieldName=genericVNFName schemaName=SimpleStringType
task outputfield create name=InputMatchTask fieldName=alarmStartTime schemaName=SimpleLongType
task outputfield create name=InputMatchTask fieldName=alarmEndTime   schemaName=SimpleLongType
task outputfield create name=InputMatchTask fieldName=status         schemaName=SimpleStringType

task logic create name=InputMatchTask logicFlavour=JAVASCRIPT logic=LS
#MACROFILE:"src/main/resources/logic/InputMatchTask.js"
LE

task create name=VNFEstablishTask

task inputfield create name=VNFEstablishTask fieldName=genericVNFName schemaName=SimpleStringType
task inputfield create name=VNFEstablishTask fieldName=alarmStartTime schemaName=SimpleLongType
task inputfield create name=VNFEstablishTask fieldName=alarmEndTime   schemaName=SimpleLongType
task inputfield create name=VNFEstablishTask fieldName=status         schemaName=SimpleStringType

task outputfield create name=VNFEstablishTask fieldName=vnfName   schemaName=SimpleStringType
task outputfield create name=VNFEstablishTask fieldName=onsetFlag schemaName=SimpleBooleanType

task contextref create name=VNFEstablishTask albumName=VNFAlbum

task logic create name=VNFEstablishTask logicFlavour=JAVASCRIPT logic=LS
#MACROFILE:"src/main/resources/logic/VNFEstablishTask.js"
LE

task create name=VNFOnsetDecideTask

task inputfield create name=VNFOnsetDecideTask fieldName=vnfName schemaName=SimpleStringType

task outputfield create name=VNFOnsetDecideTask fieldName=vnfName               schemaName=SimpleStringType
task outputfield create name=VNFOnsetDecideTask fieldName=configurationFileName schemaName=SimpleStringType

task contextref create name=VNFOnsetDecideTask albumName=VNFAlbum

task logic create name=VNFOnsetDecideTask logicFlavour=JAVASCRIPT logic=LS
#MACROFILE:"src/main/resources/logic/VNFOnsetDecideTask.js"
LE

task create name=VNFAbatedDecideTask

task inputfield create name=VNFAbatedDecideTask fieldName=vnfName schemaName=SimpleStringType

task outputfield create name=VNFAbatedDecideTask fieldName=vnfName               schemaName=SimpleStringType
task outputfield create name=VNFAbatedDecideTask fieldName=configurationFileName schemaName=SimpleStringType

task contextref create name=VNFAbatedDecideTask albumName=VNFAlbum

task logic create name=VNFAbatedDecideTask logicFlavour=JAVASCRIPT logic=LS
#MACROFILE:"src/main/resources/logic/VNFAbatedDecideTask.js"
LE

task create name=ConfigModifyActTask

task inputfield create name=ConfigModifyActTask fieldName=vnfName               schemaName=SimpleStringType
task inputfield create name=ConfigModifyActTask fieldName=configurationFileName schemaName=SimpleStringType

task outputfield create name=ConfigModifyActTask fieldName=cambria.partition schemaName=SimpleStringType
task outputfield create name=ConfigModifyActTask fieldName=rpc-name          schemaName=SimpleStringType
task outputfield create name=ConfigModifyActTask fieldName=body              schemaName=HealthCheckBodyType

task contextref create name=ConfigModifyActTask albumName=IDAlbum
task contextref create name=ConfigModifyActTask albumName=VNFAlbum

task logic create name=ConfigModifyActTask logicFlavour=JAVASCRIPT logic=LS
#MACROFILE:"src/main/resources/logic/ConfigModifyActTask.js"
LE

policy create name=VNFInitPolicy template=freestyle firstState=VNFInitState

policy state create name=VNFInitPolicy stateName=VNFInitState triggerName=VNFInitEvent defaultTaskName=VNFInitTask
policy state output create name=VNFInitPolicy stateName=VNFInitState outputName=VNFInitOutput eventName=VNFInitResultEvent
policy state taskref create name=VNFInitPolicy stateName=VNFInitState taskName=VNFInitTask outputType=DIRECT outputName=VNFInitOutput

policy create name=ONAPTLGoalPolicy template=MEDA firstState=InputMatch

policy state create name=ONAPTLGoalPolicy stateName=ConfigModifyAct triggerName=ConfigModifyDecide2ActEvent defaultTaskName=ConfigModifyActTask
policy state output create name=ONAPTLGoalPolicy stateName=ConfigModifyAct outputName=ConfigModifyActFinalOutput eventName=ConfigModifyOutputEvent
policy state taskref create name=ONAPTLGoalPolicy stateName=ConfigModifyAct taskName=ConfigModifyActTask outputType=DIRECT outputName=ConfigModifyActFinalOutput

policy state create name=ONAPTLGoalPolicy stateName=VNFDecide triggerName=VNFEstablish2DecideEvent defaultTaskName=VNFOnsetDecideTask
policy state output create name=ONAPTLGoalPolicy stateName=VNFDecide outputName=ConfigModifyActOutput eventName=ConfigModifyDecide2ActEvent nextState=ConfigModifyAct
policy state taskref create name=ONAPTLGoalPolicy stateName=VNFDecide taskName=VNFOnsetDecideTask outputType=DIRECT outputName=ConfigModifyActOutput
policy state taskref create name=ONAPTLGoalPolicy stateName=VNFDecide taskName=VNFAbatedDecideTask outputType=DIRECT outputName=ConfigModifyActOutput

policy state selecttasklogic create name=ONAPTLGoalPolicy stateName=VNFDecide logicFlavour=JAVASCRIPT logic=LS
#MACROFILE:"src/main/resources/logic/VNFDecideTSL.js"
LE

policy state create name=ONAPTLGoalPolicy stateName=VNFEstablish triggerName=EventReceivedMatch2EstablishEvent defaultTaskName=VNFEstablishTask
policy state output create name=ONAPTLGoalPolicy stateName=VNFEstablish outputName=VNFFoundOutput eventName=VNFEstablish2DecideEvent nextState=VNFDecide
policy state taskref create name=ONAPTLGoalPolicy stateName=VNFEstablish taskName=VNFEstablishTask outputType=DIRECT outputName=VNFFoundOutput

policy state create name=ONAPTLGoalPolicy stateName=InputMatch triggerName=DCAE.Config_tca-hi-lo defaultTaskName=InputMatchTask
policy state output create name=ONAPTLGoalPolicy stateName=InputMatch outputName=InputMatched eventName=EventReceivedMatch2EstablishEvent nextState=VNFEstablish
policy state taskref create name=ONAPTLGoalPolicy stateName=InputMatch taskName=InputMatchTask outputType=DIRECT outputName=InputMatched

validate

