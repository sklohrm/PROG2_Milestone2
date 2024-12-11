import Instructions.MIPSInstructionManager;
import test.TestValues;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Disassembles valid 8-digit hexadecimal code into relevant MIPS instruction values.");
            System.out.println("Usage: java -jar PROG2_Milestone1.jar <hexCode>");
            System.out.println("Test: java -jar PROG2_Milestone1.jar test");
            System.exit(1);
        }

        String input = args[0];

        if (input.equalsIgnoreCase("test")) {
             testToString();
        } else {
            if (input.matches("^[0-9A-Fa-f]{8}$")) {
                System.out.println(disassemble(input));
            } else {
                System.err.println("Error: Invalid hex code. Please provide an 8-digit hexadecimal code.");
                System.exit(1);
            }
        }
    }

    public static String disassemble(String hexString) {
        long hexCode = Long.parseLong(hexString, 16);
        return MIPSInstructionManager.toString((int) hexCode);
    }

    public static void testToString() {
        int pass = 0, fail = 0;
        int total = 0;

        System.out.println("Test toString");
        for (int i = 0; i < TestValues.TEST_HEX_CODES.length; ++i) {
            System.out.print(TestValues.TEST_HEX_CODES[i] + ":");
            String output = disassemble(TestValues.TEST_HEX_CODES[i]);
            if (!output.equals(TestValues.TEST_TO_STRING_OUTPUT[i])) {
                fail++;
                System.out.println("Test Failed");
            } else {
                pass++;
                System.out.println(" ");
            }
            total++;
            System.out.println("Evaluated:\t" + output);
            System.out.println("Expected:\t" + TestValues.TEST_TO_STRING_OUTPUT[i]);
            System.out.println(" ");
        }
        System.out.println("Test Results");
        System.out.println("Passed: " + pass + "\\" + total + " Fail: " + fail + "\\" + total);
    }

}