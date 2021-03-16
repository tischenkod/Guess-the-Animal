package animals;

import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;

public class StatementTreeNode extends BaseTreeNode {
    private BaseTreeNode yesNode;
    private BaseTreeNode noNode;

    private String fact = null;
    private Verb verb;

    public String getFact() {
        return fact;
    }

    public void setFact(String fact) {
        this.fact = fact;
    }

    public Verb getVerb() {
        return verb;
    }

    public String question() {
        return format("%s %s?", verb.question(), fact);
    }

    @Override
    public String toString() {
        return toString(false);
    }

    public String toString(boolean negate) {
        return format("%s %s %s.", Language.it, negate ? verb.negate() : verb.toString(), fact);
    }

    public String yesFactStr(String article) {
        if (yesNode == null || yesNode instanceof StatementTreeNode) {
            return null;
        }
        return format("%s %s %s %s.", article, ((AnimalTreeNode)yesNode).getName(), verb.toString(), fact);
    }

    public String noFactStr(String article) {
        if (yesNode == null || yesNode instanceof StatementTreeNode) {
            return null;
        }
        return format("%s %s %s %s.", article, ((AnimalTreeNode)noNode).getName(), verb.negate(), fact);
    }


    public StatementTreeNode(AnimalTreeNode first, AnimalTreeNode second, Verb verb, String fact) {
        this.yesNode = first;
        this.noNode = second;
        this.verb = verb;
        this.fact = fact;
    }

    public StatementTreeNode() {
    }

    public BaseTreeNode getYesNode() {
        return yesNode;
    }

    public void setYesNode(BaseTreeNode yesNode) {
        this.yesNode = yesNode;
    }

    public BaseTreeNode getNoNode() {
        return noNode;
    }

    public void setNoNode(BaseTreeNode noNode) {
        this.noNode = noNode;
    }

    public void setYes(AnimalTreeNode node) {
        yesNode = node;
    }

    public void setNo(AnimalTreeNode node) {
        noNode = node;
    }

    public void replace(BaseTreeNode oldNode, StatementTreeNode newNode) {
        if (yesNode == oldNode) {
            yesNode = newNode;
        } else if (noNode == oldNode) {
            noNode = newNode;
        } else {
            throw new InvalidParameterException(format("%s is not a child for %s", oldNode, question()));
        }
    }

    @Override
    public void listAnimals(Set<String> animals) {
        if (yesNode != null) {
            yesNode.listAnimals(animals);
        }

        if (noNode != null) {
            noNode.listAnimals(animals);
        }
    }

    @Override
    public List<String> search(String animalName) {
        List<String> factList = new LinkedList<>();

        if (yesNode != null) {
            factList = yesNode.search(animalName);
            if (factList != null) {
                factList.add(toString(false));
            }
        }

        if (factList == null && noNode != null) {
            factList = noNode.search(animalName);
            if (factList != null) {
                factList.add(toString(true));
            }
        }

        return factList;
    }

    @Override
    public void updateStatistics(TreeStatistics stats, int curDepth) {
        super.updateStatistics(stats, curDepth);
        stats.statementNodeCount++;
        if (yesNode != null) {
            yesNode.updateStatistics(stats, curDepth + 1);
        }

        if (noNode != null) {
            noNode.updateStatistics(stats, curDepth + 1);
        }
    }

    @Override
    public void print(int depth) {
        super.print(depth);
        System.out.println(question());
        if (yesNode != null) {
            yesNode.print(depth + 1);
        }

        if (noNode != null) {
            noNode.print(depth + 1);
        }
    }
}
