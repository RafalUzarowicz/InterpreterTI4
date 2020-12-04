package Scanner;

import java.util.ArrayList;
import java.util.Arrays;

public class Constants {
    public final static class Board{
        public final static int HEX_NUMBER = 51;
        public final static int PLANET_NUMBER = 59;
    }
    public final static class Token{
        public final static int MAX_IDENTIFIER_LEN = 32;
        public final static int MAX_NUMBER_VAL = Integer.MAX_VALUE;
        public final static ArrayList<Character> SPECIAL_CHARS = new ArrayList<>(Arrays.asList('_', '.', ',', '-', ' ', '/', '\\', ':'));
    }
    public final static class Position{
        public final static int FIRST_INDEX = 1;
    }
}
