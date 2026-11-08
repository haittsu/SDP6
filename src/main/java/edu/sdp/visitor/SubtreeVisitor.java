package edu.sdp.visitor;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

public abstract class SubtreeVisitor<R> implements Visitor {
    private Position current;

    public final R run(Element root) {
        Objects.requireNonNull(root, "root");
        if (current != null) {
            throw new IllegalStateException("A visitor cannot start a run during traversal");
        }

        reset();
        Deque<Position> pending = new ArrayDeque<>();
        pending.push(new Position(root, null, 0));
        try {
            while (!pending.isEmpty()) {
                current = pending.pop();
                Element element = current.element();
                element.accept(this);
                List<? extends Element> children = element.children();
                for (int i = children.size() - 1; i >= 0; i--) {
                    pending.push(new Position(children.get(i), current, current.depth() + 1));
                }
            }
            return result();
        } finally {
            current = null;
        }
    }

    protected abstract void reset();

    protected abstract R result();

    protected final int depth() {
        return current.depth();
    }

    protected final String relativePath() {
        Deque<String> segments = new ArrayDeque<>();
        for (Position position = current; position.parent() != null; position = position.parent()) {
            segments.addFirst(position.element().name());
        }
        return String.join("/", segments);
    }

    private record Position(Element element, Position parent, int depth) {}
}
