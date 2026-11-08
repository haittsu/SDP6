package edu.sdp.visitor;

public final class Main {
    private Main() {}

    public static void main(String[] args) {
        DirectoryNode workspace = exampleTree();
        String suffix = ".md";
        long minimumSize = 1024;

        System.out.println("Total size: " + new TotalSizeVisitor().run(workspace) + " bytes");
        System.out.printf("Files ending in %s with size >= %d bytes:%n", suffix, minimumSize);
        new SearchVisitor(name -> name.endsWith(suffix), minimumSize).run(workspace)
                .forEach(System.out::println);

        StatisticsVisitor.Result stats = new StatisticsVisitor().run(workspace);
        System.out.println("Files: " + stats.fileCount());
        System.out.println("Directories: " + stats.directoryCount());
        System.out.println("Maximum depth: " + stats.maxDepth());
    }

    private static DirectoryNode exampleTree() {
        DirectoryNode workspace = new DirectoryNode("workspace");
        DirectoryNode research = new DirectoryNode("research");
        DirectoryNode drafts = new DirectoryNode("drafts");
        DirectoryNode assets = new DirectoryNode("assets");

        workspace.add(research);
        workspace.add(new FileNode("overview.md", 1024));
        workspace.add(assets);
        workspace.add(new DirectoryNode("empty"));

        research.add(new FileNode("notes.md", 3072));
        research.add(drafts);
        drafts.add(new FileNode("sketch.md", 384));
        assets.add(new FileNode("diagram.svg", 4096));
        assets.add(new FileNode("credits.md", 1536));
        return workspace;
    }
}
