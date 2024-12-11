package utils;

import constants.Constants;

public class InstructionUtil {

    public static boolean isInstruction(String input) {
        boolean output = false;
        for (String variable : Constants.INSTRUCTIONS) {
            if (input.toLowerCase().contains(variable)) {
                output = true;
                break;
            }
        }
        return output;
    }

}
