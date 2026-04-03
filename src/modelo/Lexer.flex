%%

%class Lexer
%unicode
%line
%column
%type Token

letra = [a-zA-Z_]
digito = [0-9]

%%

"if"        { return new Token(TipoToken.IF, yytext(), yyline+1); }
"else"      { return new Token(TipoToken.ELSE, yytext(), yyline+1); }
"while"     { return new Token(TipoToken.WHILE, yytext(), yyline+1); }
"for"       { return new Token(TipoToken.FOR, yytext(), yyline+1); }
"def"       { return new Token(TipoToken.DEF, yytext(), yyline+1); }
"print"     { return new Token(TipoToken.PRINT, yytext(), yyline+1); }

{letra}({letra}|{digito})* {
    return new Token(TipoToken.IDENTIFICADOR, yytext(), yyline+1);
}

{digito}+ {
    return new Token(TipoToken.NUMERO, yytext(), yyline+1);
}

\"([^\"\n]*)\" {
    return new Token(TipoToken.CADENA, yytext(), yyline+1);
}

"=" { return new Token(TipoToken.IGUAL, yytext(), yyline+1); }
">" { return new Token(TipoToken.MAYOR, yytext(), yyline+1); }
"<" { return new Token(TipoToken.MENOR, yytext(), yyline+1); }

"(" { return new Token(TipoToken.PARENTESIS_ABRE, yytext(), yyline+1); }
")" { return new Token(TipoToken.PARENTESIS_CIERRA, yytext(), yyline+1); }

":" { return new Token(TipoToken.DOS_PUNTOS, yytext(), yyline+1); }

[ \t\r\n]+   { }

. {
    return new Token(TipoToken.ERROR, yytext(), yyline+1);
}

<<EOF>> { return null; }