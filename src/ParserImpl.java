public class ParserImpl extends Parser {

        /*
     * Implements a recursive-descent parser for the following CFG:
     * 
     * T -> F AddOp T              { if ($2.type == TokenType.PLUS) { $$ = new PlusExpr($1,$3); } else { $$ = new MinusExpr($1, $3); } }
     * T -> F                      { $$ = $1; }
     * F -> Lit MulOp F            { if ($2.type == TokenType.Times) { $$ = new TimesExpr($1,$3); } else { $$ = new DivExpr($1, $3); } }
     * F -> Lit                    { $$ = $1; }
     * Lit -> NUM                  { $$ = new FloatExpr(Float.parseFloat($1.lexeme)); }
     * Lit -> LPAREN T RPAREN      { $$ = $2; }
     * AddOp -> PLUS               { $$ = $1; }
     * AddOp -> MINUS              { $$ = $1; }
     * MulOp -> TIMES              { $$ = $1; }
     * MulOp -> DIV                { $$ = $1; }
     */
    @Override
    public Expr do_parse() throws Exception {
        Expr e = parseT();
        if (tokens != null) throw new Exception("Extra tokens at the end");
        return e;
    }

    private Expr parseT() throws Exception {
        Expr f = parseF();
        if (peek(TokenType.PLUS, 0) || peek(TokenType.MINUS, 0)) {
            Token op = consume(tokens.elem.ty);
            Expr t = parseT();
            if (op.ty == TokenType.PLUS) return new PlusExpr(f, t);
            else return new MinusExpr(f, t);
        }
        return f;
    }

    private Expr parseF() throws Exception {
        Expr lit = parseLit();
        if (peek(TokenType.TIMES, 0) || peek(TokenType.DIV, 0)) {
            Token op = consume(tokens.elem.ty);
            Expr f = parseF();
            if (op.ty == TokenType.TIMES) return new TimesExpr(lit, f);
            else return new DivExpr(lit, f);
        }
        return lit;
    }

    private Expr parseLit() throws Exception {
        if (peek(TokenType.NUM, 0)) {
            Token t = consume(TokenType.NUM);
            return new FloatExpr(Float.parseFloat(t.lexeme));
        } else if (peek(TokenType.LPAREN, 0)) {
            consume(TokenType.LPAREN);
            Expr e = parseT();
            consume(TokenType.RPAREN);
            return e;
        } else {
            throw new Exception("Expected literal or parenthesis");
        }
    }
}
