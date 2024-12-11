package utils;

import constants.Constants;

public class DeclarationUtil {

    public static boolean isDeclaration(String input) {
        boolean output = false;
        for (String variable : Constants.VARIABLE_TYPES) {
            if (input.toLowerCase().contains(variable)) {
                output = true;
                break;
            }
        }
        return output;
    }

}
