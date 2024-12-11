package utils;

import constants.Constants;

public class RegisterUtil {

    public static boolean isRegister(String input) {
        boolean output = false;
        for (String register : Constants.REGISTERS) {

        }
        return output;
    }

    public static int toDecimal(String register) {
        for (int i = 0; i < Constants.REGISTERS.length; i++) {
            if (register.equals(Constants.REGISTERS[i])) {
                return i;
            }
        }
        throw new IllegalArgumentException("Invalid register: " + register);
    }

}
