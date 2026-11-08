package edu.sdp.visitor;

import java.util.List;

public sealed interface Element permits FileSystemNode {
    String name();

    void accept(Visitor visitor);

    default List<? extends Element> children() {
        return List.of();
    }
}
