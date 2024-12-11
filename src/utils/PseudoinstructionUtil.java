package utils;

import constants.Constants;

public class PseudoinstructionUtil {

    public static String toInstructions() {
        return null;
    }

    public static boolean isPseudoInstruction(String input) {
        boolean output = false;
        for (String pseudoInstruction : Constants.PSEUDOINSTRUCTIONS) {
            if (input.toLowerCase().contains(pseudoInstruction)) {
                output = true;
                break;
            }
        }
        return output;
    }
}
