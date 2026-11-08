package edu.sdp.visitor;

public final class StatisticsVisitor extends SubtreeVisitor<StatisticsVisitor.Result> {
    private int fileCount;
    private int directoryCount;
    private int maxDepth;

    @Override
    public void visit(FileNode file) {
        fileCount++;
        recordDepth();
    }

    @Override
    public void visit(DirectoryNode directory) {
        directoryCount++;
        recordDepth();
    }

    @Override
    protected void reset() {
        fileCount = 0;
        directoryCount = 0;
        maxDepth = 0;
    }

    @Override
    protected Result result() {
        return new Result(fileCount, directoryCount, maxDepth);
    }

    private void recordDepth() {
        maxDepth = Math.max(maxDepth, depth());
    }

    public record Result(int fileCount, int directoryCount, int maxDepth) {}
}
