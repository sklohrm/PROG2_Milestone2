package Instructions;

import constants.Constants;
import utils.RegisterUtil;

public class MIPSsub extends MIPSInstruction {

    public static final String MNEMONIC = "sub";

    public static final int OPCODE = 0;
    public static final int FNCODE = 34;

    private int rd, rs, rt;

    MIPSsub() {}

    public MIPSsub(int hexCode) {
        this.rs = (hexCode >> 21) & 0x1F;
        this.rt = (hexCode >> 16) & 0x1F;
        this.rd = (hexCode >> 11) & 0x1F;
    }

    public MIPSsub(String hexString) {
        this(Integer.parseInt(hexString, 16));
    }

    @Override
    public String toHex(String[] instruction) {

        int rd = RegisterUtil.toDecimal(instruction[1]);
        int rs = RegisterUtil.toDecimal(instruction[2]);
        int rt = RegisterUtil.toDecimal(instruction[3]);

        int inst = 0;

        inst |= (FNCODE & 0x3F);
        inst |= (rd & 0x1F) << 11;
        inst |= (rt & 0x1F) << 16;
        inst |= (rs & 0x1F) << 21;
        inst |= (OPCODE & 0x3F) << 26;

        return String.format("%08x", inst);
    }

    @Override
    public String toString() {
        String output = Constants.R_TEMPLATE;
        output = output.replaceFirst("mnemonic", MNEMONIC);
        output = output.replaceFirst("XX", String.format("%02x", OPCODE));
        output = output.replaceFirst("XX", String.format("%02x", rs));
        output = output.replaceFirst("XX", String.format("%02x", rt));
        output = output.replaceFirst("XX", String.format("%02x", rd));
        output = output.replaceFirst("XX", String.format("%02x", 0));
        output = output.replaceFirst("XX", String.format("%02x", FNCODE));
        return output;
    }
}