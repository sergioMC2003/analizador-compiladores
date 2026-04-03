package modelo;

import java.util.List;

public class Parser {

    private final List<Token> tokens;
    private int actual = 0;

    private NodoAST raiz;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public boolean analizar() {

        raiz = new NodoAST("PROGRAMA");

        while (!esFin()) {
            raiz.agregarHijo(sentencia());
        }

        return true;
    }

    public NodoAST getAST(){
        return raiz;
    }

    // ---------------- SENTENCIAS ----------------

    private NodoAST sentencia() {

        if (coincidir(TipoToken.PRINT)) {
            return printStmt();
        }

        if (coincidir(TipoToken.IF)) {
            return ifStmt();
        }

        if (verificar(TipoToken.IDENTIFICADOR)) {
            return asignacion();
        }

        error("Sentencia no válida");
        return null;
    }

    private NodoAST asignacion() {

        Token id = consumir(TipoToken.IDENTIFICADOR, "Se esperaba un identificador");
        consumir(TipoToken.IGUAL, "Se esperaba '='");

        NodoAST nodo = new NodoAST("ASIGNACION");
        nodo.agregarHijo(new NodoAST(id.getLexema()));
        nodo.agregarHijo(expresion());

        return nodo;
    }

    private NodoAST printStmt() {

        consumir(TipoToken.PARENTESIS_ABRE, "Se esperaba '('");

        NodoAST nodo = new NodoAST("PRINT");
        nodo.agregarHijo(expresion());

        consumir(TipoToken.PARENTESIS_CIERRA, "Se esperaba ')'");

        return nodo;
    }

    private NodoAST ifStmt() {

        NodoAST nodo = new NodoAST("IF");

        nodo.agregarHijo(expresion());

        consumir(TipoToken.DOS_PUNTOS, "Se esperaba ':'");

        nodo.agregarHijo(sentencia());

        return nodo;
    }

    // ---------------- EXPRESIONES ----------------

    private NodoAST expresion() {

        NodoAST nodo = termino();

        while (coincidir(TipoToken.MAYOR) || coincidir(TipoToken.MENOR)) {

            Token operador = tokens.get(actual - 1);

            NodoAST op = new NodoAST(operador.getLexema());
            op.agregarHijo(nodo);
            op.agregarHijo(termino());

            nodo = op;
        }

        return nodo;
    }

    private NodoAST termino() {

        if (coincidir(TipoToken.NUMERO)) {
            return new NodoAST(tokens.get(actual - 1).getLexema());
        }

        if (coincidir(TipoToken.CADENA)) {
            return new NodoAST(tokens.get(actual - 1).getLexema());
        }

        if (coincidir(TipoToken.IDENTIFICADOR)) {
            return new NodoAST(tokens.get(actual - 1).getLexema());
        }

        error("Expresión inválida");
        return null;
    }

    // ---------------- FUNCIONES AUXILIARES ----------------

    private boolean coincidir(TipoToken tipo) {

        if (verificar(tipo)) {
            avanzar();
            return true;
        }

        return false;
    }

    private boolean verificar(TipoToken tipo) {

        if (esFin()) return false;

        return tokens.get(actual).getTipo() == tipo;
    }

    private Token avanzar() {

        if (!esFin()) actual++;

        return tokens.get(actual - 1);
    }

    private boolean esFin() {

        return actual >= tokens.size();
    }

    private Token consumir(TipoToken tipo, String mensaje) {

        if (verificar(tipo)) return avanzar();

        error(mensaje);
        return null;
    }

    private void error(String mensaje) {

        if (actual >= tokens.size()) {
            throw new RuntimeException(
                "Error sintáctico: fin inesperado del código"
            );
        }

        Token token = tokens.get(actual);

        throw new RuntimeException(
            "Error sintáctico en línea " +
            token.getLinea() +
            ": " + mensaje +
            " cerca de '" +
            token.getLexema() + "'"
        );
    }
}