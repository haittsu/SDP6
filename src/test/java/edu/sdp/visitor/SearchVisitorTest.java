package edu.sdp.visitor;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SearchVisitorTest {
    @Test
    void combinesTheFilenamePredicateAndInclusiveMinimumSize() {
        DirectoryNode root = new DirectoryNode("root");
        root.add(new FileNode("accepted.csv", 40));
        root.add(new FileNode("too-small.csv", 39));
        root.add(new FileNode("wrong-extension.tsv", 900));
        root.add(new DirectoryNode("folder.csv"));

        SearchVisitor visitor = new SearchVisitor(name -> name.endsWith(".csv"), 40);

        assertEquals(List.of("accepted.csv"), visitor.run(root));
    }

    @Test
    void sortsPathsRelativeToTheSelectedRootRegardlessOfInsertionOrder() {
        DirectoryNode root = new DirectoryNode("workspace");
        DirectoryNode selected = new DirectoryNode("selected");
        DirectoryNode nested = new DirectoryNode("alpha");
        root.add(selected);
        selected.add(new FileNode("zeta.log", 10));
        selected.add(nested);
        nested.add(new FileNode("zeta.log", 20));
        nested.add(new FileNode("delta.log", 30));

        SearchVisitor visitor = new SearchVisitor(name -> name.startsWith("z"), 0);

        assertEquals(List.of("alpha/zeta.log", "zeta.log"), visitor.run(selected));
        assertEquals(List.of("selected/alpha/zeta.log", "selected/zeta.log"), visitor.run(root));
    }

    @Test
    void resetsMatchesAndReturnsAnIndependentResultOnEveryRun() {
        FileNode match = new FileNode("keep.txt", 0);
        SearchVisitor visitor = new SearchVisitor("keep.txt"::equals, 0);

        List<String> firstResult = visitor.run(match);
        assertEquals(List.of(), visitor.run(new FileNode("other.txt", 80)));
        assertEquals(List.of(), visitor.run(new DirectoryNode("empty")));
        assertEquals(List.of("keep.txt"), visitor.run(match));
        assertEquals(List.of("keep.txt"), firstResult);
        assertThrows(UnsupportedOperationException.class, () -> firstResult.add("extra.txt"));
    }

    @Test
    void canRunAgainAfterTheFilenamePredicateFailsPartwayThrough() {
        DirectoryNode root = new DirectoryNode("root");
        FileNode match = new FileNode("keep.log", 10);
        root.add(match);
        root.add(new FileNode("fail.log", 20));
        SearchVisitor visitor = new SearchVisitor(name -> {
            if (name.equals("fail.log")) {
                throw new IllegalStateException("Predicate failed");
            }
            return true;
        }, 0);

        assertThrows(IllegalStateException.class, () -> visitor.run(root));
        assertEquals(List.of("keep.log"), visitor.run(match));
    }
}
