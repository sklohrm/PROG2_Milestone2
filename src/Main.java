import Instructions.MIPSInstructionManager;
import io.IOHandler;
import simulator.Simulator;
import test.TestValues;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        switch (args.length) {
            case 1 -> {
                if (args[0].equalsIgnoreCase("test")) {
                    //testToString();
                    testSimulator();
                } else {
                    if (args[0].matches("^[0-9A-Fa-f]{8}$")) {
                        System.out.println(disassemble(args[0]));
                    } else {
                        System.err.println("Error: Invalid hex code. Please provide an 8-digit hexadecimal code.");
                        System.exit(1);
                    }
                }
            }
            case 2 -> {
                List<String> data = List.of();
                List<String> text = List.of();
                if ((args[0].contains(".data") && args[1].contains(".text"))) {
                    data = IOHandler.readFromFile(args[0]);
                    text = IOHandler.readFromFile(args[1]);
                } else if (args[0].contains(".text") && args[1].contains(".data")) {
                    data = IOHandler.readFromFile(args[0]);
                    text = IOHandler.readFromFile(args[1]);
                } else {
                    System.err.println("Error: Invalid file format. Ensure that .text and .data files are provided.");
                    System.exit(1);
                }
                int[] dataArr = data.stream().mapToInt(hex -> Integer.parseInt(hex, 16)).toArray();
                int[] textArr = text.stream().mapToInt(hex -> Integer.parseInt(hex, 16)).toArray();
                Simulator simulator = new Simulator(dataArr, textArr);
                simulator.run();
            }
            default -> {
                System.out.println("Disassembles valid 8-digit hexadecimal code into relevant MIPS instruction values.");
                System.out.println("Usage: java -jar PROG2_Milestone1.jar <hexCode>");
                System.out.println("Test: java -jar PROG2_Milestone1.jar test");
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

    public static void testSimulator() {
        int[] dataArr = java.util.Arrays.stream(TestValues.EVEN_OR_ODD_DATA)
                .mapToInt(hex -> Integer.parseInt(hex, 16))
                .toArray();

        int[] textArr = java.util.Arrays.stream(TestValues.EVEN_OR_ODD_TEXT)
                .mapToInt(hex -> Integer.parseInt(hex, 16))
                .toArray();


        Simulator simulator = new Simulator(dataArr, textArr);

        simulator.debug();
//        simulator.run();


    }

}