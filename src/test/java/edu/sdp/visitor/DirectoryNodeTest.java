package edu.sdp.visitor;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DirectoryNodeTest {
    @Test
    void rejectsNegativeSizesAndInvalidPathSegments() {
        assertThrows(IllegalArgumentException.class, () -> new FileNode("invalid.dat", -1));
        assertThrows(IllegalArgumentException.class, () -> new FileNode("a/b", 0));
        assertThrows(IllegalArgumentException.class, () -> new DirectoryNode(".."));
    }

    @Test
    void rejectsDuplicateSiblingNamesWithoutAttachingTheRejectedNode() {
        DirectoryNode parent = new DirectoryNode("parent");
        FileNode file = new FileNode("shared", 0);
        DirectoryNode duplicate = new DirectoryNode("shared");
        parent.add(file);

        assertThrows(IllegalArgumentException.class, () -> parent.add(duplicate));

        DirectoryNode other = new DirectoryNode("other");
        other.add(duplicate);
        assertEquals(List.of(file), parent.children());
        assertEquals(List.of(duplicate), other.children());
    }

    @Test
    void rejectsASecondParentAndKeepsTheOriginalOwner() {
        DirectoryNode first = new DirectoryNode("first");
        DirectoryNode second = new DirectoryNode("second");
        FileNode child = new FileNode("child.txt", 18);
        first.add(child);

        assertThrows(IllegalArgumentException.class, () -> second.add(child));
        assertEquals(List.of(child), first.children());
        assertEquals(List.of(), second.children());
    }

    @Test
    void rejectsDirectAndIndirectCyclesWhileAllowingValidNesting() {
        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode levelOne = new DirectoryNode("level-one");
        DirectoryNode levelTwo = new DirectoryNode("level-two");
        root.add(levelOne);
        levelOne.add(levelTwo);

        assertThrows(IllegalArgumentException.class, () -> root.add(root));
        assertThrows(IllegalArgumentException.class, () -> levelTwo.add(root));
        assertEquals(List.of(levelOne), root.children());
        assertEquals(List.of(levelTwo), levelOne.children());
        assertEquals(List.of(), levelTwo.children());
    }

    @Test
    void childrenReturnsAnImmutableSnapshot() {
        DirectoryNode directory = new DirectoryNode("directory");
        List<FileSystemNode> before = directory.children();
        FileNode file = new FileNode("entry.dat", 9);
        directory.add(file);

        assertThrows(UnsupportedOperationException.class, () -> directory.children().remove(0));
        assertEquals(List.of(), before);
        assertEquals(List.of(file), directory.children());
    }
}
