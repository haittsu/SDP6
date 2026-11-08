package edu.sdp.visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public final class SearchVisitor extends SubtreeVisitor<List<String>> {
    private final Predicate<String> filenameMatches;
    private final long minimumSize;
    private final List<String> matches = new ArrayList<>();

    public SearchVisitor(Predicate<String> filenameMatches, long minimumSize) {
        this.filenameMatches = Objects.requireNonNull(filenameMatches, "filenameMatches");
        if (minimumSize < 0) {
            throw new IllegalArgumentException("Minimum size cannot be negative");
        }
        this.minimumSize = minimumSize;
    }

    @Override
    public void visit(FileNode file) {
        if (file.size() >= minimumSize && filenameMatches.test(file.name())) {
            matches.add(depth() == 0 ? file.name() : relativePath());
        }
    }

    @Override
    public void visit(DirectoryNode directory) {
        // The search criterion applies to file names only.
    }

    @Override
    protected void reset() {
        matches.clear();
    }

    @Override
    protected List<String> result() {
        return matches.stream().sorted().toList();
    }
}
