package edu.sdp.visitor;

import java.util.Objects;

public abstract sealed class FileSystemNode implements Element permits FileNode, DirectoryNode {
    private final String name;
    private DirectoryNode parent;

    FileSystemNode(String name) {
        Objects.requireNonNull(name, "name");
        if (name.isBlank() || name.equals(".") || name.equals("..")
                || name.contains("/") || name.contains("\\") || name.indexOf('\0') >= 0) {
            throw new IllegalArgumentException("Expected a single, non-blank path segment");
        }
        this.name = name;
    }

    @Override
    public final String name() {
        return name;
    }

    final void attachTo(DirectoryNode directory) {
        if (parent != null) {
            throw new IllegalArgumentException("Already attached: " + name);
        }
        for (FileSystemNode ancestor = directory; ancestor != null; ancestor = ancestor.parent) {
            if (ancestor == this) {
                throw new IllegalArgumentException("Adding " + name + " would create a cycle");
            }
        }
        parent = directory;
    }
}
