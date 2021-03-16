package animals;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.management.InstanceNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static java.lang.Math.min;

public class AnimalTreeNode extends BaseTreeNode {
    private String name;
    private String article;

    @JsonIgnore
    public String getFullName() {
        return (article + " " + name).trim();
    }

    public String getName() {
        return name;
    }

    public String getArticle() {
        return article;
    }

    static int alreadyCreated = 0;

    public AnimalTreeNode() {
        alreadyCreated++;
    }

    public AnimalTreeNode(String name) {
        this();
        String[] parts = new String[0];
        try {
            parts = Game.getInstance().splitName(name);
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();
        }
        article = parts[0];
        this.name = parts[1];
    }

    public AnimalTreeNode(String name, String article) {
        this();
        this.name = name;
        this.article = article;
    }

    @Override
    public void listAnimals(Set<String> animals) {
        animals.add(name);
    }

    @Override
    public List<String> search(String animalName) {
        if (animalName.equals(name)) {
            return new LinkedList<>();
        }
        return null;
    }

    @Override
    public String toString() {
        return getFullName();
    }

    @Override
    public void updateStatistics(TreeStatistics stats, int curDepth) {
        super.updateStatistics(stats, curDepth);
        stats.animalNodeCount++;
        if (stats.minDepth == -1) {
            stats.minDepth = curDepth;
        } else {
            stats.minDepth = min(stats.minDepth, curDepth);
        }
        stats.sumDepth += curDepth;
    }

    @Override
    public void print(int depth) {
        super.print(depth);
        System.out.println(getFullName());
    }
}
