package Utilities;

public class ExpressionString {
    private String originalExpression;

    public ExpressionString(String string){
        this.originalExpression = string.replaceAll("\\s+","").replaceAll("[()]","");
    }

    public boolean compareExpressions(String expression){
        boolean isTheSame = true;
        expression = expression.replaceAll("\\s+","").replaceAll("[()]","");
        if(originalExpression.length() != expression.length()){
            return false;
        }
        for(int i = 0; i < originalExpression.length(); ++i){
            if(originalExpression.charAt(i) != expression.charAt(i)){
                isTheSame = false;
                break;
            }
        }
        return isTheSame;
    }
}
