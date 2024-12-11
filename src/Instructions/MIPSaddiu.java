package Instructions;

import constants.Constants;
import utils.RegisterUtil;

public class MIPSaddiu extends MIPSInstruction {

    public static final String MNEMONIC = "addiu";

    public static final int OPCODE = 9;

    private int rt, rs, immediate;

    MIPSaddiu(){}

    public MIPSaddiu(int hexCode) {
        this.rs = (hexCode >> 21) & 0x1F;
        this.rt = (hexCode >> 16) & 0x1F;
        this.immediate = (hexCode & 0xFFFF);
    }

    public MIPSaddiu(String hexString) {
        this(Integer.parseInt(hexString, 16));
    }

    @Override
    public String toHex(String[] instruction) {
        int rt = RegisterUtil.toDecimal(instruction[1]);
        int rs = RegisterUtil.toDecimal(instruction[2]);
        int immediate = Integer.decode(instruction[3]);

        int inst = 0;

        inst |= (immediate & 0xFFFF);
        inst |= (rt & 0x1F) << 16;
        inst |= (rs & 0x1F) << 21;
        inst |= (OPCODE & 0x3F) << 26;

        return String.format("%08x", inst);
    }

    @Override
    public String toString() {
        String output = Constants.I_TEMPLATE;
        output = output.replaceFirst("mnemonic", MNEMONIC);
        output = output.replaceFirst("XX", String.format("%02x", OPCODE));
        output = output.replaceFirst("XX", String.format("%02x", rs));
        output = output.replaceFirst("XX", String.format("%02x", rt));
        output = output.replaceFirst("XXXX", String.format("%04x", immediate));
        return output;
    }
}
