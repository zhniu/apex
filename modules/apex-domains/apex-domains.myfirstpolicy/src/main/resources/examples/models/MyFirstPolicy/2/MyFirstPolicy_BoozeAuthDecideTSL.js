
var returnValueType = Java.type("java.lang.Boolean");
var returnValue = new returnValueType(true);

executor.logger.info("Task Selection Execution: '"+executor.subject.id+
	"'. Input Event: '"+executor.inFields+"'");

branchid = executor.inFields.get("branch_ID");
taskorig = executor.subject.getTaskKey("MorningBoozeCheck");
taskalt = executor.subject.getTaskKey("MorningBoozeCheckAlt1");
taskdef = executor.subject.getDefaultTaskKey();

if(branchid >=0 && branchid <1000){
  taskorig.copyTo(executor.selectedTask);
}
else if (branchid >=1000 && branchid <2000){
  taskalt.copyTo(executor.selectedTask);
}
else{
  taskdef.copyTo(executor.selectedTask);
}

/*
This task selection logic selects task "MorningBoozeCheck" for branches with 
0<=branch_ID<1000 and selects task "MorningBoozeCheckAlt1" for branches with 
1000<=branch_ID<2000. Otherwise the default task is selected. 
In this case the default task is also "MorningBoozeCheck"
*/
