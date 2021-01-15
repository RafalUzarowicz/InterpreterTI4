package Interpreter;

import Utilities.ProgramTree.*;
import Utilities.ProgramTree.BoardChange.*;
import Utilities.ProgramTree.ConditionExpresion.*;
import Utilities.ProgramTree.ConditionExpresion.Operators.*;
import Utilities.ProgramTree.Statements.*;
import Utilities.ProgramTree.Value.BoardStateCheck.ActivationCheck;
import Utilities.ProgramTree.Value.BoardStateCheck.HexStateCheck;
import Utilities.ProgramTree.Value.BoardStateCheck.PlanetStateCheck;
import Utilities.ProgramTree.Value.BoardStateCheck.PlayerStateCheck;
import Utilities.ProgramTree.Value.FunctionCallValue;
import Utilities.ProgramTree.Value.Literals.*;
import Utilities.ProgramTree.Value.Value;
import Utilities.ProgramTree.Value.VariableValue;
import Utilities.ProgramTree.Variables.*;

import java.lang.reflect.Method;

public interface IVisitor {
    default void visit(INode node) throws Exception {
        Method method = this.getClass().getMethod("visit", node.getClass());
        method.invoke(this, node);
    }

    void visit(Program program);
    void visit(Function function);
    void visit(Block block);
    void visit(Parameters parameters);
    void visit(Arguments arguments);

    // Variables
    void visit(ArrayVariable arrayVariable);
    void visit(BoolVariable boolVariable);
    void visit(ColorVariable colorVariable);
    void visit(HexVariable hexVariable);
    void visit(IntVariable intVariable);
    void visit(PlanetVariable planetVariable);
    void visit(StringVariable stringVariable);
    void visit(UnitVariable unitVariable);
    void visit(Variable variable);
    void visit(VarVariable varVariable);

    // Value
    void visit(VariableValue variableValue);
    void visit(Value value);
    void visit(FunctionCallValue functionCallValue);

    // Literals
    void visit(BoolLiteral boolLiteral);
    void visit(ColorLiteral colorLiteral);
    void visit(HexLiteral hexLiteral);
    void visit(IntLiteral intLiteral);
    void visit(Literal literal);
    void visit(PlanetLiteral planetLiteral);
    void visit(StringLiteral stringLiteral);
    void visit(UnitLiteral unitLiteral);

    // BoardStateCheck
    void visit(ActivationCheck activationCheck);
    void visit(HexStateCheck hexStateCheck);
    void visit(PlanetStateCheck planetStateCheck);
    void visit(PlayerStateCheck playerStateCheck);

    // Statements
    void visit(ArrayDeclaration arrayDeclaration);
    void visit(Assignment assignment);
    void visit(Break breakStm);
    void visit(Conditional conditional);
    void visit(Continue continueStm);
    void visit(FunctionCall functionCallStm);
    void visit(Loop loop);
    void visit(Print print);
    void visit(Return returnStm);
    void visit(Statement statement);
    void visit(VariableDeclaration variableDeclaration);

    // ConditionExpression
    void visit(AddExpression addExpression);
    void visit(AndCondition andCondition);
    void visit(Expression expression);
    void visit(MultiplyExpression multiplyExpression);
    void visit(NegativeUnaryExpression negativeUnaryExpression);
    void visit(NotUnaryExpression notUnaryExpression);
    void visit(OrCondition orCondition);
    void visit(RelationalCondition relationalCondition);
    void visit(UnaryExpression unaryExpression);

    // Operators
    void visit(AndOperator andOperator);
    void visit(DivideOperator divideOperator);
    void visit(EqualOperator equalOperator);
    void visit(GreaterEqualOperator greaterEqualOperator);
    void visit(GreaterOperator greaterOperator);
    void visit(LessEqualOperator lessEqualOperator);
    void visit(LessOperator lessOperator);
    void visit(MinusOperator minusOperator);
    void visit(MultiplyOperator multiplyOperator);
    void visit(NegativeOperator negativeOperator);
    void visit(NotEqualOperator notEqualOperator);
    void visit(NotOperator notOperator);
    void visit(Operator operator);
    void visit(OrOperator orOperator);
    void visit(PlusOperator plusOperator);

    // BoardChange
    void visit(Activation activation);
    void visit(BoardChange boardChange);
    void visit(PlayerAction playerAction);
    void visit(UnitsAction unitsAction);
    void visit(UnitsList unitsList);
}
