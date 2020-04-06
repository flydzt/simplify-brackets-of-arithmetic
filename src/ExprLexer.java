/**
 * Created by fly on 5/26/17.
 */
public class ExprLexer {
    public static final char EOF = (char) -1;
    //current char
    char c;
    //index of c in input
    int index;
    //string to handle
    String input;

    public ExprLexer(String input) {
        this.input = input;
        c = input.charAt(index);
    }

    //next char
    private void consume() {
        if ((++index) >= input.length())
            c = EOF;
        else
            c = input.charAt(index);
    }

    //is c a digit
    private boolean isDigital() {
        return c >= '0' && c <= '9' || c == '.';
    }

    //is c a blank
    private boolean isBlank() {
        return c == ' ' || c == '\n' || c == '\r' || c == '\t';
    }

    //is c a valid var
    private boolean isLower() {
        return c >= 'a' && c <= 'z';
    }

    private boolean isUpper() {
        return c >= 'A' && c <= 'Z';
    }

    private boolean isVar() {
        return isLower() || isUpper() || isDigital() || c == '_';
    }

    //get the whole num
    private String getNum() {
        StringBuilder stringBuilder = new StringBuilder();
        while (isDigital()) {
            stringBuilder.append(c);
            consume();
        }
        return stringBuilder.toString();
    }

    //get the whole var
    private String getVar() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(c);
        consume();
        while (isVar()) {
            stringBuilder.append(c);
            consume();
        }
        return stringBuilder.toString();
    }

    //get next token
    public ExprToken nextToken() throws ExprException {
        while (c != EOF) {
            if (c == '-') {
                consume();
                return new ExprToken(ExprToken.TokenName.SUB, "-");
            } else if (c == '+') {
                consume();
                return new ExprToken(ExprToken.TokenName.ADD, "+");
            } else if (c == '*') {
                consume();
                return new ExprToken(ExprToken.TokenName.MUL, "*");
            } else if (c == '/') {
                consume();
                return new ExprToken(ExprToken.TokenName.MOD, "/");
            } else if (c == '^') {
                consume();
                return new ExprToken(ExprToken.TokenName.POW, "^");
            } else if (c == '(') {
                consume();
                return new ExprToken(ExprToken.TokenName.LBR, "(");
            } else if (c == ')') {
                consume();
                return new ExprToken(ExprToken.TokenName.RBR, ")");
            } else if (isUpper() || isLower() || c == '_') {
                return new ExprToken(ExprToken.TokenName.VAR, getVar());
            } else if (isBlank()) {
                consume();
            } else if (isDigital()) {
                return new ExprToken(ExprToken.TokenName.NUM, getNum());
            } else { //c not a valid ExprToken
                StringBuilder sb = new StringBuilder();
                sb.append("not a valid input: ")
                        .append(c)
                        .append("\n")
                        .append(input)
                        .append("\n");
                while (index-- > 0) {
                    sb.append(" ");
                }
                sb.append("^");
                throw new ExprException(sb.toString());
            }
        }
        return new ExprToken(ExprToken.TokenName.EOF, "EOF");
    }
}
