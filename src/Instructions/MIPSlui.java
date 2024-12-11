package Instructions;

import constants.Constants;
import utils.RegisterUtil;

public class MIPSlui extends MIPSInstruction {

    public static final String MNEMONIC = "lui";

    public static final int OPCODE = 15;

    private int rt, immediate;

    MIPSlui() {}

    public MIPSlui(int hexCode) {
        this.rt = (hexCode >> 16) & 0x1F;
        this.immediate = hexCode & 0xFFFF;
    }

    public MIPSlui(String hexString) {
        this(Integer.parseInt(hexString, 16));
    }

    @Override
    public String toHex(String[] instruction) {

        int rt = RegisterUtil.toDecimal(instruction[1]);
        int immediate = Integer.decode(instruction[2]);

        int inst = 0;

        inst |= (immediate & 0xFFFF);
        inst |= (rt & 0x1F) << 16;
        inst |= (OPCODE & 0x3F) << 26;

        return String.format("%08x", inst);
    }

    @Override
    public String toString() {
        String output = Constants.I_TEMPLATE;
        output = output.replaceFirst("mnemonic", MNEMONIC);
        output = output.replaceFirst("XX", String.format("%02x", OPCODE));
        output = output.replaceFirst("XX", String.format("%02x", 0));
        output = output.replaceFirst("XX", String.format("%02x", rt));
        output = output.replaceFirst("XXXX", String.format("%04x", immediate));
        return output;
    }
}