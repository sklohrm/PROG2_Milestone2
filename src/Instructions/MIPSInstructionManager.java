package Instructions;

import constants.Constants;

import java.util.HashMap;

public class MIPSInstructionManager {

    private static final HashMap<String, MIPSInstruction> MIPS_INSTRUCTION_MAP = new HashMap<>() {{
        put("add", new MIPSadd());
        put("addiu", new MIPSaddiu());
        put("and", new MIPSand());
        put("andi", new MIPSandi());
        put("beq", new MIPSbeq());
        put("bne", new MIPSbne());
        put("j", new MIPSj());
        put("lui", new MIPSlui());
        put("lw", new MIPSlw());
        put("or", new MIPSor());
        put("ori", new MIPSori());
        put("slt", new MIPSslt());
        put("sub", new MIPSsub());
        put("sw", new MIPSsw());
        put("syscall", new MIPSsyscall());
    }};

    public static MIPSInstruction createInstruction(int hexCode) {
        String mnemonic;
        //Get opcode
        int opcode = (hexCode >> 26) & 0x3f;
        // If opcode is 0, instruction is r type
        if (opcode == 0) {
            //get fncode
            int fncode = hexCode & 0b111111;
            mnemonic = Constants.R_TYPE_FNCODES.get(fncode);
            if (mnemonic != null) {
                return switch (mnemonic) {
                    case "add" -> new MIPSadd(hexCode);
                    case "and" -> new MIPSand(hexCode);
                    case "or" -> new MIPSor(hexCode);
                    case "slt" -> new MIPSslt(hexCode);
                    case "sub" -> new MIPSsub(hexCode);
                    case "syscall" -> new MIPSsyscall(hexCode);
                    default -> throw new IllegalArgumentException("Invalid MIPS instruction" + hexCode);
                };
            } else {
                throw new IllegalArgumentException("Invalid MIPS instruction" + hexCode);
            }
        //if not r type check i type
        } else {
            mnemonic = Constants.I_TYPE_OPCODES.get(opcode);
            if (mnemonic != null) {
                return switch (mnemonic) {
                    case "addiu" -> new MIPSaddiu(hexCode);
                    case "andi" -> new MIPSandi(hexCode);
                    case "beq" -> new MIPSbeq(hexCode);
                    case "bne" -> new MIPSbne(hexCode);
                    case "lui" -> new MIPSlui(hexCode);
                    case "lw" -> new MIPSlw(hexCode);
                    case "ori" -> new MIPSori(hexCode);
                    case "sw" -> new MIPSsw(hexCode);
                    default -> throw new IllegalArgumentException("Invalid MIPS instruction" + hexCode);
                };
            } else {
                //if null check j type
                mnemonic = Constants.J_TYPE_OPCODES.get(opcode);
                if (mnemonic != null) {
                    // switch not if else for extending later if necessary
                    return switch (mnemonic) {
                        case "j" -> new MIPSj(hexCode);
                        default -> throw new IllegalArgumentException("Invalid MIPS instruction" + hexCode);
                    };
                } else {
                    //if not r, i or j, throw error
                    throw new IllegalArgumentException("Invalid MIPS instruction" + hexCode);
                }
            }
        }
    }

    public static String toString(int hexCode) {
        return MIPSInstructionManager.createInstruction(hexCode).toString();
    }

    public static String toHex(String[] instruction) {
        return MIPSInstructionManager.MIPS_INSTRUCTION_MAP.get(instruction[0]).toHex(instruction);
    }


}
