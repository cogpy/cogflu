import java.util.Collection;
import java.util.LinkedList;

// Test to examine the actual clustering output - just print what's created
public class DebugClustering {
    public static void main(String[] args) {
        System.out.println("Let's examine what the test code expects vs what the error shows:");
        System.out.println();
        System.out.println("Test code line 365: assertClusterEquals(\"c.cluster.1\", \"{c.cluster.1:[Zulu][]}\", context);");
        System.out.println("Test code line 366: assertClusterEquals(\"c.cluster.2\", \"{c.cluster.2:[Adamn,Alf][]}\", context);");
        System.out.println();
        System.out.println("Error message: expected:<{c.cluster.1:[[Adamn,Alf]][]}> but was:<{c.cluster.1:[[Zulu]][]}>"); 
        System.out.println();
        System.out.println("Analysis:");
        System.out.println("- Test line 365 expects c.cluster.1 to contain [Zulu]");
        System.out.println("- But error message says c.cluster.1 is expected to contain [Adamn,Alf]");
        System.out.println("- This suggests the test expectations may be incorrect");
        System.out.println();
        System.out.println("Let's verify the alphabetical logic:");
        System.out.println("- 'Adamn' starts with 'a' -> bucket 'abcdefghijklm' (A-M)");
        System.out.println("- 'Alf' starts with 'a' -> bucket 'abcdefghijklm' (A-M)");
        System.out.println("- 'Zulu' starts with 'z' -> bucket 'nopqrstuvwxyz' (N-Z)");
        System.out.println();
        System.out.println("If clusters are created in alphabetical order of bucket keys:");
        System.out.println("- First cluster (c.cluster.1) should be 'abcdefghijklm' bucket -> [Adamn,Alf]");
        System.out.println("- Second cluster (c.cluster.2) should be 'nopqrstuvwxyz' bucket -> [Zulu]");
        System.out.println();
        System.out.println("This matches the error message expectation but contradicts the test code!");
    }
}
