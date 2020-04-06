/**
 * Created by fly on 5/26/17.
 */


// tree node
public class ExprAST {
    ExprToken.TokenName tokenType;
    String text;
    ExprAST left;
    ExprAST right;

    public ExprAST(ExprToken.TokenName tokenType, String text) {
        this.tokenType = tokenType;
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public String exprString() throws ExprException {

        if (((tokenType.equals(ExprToken.TokenName.POW)
                || tokenType.equals(ExprToken.TokenName.MUL)
                || tokenType.equals(ExprToken.TokenName.MOD))
                && (left == null || right == null))
                ||
                ((tokenType.equals(ExprToken.TokenName.ADD)
                        || tokenType.equals(ExprToken.TokenName.SUB))
                        && right == null)
                ||
                ((tokenType.equals(ExprToken.TokenName.NUM)
                        || tokenType.equals(ExprToken.TokenName.VAR))
                        && (left != null || right != null))) {
            throw new ExprException("wrong input around " + text);
        }
        if (left == null) {
            if (right == null)
                return text;
            if (right.tokenType.equals(ExprToken.TokenName.VAR)
                    || right.tokenType.equals(ExprToken.TokenName.NUM))
                return text + right.exprString();
            else return text + "(" + right.exprString() + ")";
        }
        String leftExpr = left.exprString();
        String rightExpr = right == null ? "" : right.exprString();
        StringBuilder stringBuilder = new StringBuilder();
        if (tokenType.equals(ExprToken.TokenName.POW)) {  // ^
            if (left != null && !(left.tokenType.equals(ExprToken.TokenName.NUM)
                    || left.tokenType.equals(ExprToken.TokenName.VAR))) {
                stringBuilder.append("(").append(leftExpr).append(")");
            } else {
                stringBuilder.append(leftExpr);
            }
            stringBuilder.append(text);
            if (right != null && !(right.tokenType.equals(ExprToken.TokenName.NUM)
                    || right.tokenType.equals(ExprToken.TokenName.VAR)
                    || right.tokenType.equals(ExprToken.TokenName.POW))) {
                stringBuilder.append("(").append(rightExpr).append(")");
            } else {
                stringBuilder.append(rightExpr);
            }
        } else if (tokenType.equals(ExprToken.TokenName.MUL)
                || tokenType.equals(ExprToken.TokenName.MOD)) {  //* /
            if (left != null && left.left != null && left.tokenType.equals(ExprToken.TokenName.ADD)
                    || left.tokenType.equals(ExprToken.TokenName.SUB)) {
                stringBuilder.append("(").append(leftExpr).append(")");
            } else {
                stringBuilder.append(leftExpr);
            }
            stringBuilder.append(text);
            if (right != null && right.left != null && right.tokenType.equals(ExprToken.TokenName.ADD)
                    || right.tokenType.equals(ExprToken.TokenName.SUB)) {
                stringBuilder.append("(").append(rightExpr).append(")");
            } else {
                stringBuilder.append(rightExpr);
            }
        } else {
            stringBuilder.append(leftExpr).append(text).append(rightExpr);
        }
        return stringBuilder.toString();
    }
}
