package animals;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class Game {
    private static Game instance = null;
    ResourceBundle resourceBundle;
    public Scanner scanner = new Scanner(System.in);
    FileType fileType;
    BinaryTree tree;

    Function<String, String[]> splitFunc;
    BiFunction<AnimalTreeNode, AnimalTreeNode, StatementTreeNode> newStatementFunc;
    Supplier<Boolean> getYesNoFunc;

    @SuppressWarnings("unchecked")
    public Game(FileType fileType) throws InstanceAlreadyExistsException {
        if (instance != null) {
            throw new InstanceAlreadyExistsException();
        }
        instance = this;
        this.fileType = fileType;
        resourceBundle = ResourceBundle.getBundle("animals.resources.App", Locale.getDefault());
        splitFunc = (Function<String, String[]>) resourceBundle.getObject("split");
        newStatementFunc = (BiFunction<AnimalTreeNode, AnimalTreeNode, StatementTreeNode>) resourceBundle.getObject("newStatement");
        getYesNoFunc = (Supplier<Boolean>) resourceBundle.getObject("getYesNo");
        Language.it = getMessage("it");
        Language.stats = getMessage("stats");

        tree = new BinaryTree(null);

        System.out.println(getMessage("hello"));

        tree.load(fileType, Locale.getDefault().getLanguage());
    }

    public static Game getInstance() throws InstanceNotFoundException {
        if (instance == null) {
            throw new InstanceNotFoundException();
        }
        return instance;
    }

    String askForAnimal() {
        String name = null;
        System.out.println(resourceBundle.getStringArray("animalQuestionText")[AnimalTreeNode.alreadyCreated == 0 ? 0 : 1]);
        Scanner scanner = new Scanner(System.in);
        while (name == null || name.length() == 0) {
            name = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
        }
        return name;
    }

    boolean getYesNo() {
        return getYesNoFunc.get();
    }

    public void play(){
        do {
            System.out.println(getMessage("rules"));
            System.out.println(getMessage("press_enter"));
            scanner.nextLine();

            BaseTreeNode parentNode = null;
            BaseTreeNode node = tree.root;
            while (node instanceof StatementTreeNode) {
                System.out.println(((StatementTreeNode) node).question());
                parentNode = node;
                node = getYesNo() ? ((StatementTreeNode) node).getYesNode() : ((StatementTreeNode) node).getNoNode();
            }

            System.out.printf(getMessage("isIt"), ((AnimalTreeNode) node).getFullName());
            if (getYesNo()) {
                System.out.println("Yahoo!!!!");
            } else {
                AnimalTreeNode newAnimalNode = new AnimalTreeNode(askForAnimal());

                StatementTreeNode newStatementNode = newStatementFunc.apply((AnimalTreeNode) node, newAnimalNode);

                        tree.replaceParent(node, parentNode, newStatementNode);
            }

            System.out.println(getMessage("playAgain"));
        } while (getYesNo());

    }

    public void run() throws InstanceNotFoundException {
        if (tree.root == null) {
            tree.replaceParent(null, null, new AnimalTreeNode(askForAnimal()));
        }
        while (true) {
            switch (menu()) {
                case "1":
                    play();
                    break;
                case "2":
                    list();
                    break;
                case "3":
                    search();
                    break;
                case "4":
                    statistics();
                    break;
                case "5":
                    print();
                    break;
                case "0":
                    System.out.println(getMessage("bye"));
                    tree.save(fileType, Locale.getDefault().getLanguage());
                    return;
            }
        }
    }

    private void print() {
        tree.print();
    }

    private void statistics() {
        System.out.println(tree.statistics());
    }

    private void search() {
        System.out.println(getMessage("enterTheAnimal"));
        String animalName = scanner.nextLine();
        animalName = splitName(animalName)[1];
        List<String> facts = tree.search(animalName);
        if (facts == null) {
            System.out.printf("No facts about the %s.%n", animalName);
        } else {
            System.out.printf(getMessage("factsAbout"), animalName);
            Collections.reverse(facts);
            facts.forEach(fact -> System.out.println(" - " + fact));
        }
    }

    public String[] splitName(String animalName) {
        return splitFunc.apply(animalName);
    }

    private void list() {
        tree.animals().forEach(s-> System.out.println("- " + s));
    }

    private String menu() {
        System.out.println(getMessage("menu"));
        return scanner.nextLine();
    }

    String getMessage(String key) {
        Object object = resourceBundle.getObject(key);
        if (object instanceof String) {
            return (String) object;
        }

        if (object instanceof Supplier) {
            //noinspection rawtypes
            return (String) ((Supplier) object).get();
        }

        return null;
    }

}
