package Instructions;

import constants.Constants;
import utils.RegisterUtil;

public class MIPSbeq extends MIPSInstruction {

    public static final String MNEMONIC = "beq";

    public static final int OPCODE = 4;

    private int rs, rt, offset;

    MIPSbeq() {}

    public MIPSbeq(int hexCode) {
        this.rs = (hexCode >> 21) & 0x1F;
        this.rt = (hexCode >> 16) & 0x1F;
        this.offset = (hexCode & 0xFFFF);
    }

    public MIPSbeq(String hexString) { this(Integer.parseInt(hexString, 16)); }

    public int getRs() { return rs; }

    public int getRt() { return rt; }

    public int getOffset() { return offset; }

    @Override
    public String toHex(String[] instruction) {

        int rs = RegisterUtil.toDecimal(instruction[1]);
        int rt = RegisterUtil.toDecimal(instruction[2]);
        int offset = Integer.decode(instruction[3]);

        int inst = 0;

        inst |= (offset & 0xFFFF);
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
        output = output.replaceFirst("XXXX", String.format("%04x", offset));
        return output;
    }
}
