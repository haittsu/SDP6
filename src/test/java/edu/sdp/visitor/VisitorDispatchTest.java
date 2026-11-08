package edu.sdp.visitor;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VisitorDispatchTest {
    @Test
    void acceptCallsTheMatchingOverloadForEachElement() {
        List<String> calls = new ArrayList<>();
        Visitor recorder = new Visitor() {
            @Override
            public void visit(FileNode file) {
                calls.add("file " + file.name());
            }

            @Override
            public void visit(DirectoryNode directory) {
                calls.add("directory " + directory.name());
            }
        };
        FileNode file = new FileNode("map.txt", 11);
        DirectoryNode directory = new DirectoryNode("fieldwork");
        directory.add(file);

        for (Element element : List.of(file, directory)) {
            element.accept(recorder);
        }

        assertEquals(List.of("file map.txt", "directory fieldwork"), calls);
    }
}
