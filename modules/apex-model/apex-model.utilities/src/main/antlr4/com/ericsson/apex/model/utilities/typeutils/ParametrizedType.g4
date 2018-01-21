grammar ParametrizedType;

//For more information see:
// http://stackoverflow.com/questions/39401083/class-forname-equivalent-for-creating-parameterizedtypes-from-string
// https://github.com/KetothXupack/stackoverflow-answers/tree/master/q39401083

@parser::header {
 //For more information see:
 // http://stackoverflow.com/questions/39401083/class-forname-equivalent-for-creating-parameterizedtypes-from-string
 // https://github.com/KetothXupack/stackoverflow-answers/tree/master/q39401083


 //Note: Unused Imports  
 //Since this is generated code compile warnings are to be expected and cannot always be suppressed
 //See https://github.com/antlr/antlr4/issues/1192 
 import com.ericsson.apex.model.utilities.typeutils.ClassBuilder;
}

type returns[ClassBuilder value]
    : cls=CLASS          { $value = ClassBuilder.parse($cls.text); }
    | cls=CLASS          { $value = ClassBuilder.parse($cls.text); }
      LT head=type       { $value.add($head.value); }
        (COMMA tail=type { $value.add($tail.value); })* GT
    ;

GT  : '>'
    ;

LT  : '<'
    ;

COMMA
    : ','
    ;

CLASS
    : ('a'..'z'|'A'..'Z') ('a'..'z'|'A'..'Z'|'0'..'9'|'$'|'.'|'_')*
    ;
