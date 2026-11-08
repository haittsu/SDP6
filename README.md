# Visitor Pattern for an In-Memory File System

Assignment 06 for ShP-2216. The Java application builds a sample file system in memory and runs three independent operations over it to calculate total size, find files, and collect statistics.

## Run the project

Use JDK 17 or newer and Maven 3.9+. Run these commands from the project root.

```sh
mvn test
mvn package
java -jar target/visitor-file-system-1.0.0.jar
```

In IntelliJ IDEA, open the repository as a Maven project using `pom.xml`, then run `edu.sdp.visitor.Main`. The demo takes no input. To try a different search, edit `suffix` or `minimumSize` in `Main`.

## Sample output

```text
Total size: 10112 bytes
Files ending in .md with size >= 1024 bytes:
assets/credits.md
overview.md
research/notes.md
Files: 5
Directories: 5
Maximum depth: 3
```

## Design

- `Element` defines the shared node API. `FileNode` and `DirectoryNode` each call `visitor.visit(this)` in `accept`. The runtime element chooses the `accept` implementation, and the runtime visitor supplies the implementation of the overload selected there.
- `SubtreeVisitor.run` owns traversal. Its stack performs a pre-order depth-first walk, visiting each reachable node once. Individual visitors handle only the current element. A file has no children, and a directory returns a read-only snapshot of its children.
- `DirectoryNode.add` rejects duplicate sibling names and uses `FileSystemNode.attachTo` to reject an existing parent or a cycle before insertion. Names are immutable, case-sensitive path segments. File sizes are immutable, non-negative byte counts.
- `TotalSizeVisitor` sums file sizes exactly using `BigInteger`. `SearchVisitor` applies a filename predicate and an inclusive size threshold. Paths omit the chosen directory root and use `/` between names. The result uses Java's natural string order. For a file used as the root, the path is its filename.
- `StatisticsVisitor` counts files and directories separately, including the root in the appropriate count. The traversal starts the root at depth zero and adds one per parent-child edge. The visitor takes the maximum depth across both element types.
- Call `run(root)` for each operation. Visitors reset their accumulators at the start of a run and can be reused sequentially. Each result is immutable and survives future runs. The tree and visitors are intended for single-threaded use.

## Extending the visitor hierarchy

`StatisticsVisitor` was added in a separate commit using the existing `Visitor` overloads and traversal code. A new operation can define another visitor without changing either node's `accept` method. Adding a new element type would require updating the sealed hierarchy and adding a matching `visit` overload to the interface and to each existing visitor.

## Automated tests

| Test class | Coverage |
| --- | --- |
| `VisitorDispatchTest` | Overload selection for both node types |
| `DirectoryNodeTest` | Tree invariants, valid nesting, and read-only children |
| `TotalSizeVisitorTest` | Nested totals, empty directories, repeat runs, and large totals |
| `SearchVisitorTest` | Combined filters, inclusive boundary, sorted root-relative paths, and reuse |
| `StatisticsVisitorTest` | Counts, depth, empty directories, file roots, and deep nesting |
