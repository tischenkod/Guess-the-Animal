package animals;

import static java.lang.String.format;

public class TreeStatistics {
    public String rootStr;
    int nodeCount;
    int animalNodeCount;
    int statementNodeCount;
    int height;
    int minDepth;
    int sumDepth;

    public TreeStatistics() {
        this.nodeCount = 0;
        this.animalNodeCount = 0;
        this.statementNodeCount = 0;
        this.height = 0;
        this.minDepth = -1;
        this.sumDepth = 0;
    }

    double avgDepth() {
        return  (double) sumDepth / animalNodeCount;
    };

    @Override
    public String toString() {
        return format(Language.stats,
                rootStr,
                nodeCount,
                animalNodeCount,
                statementNodeCount,
                height,
                minDepth,
                avgDepth());
    }
}
