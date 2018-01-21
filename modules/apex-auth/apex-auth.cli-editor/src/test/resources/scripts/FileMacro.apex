#MACROFILE:"src/test/resources/macros/MacroFile0.apexmacro"
#MACROFILE: "src/test/resources/macros/MacroFile1.apexmacro"
#MACROFILE:src/test/resources/macros/MacroFile0.apexmacro
#MACROFILE: src/test/resources/macros/MacroFile0.apexmacro  
#MACROFILE:" src/test/resources/macros/MacroFile1.apexmacro"
#MACROFILE: "src/test/resources/macros/MacroFile1.apexmacro "
#MACROFILE:                     "src/test/resources/macros/MacroFile2.apexmacro"
#MACROFILE:                     "src/test/resources/macros/MacroFile3.apexmacro"                      
#MACROFILE:                     "src/test/resources/macros/MacroFile4.apexmacro"                      # This is a comment
#MACROFILE:
#MACROFILE:                             
#MACROFILE
MACROFILE:
#MACROFILE: "src/test/resources/macros/MacroFileNonExistant.ApexMacro"
schema create #MACROFILE:"src/test/resources/macros/MacroFile5.apexmacro" #MACROFILE: "src/test/resources/macros/MacroFile6.apexmacro"

task create name=dummyTask1
task logic create name=dummyTask1 logicFlavour=JAVASCRIPT logic=LS
#MACROFILE:"src/test/resources/macros/MacroFile7.apexmacro"
LE

task create name=dummyTask2
task logic create name=dummyTask2 logicFlavour=JAVASCRIPT logic=LS
x=x+1
#MACROFILE:"src/test/resources/macros/MacroFile8.apexmacro"
x=x+1
#MACROFILE:"src/test/resources/macros/MacroFile9.apexmacro"
x=x+1
LE
