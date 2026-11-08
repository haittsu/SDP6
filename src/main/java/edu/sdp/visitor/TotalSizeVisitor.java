package edu.sdp.visitor;

import java.math.BigInteger;

public final class TotalSizeVisitor extends SubtreeVisitor<BigInteger> {
    private BigInteger total = BigInteger.ZERO;

    @Override
    public void visit(FileNode file) {
        total = total.add(BigInteger.valueOf(file.size()));
    }

    @Override
    public void visit(DirectoryNode directory) {
        // Only files contribute bytes.
    }

    @Override
    protected void reset() {
        total = BigInteger.ZERO;
    }

    @Override
    protected BigInteger result() {
        return total;
    }
}
