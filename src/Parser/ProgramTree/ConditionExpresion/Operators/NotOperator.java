package Parser.ProgramTree.ConditionExpresion.Operators;

public class NotOperator extends Operator{
    public NotOperator(){
        this.operator = "!";
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(operator);
        stringBuilder.append("(");
        if(left != null){
            stringBuilder.append(left.toString());
        }
        if(right != null){
            stringBuilder.append(right.toString());
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
