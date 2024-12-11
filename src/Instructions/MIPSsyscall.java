package Instructions;

import constants.Constants;

public class MIPSsyscall extends MIPSInstruction {

    public static final String MNEMONIC = "syscall";

    public static final int OPCODE = 0;
    public static final int FNCODE = 12;

    MIPSsyscall() {}

    public MIPSsyscall(int hexCode) {}

    public MIPSsyscall(String hexString) {
        this(Integer.parseInt(hexString, 16));
    }

    @Override
    public String toHex(String[] instruction) {

        int inst = 0;

        inst |= (FNCODE & 0x3F);
        inst |= (OPCODE & 0x3F) << 26;

        return String.format("%08x", inst);
    }

    @Override
    public String toString() {
        String output = Constants.SYS_TEMPLATE;
        output = output.replaceFirst("mnemonic", MNEMONIC);
        output = output.replaceFirst("XX", String.format("%02x", OPCODE));
        output = output.replaceFirst("XX", String.format("%02x", FNCODE));
        return output;
    }
}