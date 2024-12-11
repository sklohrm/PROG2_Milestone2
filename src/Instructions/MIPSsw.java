package Instructions;

import constants.Constants;
import utils.RegisterUtil;

public class MIPSsw extends MIPSInstruction {

    public static final String MNEMONIC = "sw";

    public static final int OPCODE = 43;

    private int rt, offset, base;

    MIPSsw() {}

    public MIPSsw(int hexCode) {
        this.rt = (hexCode >> 16) & 0x1F;
        this.offset = (hexCode & 0xFFFF);
        this.base = (hexCode >> 21) & 0x1F;
    }

    public MIPSsw(String hexString) {
        this(Integer.parseInt(hexString, 16));
    }

    @Override
    public String toHex(String[] instruction) {

        boolean offsetPresent = instruction.length == 4;

        int rt = RegisterUtil.toDecimal(instruction[1]);

        int offset = 0;
        int base;

        if (offsetPresent) {
            offset = Integer.decode(instruction[2]);
            base = RegisterUtil.toDecimal(instruction[3]);
        } else {
            base = RegisterUtil.toDecimal(instruction[2]);
        }

        int inst = 0;

        inst |= (offset & 0xFFFF);
        inst |= (rt & 0x1F) << 16;
        inst |= (base & 0x1F) << 21;
        inst |= (OPCODE & 0x3F) << 26;

        return String.format("%08x", inst);
    }

    @Override
    public String toString() {
        String output = Constants.I_TEMPLATE;
        output = output.replaceFirst("mnemonic", MNEMONIC);
        output = output.replaceFirst("XX", String.format("%02x", OPCODE));
        output = output.replaceFirst("XX", String.format("%02x", base));
        output = output.replaceFirst("XX", String.format("%02x", rt));
        output = output.replaceFirst("XXXX", String.format("%04x", offset));
        return output;
    }
}