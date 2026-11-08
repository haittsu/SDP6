package edu.sdp.visitor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StatisticsVisitorTest {
    @Test
    void countsAllNodesAndMeasuresDepthFromTheChosenRoot() {
        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode branch = new DirectoryNode("branch");
        DirectoryNode nested = new DirectoryNode("nested");
        root.add(branch);
        root.add(new FileNode("first.bin", 71));
        root.add(new DirectoryNode("empty"));
        branch.add(nested);
        nested.add(new FileNode("deepest.bin", 88));

        StatisticsVisitor visitor = new StatisticsVisitor();

        assertEquals(new StatisticsVisitor.Result(2, 4, 3), visitor.run(root));
        assertEquals(new StatisticsVisitor.Result(1, 2, 2), visitor.run(branch));
    }

    @Test
    void emptyDirectoryCountsAsOneDirectoryAtDepthZero() {
        DirectoryNode empty = new DirectoryNode("empty");

        assertEquals(new StatisticsVisitor.Result(0, 1, 0), new StatisticsVisitor().run(empty));
    }

    @Test
    void reusingTheVisitorResetsCountsAndDepthForAFileRoot() {
        DirectoryNode directory = new DirectoryNode("directory");
        FileNode file = new FileNode("file.txt", 22);
        directory.add(file);
        StatisticsVisitor visitor = new StatisticsVisitor();

        StatisticsVisitor.Result firstResult = visitor.run(directory);
        assertEquals(new StatisticsVisitor.Result(1, 0, 0), visitor.run(file));
        assertEquals(new StatisticsVisitor.Result(1, 1, 1), firstResult);
        assertEquals(firstResult, visitor.run(directory));
    }

    @Test
    void traversesDeeplyNestedEmptyDirectoriesWithoutRecursion() {
        int maxDepth = 4096;
        DirectoryNode subtree = new DirectoryNode("leaf");
        for (int i = 0; i < maxDepth; i++) {
            DirectoryNode parent = new DirectoryNode("level-" + i);
            parent.add(subtree);
            subtree = parent;
        }

        assertEquals(new StatisticsVisitor.Result(0, maxDepth + 1, maxDepth),
                new StatisticsVisitor().run(subtree));
    }
}
