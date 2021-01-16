package Interpreter;

import Exceptions.InterpreterException;
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
/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 * Visitor interface.
 */
public interface IVisitor {
    default void visit(INode node) throws Exception {
        Method method = this.getClass().getMethod("visit", node.getClass());
        method.invoke(this, node);
    }

    void visit(Program program) throws Exception;
    void visit(Function function) throws Exception;
    void visit(Block block) throws Exception;
    void visit(Parameters parameters) throws InterpreterException;
    void visit(Arguments arguments) throws Exception;

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
    void visit(VariableValue variableValue) throws Exception;
    void visit(Value value);
    void visit(FunctionCallValue functionCallValue) throws Exception;

    // Literals
    void visit(BoolLiteral boolLiteral) throws InterpreterException;
    void visit(ColorLiteral colorLiteral) throws InterpreterException;
    void visit(HexLiteral hexLiteral) throws InterpreterException;
    void visit(IntLiteral intLiteral) throws InterpreterException;
    void visit(Literal literal);
    void visit(PlanetLiteral planetLiteral) throws InterpreterException;
    void visit(StringLiteral stringLiteral) throws InterpreterException;
    void visit(UnitLiteral unitLiteral) throws InterpreterException;

    // BoardStateCheck
    void visit(ActivationCheck activationCheck) throws Exception;
    void visit(HexStateCheck hexStateCheck) throws Exception;
    void visit(PlanetStateCheck planetStateCheck) throws Exception;
    void visit(PlayerStateCheck playerStateCheck) throws Exception;

    // Statements
    void visit(ArrayDeclaration arrayDeclaration) throws Exception;
    void visit(Assignment assignment) throws Exception;
    void visit(Break breakStm);
    void visit(Conditional conditional) throws Exception;
    void visit(Continue continueStm);
    void visit(FunctionCall functionCallStm) throws Exception;
    void visit(Loop loop) throws Exception;
    void visit(Print print) throws Exception;
    void visit(Return returnStm) throws Exception;
    void visit(VariableDeclaration variableDeclaration) throws Exception;

    // ConditionExpression
    void visit(AddExpression addExpression) throws Exception;
    void visit(AndCondition andCondition) throws Exception;
    void visit(Expression expression);
    void visit(MultiplyExpression multiplyExpression) throws Exception;
    void visit(NegativeUnaryExpression negativeUnaryExpression) throws Exception;
    void visit(NotUnaryExpression notUnaryExpression) throws Exception;
    void visit(OrCondition orCondition) throws Exception;
    void visit(RelationalCondition relationalCondition) throws Exception;
    void visit(UnaryExpression unaryExpression) throws Exception;
    void visit(EqualityCondition equalityCondition) throws Exception;

    // Operators
    void visit(AndOperator andOperator) throws InterpreterException;
    void visit(DivideOperator divideOperator) throws InterpreterException;
    void visit(EqualOperator equalOperator) throws InterpreterException;
    void visit(GreaterEqualOperator greaterEqualOperator) throws InterpreterException;
    void visit(GreaterOperator greaterOperator) throws InterpreterException;
    void visit(LessEqualOperator lessEqualOperator) throws InterpreterException;
    void visit(LessOperator lessOperator) throws InterpreterException;
    void visit(MinusOperator minusOperator) throws InterpreterException;
    void visit(MultiplyOperator multiplyOperator) throws InterpreterException;
    void visit(NegativeOperator negativeOperator) throws InterpreterException;
    void visit(NotEqualOperator notEqualOperator) throws InterpreterException;
    void visit(NotOperator notOperator) throws InterpreterException;
    void visit(Operator operator);
    void visit(OrOperator orOperator) throws InterpreterException;
    void visit(PlusOperator plusOperator) throws InterpreterException;

    // BoardChange
    void visit(Activation activation) throws Exception;
    void visit(BoardChange boardChange) throws Exception;
    void visit(PlayerAction playerAction);
    void visit(UnitsAction unitsAction) throws Exception;
    void visit(UnitsList unitsList) throws Exception;
}
