package simulator;

import Instructions.*;
import constants.Constants;

import java.sql.SQLOutput;
import java.util.Scanner;

public class Simulator {

    public int[] registers, data;
    public MIPSInstruction[] text;

    private final int[] rawText;

    int programCounter;

    public Simulator(int[] data, int[] text) {
        registers = new int[Constants.REGISTERS.length];
        this.data = data.clone();
        rawText = text.clone();

        this.text = new MIPSInstruction[text.length];
        for (int i = 0; i < text.length; i++) {
            this.text[i] = MIPSInstructionManager.createInstruction(text[i]);
        }

        programCounter = 0;
    }

    public void run() {
        while (programCounter < text.length) {
            MIPSInstruction current = text[programCounter];
            System.out.printf("%08x", Constants.TEXT_SEG_START + (programCounter * 4));
            System.out.println(" " + current.toString());
            System.out.println();
            programCounter++;
            execute(current);
        }
    }

    public void debug() {
        Scanner scanner = new Scanner(System.in);
        String input = "";
        while (!input.equals("e") && programCounter < text.length) {
            display();
            System.out.print("Press e to exit or any other key to step forward: ");
            input = scanner.nextLine();
            MIPSInstruction current = text[programCounter];
            programCounter++;
            execute(current);
        }
    }

    private void execute(MIPSInstruction instruction) {
        switch(instruction) {
            case MIPSadd i -> {
                registers[i.getRd()] = registers[i.getRs()] + registers[i.getRt()];
            }
            case MIPSaddiu i -> {
                registers[i.getRt()] = registers[i.getRs()] + i.getImmediate();
            }
            case MIPSand i -> {
                registers[i.getRd()] = registers[i.getRs()] & data[i.getRt()];
            }
            case MIPSandi i -> {
//                registers[i.getRt()] = registers[i.getRs()] & data[i.getImmediate()];
                registers[i.getRt()] = registers[i.getRs()] & (i.getImmediate() & 0xFFFF);

            }
            case MIPSbeq i -> {
                System.out.println("getRS" + registers[i.getRs()]);
                System.out.println("getRT" + registers[i.getRt()]);
                System.out.println("getOffset" + i.getOffset());
                System.out.println("getPC" + programCounter);
                System.out.println(text[programCounter]);
                if (registers[i.getRs()] == registers[i.getRt()]) {
                    programCounter = (programCounter + i.getOffset());
                    System.out.println();
                    System.out.println("getRS" + registers[i.getRs()]);
                    System.out.println("getRT" + registers[i.getRt()]);
                    System.out.println("getOffset" + i.getOffset());
                    System.out.println("getPC" + programCounter);
                }
            }
            case MIPSbne i -> {
                if (registers[i.getRs()] != registers[i.getRt()]) {
                    programCounter = (programCounter + i.getOffset() + 1);
                }
            }
            case MIPSj i -> {
//                programCounter = (programCounter & 0xF0000000) | (i.getIndex() << 2);
//                programCounter = ((programCounter * 4) + Constants.TEXT_SEG_START);
//                int jumpTarget = i.getIndex();
//                int newPC = Constants.TEXT_SEG_START + (jumpTarget * 4);
//                programCounter = (newPC - Constants.TEXT_SEG_START) / 4;

                int jumpTarget = i.getIndex(); // 26-bit index
                int newPC = (programCounter & 0xF0000000) | (jumpTarget << 2); // Calculate the new PC with upper bits preserved
                programCounter = (newPC - Constants.TEXT_SEG_START) / 4; // Update to instruction index
                System.out.println("jumpTarget: " + jumpTarget );
                System.out.println(("newPC: " + newPC));
                System.out.println("programCounter: " + programCounter);

            }
            case MIPSlui i -> {
                registers[i.getRt()] = i.getImmediate() << 16;
            }
            case MIPSlw i -> {
                int address = registers[i.getOffset()] + i.getBase();


                registers[i.getRt()] = data[address / 4];
            }
            case MIPSor i -> {
                registers[i.getRd()] = registers[i.getRs()] | registers[i.getRt()];
            }
            case MIPSori i -> {
                registers[i.getRt()] = registers[i.getRs()] | i.getImmediate();
            }
            case MIPSslt i -> {
                if (registers[i.getRs()] < registers[i.getRt()]) {
                    registers[i.getRd()] = 1;
                } else {
                    registers[i.getRd()] = 0;
                }
            }
            case MIPSsub i -> {
                registers[i.getRd()] = registers[i.getRs()] - registers[i.getRt()];
            }
            case MIPSsw i -> {
                int address = registers[i.getOffset()] + i.getBase();

                data[address] = registers[i.getRt()];
            }
            case MIPSsyscall i -> {
                int syscallCode = registers[2];



                switch (syscallCode) {
                    case 1:
                        System.out.println(registers[4]);
                        break;

                    case 4:

                        int index = registers[4] - Constants.DATA_SEG_START;

                        StringBuilder sb = new StringBuilder();
                        boolean nullFound = false;
                        while (index < data.length) {
                            System.out.println("WE IN HERE");
                            String hexString = Integer.toHexString(data[index]);
                            if (hexString.contains("00")) {
                                hexString = hexString.substring(hexString.lastIndexOf("00"));
                                nullFound = true;
                            }
                            StringBuilder str = new StringBuilder(hexString);
                            for (int j = 2; j <= hexString.length(); j+=2) {
                                sb.append(str, str.length() - j, str.length() - j + 2);
                            }


                            if (nullFound) {
                                break;
                            }
                            index++;
                        }


                        StringBuilder ascii = new StringBuilder();
                        for (int j = 0; j < sb.length(); j += 2) {
                            String hexByte = sb.substring(j, j + 2);
                            int intValue = Integer.parseInt(hexByte, 16);
                            ascii.append((char) intValue);
                        }

                        System.out.println(ascii);

                        break;




                    case 5:
                        Scanner scanner = new Scanner(System.in);
                        int input = scanner.nextInt();
                        registers[2] = input;

                        break;

                    case 10:
                        System.out.println("-- program is finished running --");
                        System.exit(0);
                        break;


                    default:
                        System.out.println("Unknown syscall: " + syscallCode);
                }
            }
            default -> throw new IllegalStateException("Unexpected value: ");
        }
    }

