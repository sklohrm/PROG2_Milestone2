package Instructions;

public abstract class MIPSInstruction {
    public abstract String toHex(String[] instruction);
    @Override
    public abstract String toString();
}
