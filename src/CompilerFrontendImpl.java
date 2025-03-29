public class CompilerFrontendImpl extends CompilerFrontend {
    public CompilerFrontendImpl() {
        super();
    }

    public CompilerFrontendImpl(boolean debug_) {
        super(debug_);
    }

    /*
     * Initializes the local field "lex" to be equal to the desired lexer.
     * The desired lexer has the following specification:
     * 
     * NUM: [0-9]*\.[0-9]+
     * PLUS: \+
     * MINUS: -
     * TIMES: \*
     * DIV: /
     * WHITE_SPACE (' '|\n|\r|\t)*
     */

    @Override
    protected void init_lexer() {
        lex = new LexerImpl();

        Automaton a_num = new AutomatonImpl();
        a_num.addState(0, true, false);
        a_num.addState(1, false, false);
        a_num.addState(2, false, true);
        for (char c = '0'; c <= '9'; c++) {
            a_num.addTransition(0, c, 0);
            a_num.addTransition(1, c, 2);
            a_num.addTransition(2, c, 2);
        }
        a_num.addTransition(0, '.', 1);
        lex.add_automaton(TokenType.NUM, a_num);

        lex.add_automaton(TokenType.PLUS, singleCharAutomaton('+'));
        lex.add_automaton(TokenType.MINUS, singleCharAutomaton('-'));
        lex.add_automaton(TokenType.TIMES, singleCharAutomaton('*'));
        lex.add_automaton(TokenType.DIV, singleCharAutomaton('/'));
        lex.add_automaton(TokenType.LPAREN, singleCharAutomaton('('));
        lex.add_automaton(TokenType.RPAREN, singleCharAutomaton(')'));

        Automaton a_ws = new AutomatonImpl();
        a_ws.addState(0, true, true);
        a_ws.addState(1, false, true);
        for (char c : new char[]{' ', '\n', '\r', '\t'}) {
            a_ws.addTransition(0, c, 1);
            a_ws.addTransition(1, c, 1);
        }
        lex.add_automaton(TokenType.WHITE_SPACE, a_ws);
    }

    private Automaton singleCharAutomaton(char c) {
        Automaton a = new AutomatonImpl();
        a.addState(0, true, false);
        a.addState(1, false, true);
        a.addTransition(0, c, 1);
        return a;
    }
}