    private void display() {
//        "╔" "║" "╦" "═" "╗" "╬" "╠" "╣" "╝"
        System.out.printf("╔%-98s╗", String.valueOf('═').repeat(98));
        System.out.printf("╔%-23s╗", String.valueOf('═').repeat(23));
        System.out.printf("╔%-51s╗%n", String.valueOf('═').repeat(51));

        System.out.printf("║%-44s%-54s║", "", "Text Segment");
        System.out.printf("║%-4s%-19s║", "", "Program Counter");
        System.out.printf("║%-22s%-29s║%n", "", "Registers");

        System.out.printf("╠%-12s╦%-12s╦%-72s╣", String.valueOf('═').repeat(12), String.valueOf('═').repeat(12), String.valueOf('═').repeat(72));
        System.out.printf("╠%-23s╣", String.valueOf('═').repeat(23));
        System.out.printf("╠%-12s╦%-12s╦%-12s╦%-12s╣%n", String.valueOf('═').repeat(12), String.valueOf('═').repeat(12), String.valueOf('═').repeat(12), String.valueOf('═').repeat(12));

        System.out.printf("║ %-10s ║ %-10s ║ %-70s ║", "Address", "Code", "Basic");
        System.out.printf("║       %-10s      ║", "");
        System.out.printf("║ %-11s║ %-10s ║ %-11s║ %-10s ║%n", "Register", "Value", "Register", "Value");

        int count = 0;
        int address = Constants.TEXT_SEG_START;
        for (int i = 0; i < text.length; i++) {
            System.out.printf("╠%-12s╬%-12s╬%-72s╣", String.valueOf('═').repeat(12), String.valueOf('═').repeat(12), String.valueOf('═').repeat(72));
            System.out.printf("╠%-23s╣", String.valueOf('═').repeat(23));
            System.out.printf("╠%-12s╬%-12s╬%-12s╬%-12s╣%n", String.valueOf('═').repeat(12), String.valueOf('═').repeat(12), String.valueOf('═').repeat(12), String.valueOf('═').repeat(12));

            System.out.printf(
                    "║ 0x%08x ║ 0x%08x ║ %-70s ║",
                    (Constants.TEXT_SEG_START + (i * 4)),
                    i < rawText.length ? rawText[i] : 0,
                    text[i] != null ? text[i].toString() : "null"
            );
            if (Constants.TEXT_SEG_START + (i * 4) == Constants.TEXT_SEG_START + (4 * programCounter)) {
                System.out.print("║       <|--PC--<<      ║");
            } else {
                System.out.print("║                       ║");
            }
            System.out.print("║");
            for (int j = 0; j < 1; ++j) {
                if (count < Constants.REGISTERS.length) {
                    System.out.printf(" %-11s║ 0x%08x ║", Constants.REGISTERS[count], registers[count]);
                    ++count;
                    if (count < Constants.REGISTERS.length) {
                        System.out.printf(" %-11s║ 0x%08x ║%n", Constants.REGISTERS[count], registers[count]);
                        ++count;
                    }
                } else {
                    System.out.println("            ║            ║            ║            ║");
                }

            }
        }

        System.out.printf("╚%-12s╩%-12s╩%-72s╝", String.valueOf('═').repeat(12), String.valueOf('═').repeat(12), String.valueOf('═').repeat(72));
        System.out.printf("╚%-23s╝", String.valueOf('═').repeat(23));
        System.out.printf("╚%-12s╩%-12s╩%-12s╩%-12s╝%n", String.valueOf('═').repeat(12), String.valueOf('═').repeat(12), String.valueOf('═').repeat(12), String.valueOf('═').repeat(12));

        System.out.printf("╔%-176s╗%n", String.valueOf('═').repeat(176));
        System.out.printf("║%-83s%-93s║%n", "", "Text Segment");
        System.out.printf("╠%-24s╦%-18s╦%-18s╦%-18s╦%-18s╦%-18s╦%-18s╦%-18s╦%-18s╣%n", String.valueOf('═').repeat(24), String.valueOf('═').repeat(18), String.valueOf('═').repeat(18), String.valueOf('═').repeat(18), String.valueOf('═').repeat(18), String.valueOf('═').repeat(18), String.valueOf('═').repeat(18), String.valueOf('═').repeat(18), String.valueOf('═').repeat(18));
        System.out.printf("║%-24s║%-18s║%-18s║%-18s║%-18s║%-18s║%-18s║%-18s║%-18s║%n", "Address", "Value(+0)", "Value(+4)", "Value(+8)", "Value(+c)", "Value(+10)", "Value(+14)", "Value(+18)", "Value(+1c)");

        count = 0;
        for (int i = 0; i < 16; i++) {
            System.out.printf("╠%-24s╬%-18s╬%-18s╬%-18s╬%-18s╬%-18s╬%-18s╬%-18s╬%-18s╣%n", String.valueOf('═').repeat(24), String.valueOf('═').repeat(18), String.valueOf('═').repeat(18), String.valueOf('═').repeat(18), String.valueOf('═').repeat(18), String.valueOf('═').repeat(18), String.valueOf('═').repeat(18), String.valueOf('═').repeat(18), String.valueOf('═').repeat(18));
            System.out.printf("║       0x%08x       ║",
                    (Constants.DATA_SEG_START + (i * 4))
            );

            for (int j = 0; j < 8; j++) {
                System.out.printf("    0x%08x    ║", (count < data.length) ? data[count] : 0);
                count++;
            }
            System.out.println();

            if (count >= data.length) {
                break;
            }
        }
        System.out.printf("╚%-24s╩%-18s╩%-18s╩%-18s╩%-18s╩%-18s╩%-18s╩%-18s╩%-18s╝%n", String.valueOf('═').repeat(24), String.valueOf('═').repeat(18), String.valueOf('═').repeat(18), String.valueOf('═').repeat(18), String.valueOf('═').repeat(18), String.valueOf('═').repeat(18), String.valueOf('═').repeat(18), String.valueOf('═').repeat(18), String.valueOf('═').repeat(18));
    }



}


