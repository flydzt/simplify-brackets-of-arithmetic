/**
 * Created by fly on 5/26/17.
 */
public class ExprToken {
    public TokenName type;
    public String text;

    public ExprToken(TokenName type, String text) {
        this.type = type;
        this.text = text;
    }

    @Override
    public String toString() {
        return "<" + type.toString() + ":" + text + ">";
    }

    public enum TokenName {
        NUM, VAR, ADD, SUB, MUL, MOD, POW, LBR, RBR, EOF
        //111,xyz,+++,---,***,///,^^^,(((,))),   ;
    }
}
