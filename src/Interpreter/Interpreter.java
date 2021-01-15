package Interpreter;

import Board.Board;
import Parser.Parser;
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

public class Interpreter implements IVisitor{
    private Board board;
    private Parser parser;
    private Program program;

    public Interpreter(){

    }

    // Program tree
    @Override public void visit(Program program){

    }
    @Override public void visit(Function function){

    }
    @Override public void visit(Block block){

    }
    @Override public void visit(Parameters parameters){

    }
    @Override public void visit(Arguments arguments){

    }

    // Variables
    @Override public void visit(ArrayVariable arrayVariable){

    }
    @Override public void visit(BoolVariable boolVariable){

    }
    @Override public void visit(ColorVariable colorVariable){

    }
    @Override public void visit(HexVariable hexVariable){

    }
    @Override public void visit(IntVariable intVariable){

    }
    @Override public void visit(PlanetVariable planetVariable){

    }
    @Override public void visit(StringVariable stringVariable){

    }
    @Override public void visit(UnitVariable unitVariable){

    }
    @Override public void visit(Variable variable){

    }
    @Override public void visit(VarVariable varVariable){

    }

    // Value
    @Override public void visit(VariableValue variableValue){

    }
    @Override public void visit(Value value){

    }
    @Override public void visit(FunctionCallValue functionCallValue){

    }

    // Literals
    @Override public void visit(BoolLiteral boolLiteral){

    }
    @Override public void visit(ColorLiteral colorLiteral){

    }
    @Override public void visit(HexLiteral hexLiteral){

    }
    @Override public void visit(IntLiteral intLiteral){

    }
    @Override public void visit(Literal literal){

    }
    @Override public void visit(PlanetLiteral planetLiteral){

    }
    @Override public void visit(StringLiteral stringLiteral){

    }
    @Override public void visit(UnitLiteral unitLiteral){

    }

    // BoardStateCheck
    @Override public void visit(ActivationCheck activationCheck){

    }
    @Override public void visit(HexStateCheck hexStateCheck){

    }
    @Override public void visit(PlanetStateCheck planetStateCheck){

    }
    @Override public void visit(PlayerStateCheck playerStateCheck){

    }

    // Statements
    @Override public void visit(ArrayDeclaration arrayDeclaration){

    }
    @Override public void visit(Assignment assignment){

    }
    @Override public void visit(Break breakStm){

    }
    @Override public void visit(Conditional conditional){

    }
    @Override public void visit(Continue continueStm){

    }
    @Override public void visit(FunctionCall functionCallStm){

    }
    @Override public void visit(Loop loop){

    }
    @Override public void visit(Print print){

    }
    @Override public void visit(Return returnStm){

    }
    @Override public void visit(Statement statement){

    }
    @Override public void visit(VariableDeclaration variableDeclaration){

    }

    // ConditionExpression
    @Override public void visit(AddExpression addExpression){

    }
    @Override public void visit(AndCondition andCondition){

    }
    @Override public void visit(Expression expression){

    }
    @Override public void visit(MultiplyExpression multiplyExpression){

    }
    @Override public void visit(NegativeUnaryExpression negativeUnaryExpression){

    }
    @Override public void visit(NotUnaryExpression notUnaryExpression){

    }
    @Override public void visit(OrCondition orCondition){

    }
    @Override public void visit(RelationalCondition relationalCondition){

    }
    @Override public void visit(UnaryExpression unaryExpression){

    }

    // Operators
    @Override public void visit(AndOperator andOperator){

    }
    @Override public void visit(DivideOperator divideOperator){

    }
    @Override public void visit(EqualOperator equalOperator){

    }
    @Override public void visit(GreaterEqualOperator greaterEqualOperator){

    }
    @Override public void visit(GreaterOperator greaterOperator){

    }
    @Override public void visit(LessEqualOperator lessEqualOperator){

    }
    @Override public void visit(LessOperator lessOperator){

    }
    @Override public void visit(MinusOperator minusOperator){

    }
    @Override public void visit(MultiplyOperator multiplyOperator){

    }
    @Override public void visit(NegativeOperator negativeOperator){

    }
    @Override public void visit(NotEqualOperator notEqualOperator){

    }
    @Override public void visit(NotOperator notOperator){

    }
    @Override public void visit(Operator operator){

    }
    @Override public void visit(OrOperator orOperator){

    }
    @Override public void visit(PlusOperator plusOperator){

    }

    // BoardChange
    @Override public void visit(Activation activation){

    }
    @Override public void visit(BoardChange boardChange){

    }
    @Override public void visit(PlayerAction playerAction){

    }
    @Override public void visit(UnitsAction unitsAction){

    }
    @Override public void visit(UnitsList unitsList){

    }
}
