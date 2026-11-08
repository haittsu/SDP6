package edu.sdp.visitor;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TotalSizeVisitorTest {
    @Test
    void sumsEachFileAcrossMultipleLevelsAndSkipsDirectories() {
        DirectoryNode root = new DirectoryNode("lab");
        DirectoryNode samples = new DirectoryNode("samples");
        DirectoryNode archive = new DirectoryNode("archive");
        root.add(samples);
        root.add(new FileNode("readings.dat", 34));
        root.add(new DirectoryNode("empty"));
        samples.add(new FileNode("readings.dat", 260));
        samples.add(archive);
        archive.add(new FileNode("backup.dat", 512));

        TotalSizeVisitor visitor = new TotalSizeVisitor();
        assertEquals(BigInteger.valueOf(806), visitor.run(root));
        assertEquals(BigInteger.valueOf(772), visitor.run(samples));
        assertEquals(BigInteger.valueOf(806), visitor.run(root));
    }

    @Test
    void returnsZeroForAnEmptyDirectoryAfterANonemptyRun() {
        TotalSizeVisitor visitor = new TotalSizeVisitor();

        assertEquals(BigInteger.valueOf(73), visitor.run(new FileNode("signal.dat", 73)));
        assertEquals(BigInteger.ZERO, visitor.run(new DirectoryNode("empty")));
    }

    @Test
    void computesTotalsLargerThanTheLargestSingleFile() {
        DirectoryNode root = new DirectoryNode("root");
        root.add(new FileNode("large.bin", Long.MAX_VALUE));
        root.add(new FileNode("tiny.bin", 1));

        assertEquals(BigInteger.ONE.shiftLeft(63), new TotalSizeVisitor().run(root));
    }
}
