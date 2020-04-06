public class Main {

    public static void main(String[] args) {
        String input1 = "-02-3 * 2.00 + (121 * 1101.1)";
        String input2 = "-(1.1*1+190.0000+x_y+(_this_&var^(1+2^(3^3))*10+1)*(2+(3+4+(5*6))*10)*(7*8)+((8/12)*(21 +112)))";
        try {
            ExprParser exprParser_1 = new ExprParser(input1);
            System.out.println("input >>>  :  " + input1);
            System.out.println("output >>> :  " + exprParser_1.getExpr());
            ExprParser exprParser = new ExprParser(input2);
            exprParser.getExpr();
        } catch (ExprException e) {
            e.printStackTrace();
        }
    }
}
