package Utilities;

import Exceptions.InterpreterException;
import Utilities.ProgramTree.Value.Literals.*;
import Utilities.ProgramTree.Variables.*;
/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class InterpreterUtils {
    private static int line = 0;
    public static void setLine(int newLine){
        line = newLine;
    }
    public static Literal literalCast(Literal source, Variable variable) throws InterpreterException {
        if (BoolVariable.class.equals(variable.getClass())) {
            return boolLiteralCast(source);
        } else if (ColorVariable.class.equals(variable.getClass())) {
            return colorLiteralCast(source);
        } else if (HexVariable.class.equals(variable.getClass())) {
            return hexLiteralCast(source);
        } else if (IntVariable.class.equals(variable.getClass())) {
            return intLiteralCast(source);
        } else if (PlanetVariable.class.equals(variable.getClass())) {
            return planetLiteralCast(source);
        } else if (StringVariable.class.equals(variable.getClass())) {
            return stringLiteralCast(source);
        } else if (UnitVariable.class.equals(variable.getClass())) {
            return unitLiteralCast(source);
        } else if (VarVariable.class.equals(variable.getClass())) {
            return source;
        }
        throw new InterpreterException(line, "Unknown variable type for casting.");
    }

    private static Literal boolLiteralCast(Literal source) throws InterpreterException {
        if (BoolLiteral.class.equals(source.getClass())) {
            return new BoolLiteral(source.getValue());
        } else if (ColorLiteral.class.equals(source.getClass())) {
            throw new InterpreterException(line, "No cast from color to bool.");
        } else if (HexLiteral.class.equals(source.getClass())) {
            throw new InterpreterException(line, "No cast from hex to bool.");
        } else if (IntLiteral.class.equals(source.getClass())) {
            int value = Integer.parseInt(source.getValue());
            if(value == 0){
                return new BoolLiteral("false");
            }else{
                return new BoolLiteral("true");
            }
        } else if (PlanetLiteral.class.equals(source.getClass())) {
            throw new InterpreterException(line, "No cast from planet to bool.");
        } else if (StringLiteral.class.equals(source.getClass())) {
            if(source.getValue().equals("")){
                return new BoolLiteral("false");
            }else{
                return new BoolLiteral("true");
            }
        } else if (UnitLiteral.class.equals(source.getClass())) {
            throw new InterpreterException(line, "No cast from unit to bool.");
        } else if (Literal.class.equals(source.getClass())){
            return new BoolLiteral(source.getValue());
        }
        throw new InterpreterException(line, "Unknown literal type.");
    }

    private static Literal colorLiteralCast(Literal source) throws InterpreterException {
        if (BoolLiteral.class.equals(source.getClass())) {
            throw new InterpreterException(line, "No cast from bool to color.");
        } else if (ColorLiteral.class.equals(source.getClass())) {
            return new ColorLiteral(source.getValue());
        } else if (HexLiteral.class.equals(source.getClass())) {
            throw new InterpreterException(line, "No cast from hex to color.");
        } else if (IntLiteral.class.equals(source.getClass())) {
            throw new InterpreterException(line, "No cast from number to color.");
        } else if (PlanetLiteral.class.equals(source.getClass())) {
            throw new InterpreterException(line, "No cast from planet to color.");
        } else if (StringLiteral.class.equals(source.getClass())) {
            if(ScannerUtils.literalToType.get(source.getValue()) == Token.Type.ColorLiteral){
                return new ColorLiteral(source.getValue());
            }else{
                throw new InterpreterException(line, "Wrong cast from string to color.");
            }
        } else if (UnitLiteral.class.equals(source.getClass())) {
            throw new InterpreterException(line, "No cast from unit to color.");
        } else if (Literal.class.equals(source.getClass())){
            return new ColorLiteral(source.getValue());
        }
        throw new InterpreterException(line, "Unknown literal type.");
    }

    private static Literal hexLiteralCast(Literal source) throws InterpreterException {
        if (BoolLiteral.class.equals(source.getClass())) {
            throw new InterpreterException(line, "No cast from bool to hex.");
        } else if (ColorLiteral.class.equals(source.getClass())) {
            throw new InterpreterException(line, "No cast from color to hex.");
        } else if (HexLiteral.class.equals(source.getClass())) {
            String string = source.getValue();
            if(string.charAt(0) == 'h'){
                try{
                    int value = Integer.parseInt(string.substring(1));
                    if(value < Constants.Board.HEX_NUMBER && value >= 0){
                        return new HexLiteral(string);
                    }
                }catch (NumberFormatException ignored){
                    throw new InterpreterException(line, "Wrong hex index.");
                }
            }
            throw new InterpreterException(line, "Wrong cast from hex to hex.");
        } else if (IntLiteral.class.equals(source.getClass())) {
            int value = Integer.parseInt(source.getValue());
            if(value < Constants.Board.HEX_NUMBER && value >= 0){
                return new HexLiteral("h"+value);
            }else{
                throw new InterpreterException(line, "Wrong cast from number to hex.");
            }
        } else if (PlanetLiteral.class.equals(source.getClass())) {
            throw new InterpreterException(line, "No cast from planet to hex.");
        } else if (StringLiteral.class.equals(source.getClass())) {
            String string = source.getValue();
            if(string.charAt(0) == 'h'){
                try{
                    int value = Integer.parseInt(string.substring(1));
                    if(value < Constants.Board.HEX_NUMBER && value >= 0){
                        return new HexLiteral("h"+value);
                    }
                }catch (NumberFormatException ignored){

                }
            }
            throw new InterpreterException(line, "Wrong cast from string to hex.");
        } else if (UnitLiteral.class.equals(source.getClass())) {
            throw new InterpreterException(line, "No cast from unit to hex.");
        } else if (Literal.class.equals(source.getClass())){
            return new HexLiteral(source.getValue());
        }
        throw new InterpreterException(line, "Unknown literal type.");
    }

    private static Literal intLiteralCast(Literal source) throws InterpreterException {
        if (BoolLiteral.class.equals(source.getClass())) {
            if(source.getValue().equals("true")){
                return new IntLiteral("1");
            }else if(source.getValue().equals("false")){
                return new IntLiteral("0");
            }
            throw new InterpreterException(line, "Wrong cast from bool to number.");
        } else if (ColorLiteral.class.equals(source.getClass())) {
            throw new InterpreterException(line, "No cast from color to number.");
        } else if (HexLiteral.class.equals(source.getClass())) {
            throw new InterpreterException(line, "No cast from hex to number.");
        } else if (IntLiteral.class.equals(source.getClass())) {
            return new IntLiteral(source.getValue());
        } else if (PlanetLiteral.class.equals(source.getClass())) {
            throw new InterpreterException(line, "No cast from planet to number.");
        } else if (StringLiteral.class.equals(source.getClass())) {
            try{
                int value = Integer.parseInt(source.getValue());
                return new IntLiteral(""+value);
            }catch (NumberFormatException e){
                throw new InterpreterException(line, "Wrong cast from string to number.");
            }
        } else if (UnitLiteral.class.equals(source.getClass())) {
            throw new InterpreterException(line, "No cast from unit to number.");
        } else if (Literal.class.equals(source.getClass())){
            return new IntLiteral(source.getValue());
        }
        throw new InterpreterException(line, "Unknown literal type.");
    }

    private static Literal planetLiteralCast(Literal source) throws InterpreterException {
        if (BoolLiteral.class.equals(source.getClass())) {
            throw new InterpreterException(line, "No cast from bool to planet.");
        } else if (ColorLiteral.class.equals(source.getClass())) {
            throw new InterpreterException(line, "No cast from color to planet.");
        } else if (HexLiteral.class.equals(source.getClass())) {
            throw new InterpreterException(line, "No cast from hex to planet.");
        } else if (IntLiteral.class.equals(source.getClass())) {
            int value = Integer.parseInt(source.getValue());
            if(value < Constants.Board.PLANET_NUMBER && value >= 0){
                return new HexLiteral("p"+value);
            }else{
                throw new InterpreterException(line, "Wrong cast from number to planet.");
            }
        } else if (PlanetLiteral.class.equals(source.getClass())) {
            String string = source.getValue();
            if(string.charAt(0) == 'h'){
                try{
                    int value = Integer.parseInt(string.substring(1));
                    if(value < Constants.Board.PLANET_NUMBER && value >= 0){
                        return new PlanetLiteral(string);
                    }
                }catch (NumberFormatException ignored){
                    throw new InterpreterException(line, "Wrong planet index.");
                }
            }
            throw new InterpreterException(line, "Wrong cast from planet to planet.");
        } else if (StringLiteral.class.equals(source.getClass())) {
            String string = source.getValue();
            if(string.charAt(0) == 'h'){
                try{
                    int value = Integer.parseInt(string.substring(1));
                    if(value < Constants.Board.PLANET_NUMBER && value >= 0){
                        return new HexLiteral("p"+value);
                    }
                }catch (NumberFormatException ignored){

                }
            }
            throw new InterpreterException(line, "Wrong cast from string to planet.");
        } else if (UnitLiteral.class.equals(source.getClass())) {
            throw new InterpreterException(line, "No cast from unit to planet.");
        } else if (Literal.class.equals(source.getClass())){
            return new PlanetLiteral(source.getValue());
        }
        throw new InterpreterException(line, "Unknown literal type.");
    }

    private static Literal stringLiteralCast(Literal source) throws InterpreterException {
        if (BoolLiteral.class.equals(source.getClass())) {
            return new StringLiteral(source.getValue());
        } else if (ColorLiteral.class.equals(source.getClass())) {
            return new StringLiteral(source.getValue());
        } else if (HexLiteral.class.equals(source.getClass())) {
            return new StringLiteral(source.getValue());
        } else if (IntLiteral.class.equals(source.getClass())) {
            return new StringLiteral(source.getValue());
        } else if (PlanetLiteral.class.equals(source.getClass())) {
            return new StringLiteral(source.getValue());
        } else if (StringLiteral.class.equals(source.getClass())) {
            return new StringLiteral(source.getValue());
        } else if (UnitLiteral.class.equals(source.getClass())) {
            return new StringLiteral(source.getValue());
        } else if (Literal.class.equals(source.getClass())){
            return new StringLiteral(source.getValue());
        }
        throw new InterpreterException(line, "Unknown literal type.");
    }

    private static Literal unitLiteralCast(Literal source) throws InterpreterException {
        if (BoolLiteral.class.equals(source.getClass())) {
            throw new InterpreterException(line, "No cast from bool to unit.");
        } else if (ColorLiteral.class.equals(source.getClass())) {
            throw new InterpreterException(line, "No cast from color to unit.");
        } else if (HexLiteral.class.equals(source.getClass())) {
            throw new InterpreterException(line, "No cast from hex to unit.");
        } else if (IntLiteral.class.equals(source.getClass())) {
            throw new InterpreterException(line, "No cast from number to unit.");
        } else if (PlanetLiteral.class.equals(source.getClass())) {
            throw new InterpreterException(line, "No cast from planet to unit.");
        } else if (StringLiteral.class.equals(source.getClass())) {
            if(ScannerUtils.literalToType.get(source.getValue()) == Token.Type.UnitLiteral){
                return new ColorLiteral(source.getValue());
            }else{
                throw new InterpreterException(line, "Wrong cast from string to unit.");
            }
        } else if (UnitLiteral.class.equals(source.getClass())) {
            return new UnitLiteral(source.getValue());
        } else if (Literal.class.equals(source.getClass())){
            return new UnitLiteral(source.getValue());
        }
        throw new InterpreterException(line, "Unknown literal type.");
    }
}
