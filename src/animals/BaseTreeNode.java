package animals;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;
import java.util.Set;

import static java.lang.Math.max;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AnimalTreeNode.class, name = "animal"),
        @JsonSubTypes.Type(value = StatementTreeNode.class, name = "statement")
})
public abstract class BaseTreeNode {
    abstract public void listAnimals(Set<String> animals);

    public abstract List<String> search(String animalName);

    @Override
    public abstract String toString();

    public  void updateStatistics(TreeStatistics stats, int curDepth) {
        stats.nodeCount++;
        stats.height = max(curDepth, stats.height);
    }

    public void print(int depth) {
        System.out.print(" ".repeat(depth));
    }
}
