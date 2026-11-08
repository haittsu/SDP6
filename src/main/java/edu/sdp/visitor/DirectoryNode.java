package edu.sdp.visitor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class DirectoryNode extends FileSystemNode {
    private final Map<String, FileSystemNode> children = new LinkedHashMap<>();

    public DirectoryNode(String name) {
        super(name);
    }

    public void add(FileSystemNode child) {
        Objects.requireNonNull(child, "child");
        if (children.containsKey(child.name())) {
            throw new IllegalArgumentException("Duplicate child name: " + child.name());
        }
        child.attachTo(this);
        children.put(child.name(), child);
    }

    @Override
    public List<FileSystemNode> children() {
        return List.copyOf(children.values());
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
