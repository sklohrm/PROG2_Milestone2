package Instructions;

import constants.Constants;

public class MIPSj extends MIPSInstruction {

    public static final String MNEMONIC = "j";

    public static final int OPCODE = 2;

    private int index;

    MIPSj() {}

    public MIPSj(int hexCode) {
        this.index = hexCode & 0x3FFFFFF;
    }

    public MIPSj(String hexString) {
        this(Integer.parseInt(hexString, 16));
    }

    @Override
    public String toHex(String[] instruction) {

        int index = Integer.decode(instruction[1]);

        int inst = 0;

        inst |= (index & 0x3FFFFFF);
        inst |= (OPCODE & 0x3F) << 26;

        return String.format("%08x", inst);
    }

    @Override
    public String toString() {
        String output = Constants.J_TEMPLATE;
        output = output.replaceFirst("mnemonic", MNEMONIC);
        output = output.replaceFirst("XX", String.format("%02x", OPCODE));
        output = output.replaceFirst("XXXXXXX", String.format("%07x", index));
        return output;
    }
}