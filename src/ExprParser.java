import java.util.*;

/**
 * Created by fly on 5/26/17.
 */
public class ExprParser {
    private ExprAST root;
    //make tokenList
    private ExprLexer exprLexer;
    //tokenList to handle
    private List<ExprToken> tokenList = new ArrayList<>();
    //brackets map, Left -> Right
    private Map<Integer, Integer> braMap = new HashMap<>();

    public ExprParser(String input) {
        exprLexer = new ExprLexer(input);

    }

    private void checkAndSimplifyNum() throws ExprException {
        for (int i = 0; i < tokenList.size() - 1; ++i) {
            ExprToken token = tokenList.get(i);
            if (token.type.equals(ExprToken.TokenName.NUM)) {
                long numOfDots = token.text.chars().filter(x -> x == '.').count();
                if (token.text.charAt(token.text.length() - 1) == '.') {
                    throw new ExprException("new");
                }
                if (numOfDots > 1) {
                    throw new ExprException("more than one . found in " + token.text);
                }
                int j;
                for (j = 0; j < token.text.length(); ++j) {
                    if (token.text.charAt(j) != '0')
                        break;
                }
                if (token.text.charAt(j) == '.') {
                    token.text = "0" + token.text.substring(j);
                } else {
                    token.text = token.text.substring(j);
                }
            }
        }
    }

    private ExprAST buildTree() throws ExprException {
        ExprToken token;
        Stack<Integer> braIndex = new Stack<>();
        int index = 0;
        while (!(token = exprLexer.nextToken()).type.equals(ExprToken.TokenName.EOF)) {
            if (token.type.equals(ExprToken.TokenName.LBR)) {
                braIndex.push(index);
            } else if (token.type.equals(ExprToken.TokenName.RBR)) {
                int leftBra = braIndex.pop();
                braMap.put(leftBra, index);
                braMap.put(index, leftBra);
            }
            tokenList.add(token);
            ++index;
        }
        checkAndSimplifyNum();
        return root = buildTree(0, tokenList.size());
    }

    private ExprAST buildTree(int start, int end) {
        //take expr from (expr)
        while (braMap.get(start) != null && braMap.get(start) == end - 1) {
            ++start;
            --end;
        }
        if (start >= end)
            return null;
        ExprAST node;
        //find + or -
        for (int i = start; i < end; ++i) {
            ExprToken token = tokenList.get(i);
            //skip ( -> )
            if (token.type.equals(ExprToken.TokenName.LBR)) {
                i = braMap.get(i);
            }
            if (token.type.equals(ExprToken.TokenName.ADD)
                    || token.type.equals(ExprToken.TokenName.SUB)) {
                if (i == start && end - start > 2
                        && (tokenList.get(i + 1).type.equals(ExprToken.TokenName.VAR)
                        || tokenList.get(i + 1).type.equals(ExprToken.TokenName.NUM)))
                    continue;
                if (i - 1 >= 0) {
                    ExprToken prevToken = tokenList.get(i - 1);
                    if (prevToken.type.equals(ExprToken.TokenName.ADD)
                            || prevToken.type.equals(ExprToken.TokenName.SUB)
                            || prevToken.type.equals(ExprToken.TokenName.POW)
                            || prevToken.type.equals(ExprToken.TokenName.LBR)
                            || prevToken.type.equals(ExprToken.TokenName.MOD)
                            || prevToken.type.equals(ExprToken.TokenName.MUL)) {
                        continue;
                    }
                }
                node = new ExprAST(token.type, token.text);
                node.left = buildTree(start, i);
                node.right = buildTree(i + 1, end);
                return node;
            }
        }
        //find * or /
        for (int i = start; i < end; ++i) {
            ExprToken token = tokenList.get(i);
            //skip ( -> )
            if (token.type.equals(ExprToken.TokenName.LBR)) {
                i = braMap.get(i);
            }
            if (token.type.equals(ExprToken.TokenName.MUL)
                    || token.type.equals(ExprToken.TokenName.MOD)) {

                node = new ExprAST(token.type, token.text);
                node.left = buildTree(start, i);
                node.right = buildTree(i + 1, end);
                return node;
            }
        }
        //find ^
        for (int i = end - 1; i >= start; --i) {
            ExprToken token = tokenList.get(i);
            if (token.type.equals(ExprToken.TokenName.RBR)) {
                i = braMap.get(i);
            }
            if (token.type.equals(ExprToken.TokenName.POW)) {
                node = new ExprAST(token.type, token.text);
                node.left = buildTree(start, i);
                node.right = buildTree(i + 1, end);
                return node;
            }
        }
        return new ExprAST(tokenList.get(start).type, tokenList.get(start).text);
    }

    public String getExpr() throws ExprException {
        root = buildTree();
        return root.exprString();
    }
}
