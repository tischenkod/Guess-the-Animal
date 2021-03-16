package animals.resources;

import animals.*;

import java.time.LocalTime;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.lang.String;

public class App_en extends ListResourceBundle {
    final Set<String> yes = Set.of("y", "yes", "yeah", "yep", "sure", "right", "affirmative", "correct", "indeed",
            "you bet", "exactly", "you said it");
    final Set<String> no = Set.of("n", "no", "no way", "nah", "nope", "negative", "i don't think so", "yeah no");
    final String[] clarify =  new String[]{"I'm not sure I caught you: was it yes or no?",
            "Funny, I still don't understand, is it yes or no?",
            "Oh, it's too complicated for me: just tell me yes or no.",
            "Could you please simply say yes or no?",
            "Oh, no, don't try to confuse me: say yes or no."};
    final String[] bye = new String[]{
            "Bye",
            "Bye, bye!",
            "See you the next time.",
            "See you next time.",
            "See you later.",
            "See you soon!",
            "Talk to you later",
            "See ya!",
            "Catch you later.",
            "Have a nice one!",
            "Have a nice day!"};

    Scanner scanner = new Scanner(System.in);
    Random random = new Random();

    boolean getYesNo() {
        while (true) {
            String answer = null;
            while (answer == null || answer.length() == 0) {
                answer = scanner.nextLine()
                        .trim()
                        .toLowerCase(Locale.ROOT);
            }
            if (answer.charAt(answer.length() - 1) == '.' || answer.charAt(answer.length() - 1) == '!') {
                answer = answer.substring(0, answer.length() - 1).trim();
            }
            if (yes.contains(answer)) {
                return true;
            } else if (no.contains(answer)) {
                return false;
            } else {
                System.out.println(clarify[random.nextInt(clarify.length)]);
            }
        }
    }

    Verb getVerb(String name) {
        switch (name) {
            case "can": return new Verb(name, "can't", "Can it");
//            case "have": return new Verb(name, "doesn't have", "Does it have");
            case "is": return new Verb(name, "isn't", "Is it");
            default: return new Verb(name, "doesn't " + name, "Does it " + name);
        }
    };

    @Override
    protected Object[][] getContents() {
        return new Object[][]{
                {"hello", (Supplier<String>) () -> {
                    LocalTime time = LocalTime.now();
                    String greetingMsg;
                    if (!time.isBefore(LocalTime.of(0, 0)) && time.isBefore(LocalTime.of(4, 0))) {
                        greetingMsg = "Hi, Night Owl\n";
                    } else if (!time.isBefore(LocalTime.of(4, 0)) && time.isBefore(LocalTime.of(5, 0))) {
                        greetingMsg = "Hi, Early Bird\n";
                    } else if (!time.isBefore(LocalTime.of(5, 0)) && time.isBefore(LocalTime.of(12, 0))) {
                        greetingMsg = "Good morning\n";
                    } else if (!time.isBefore(LocalTime.of(12, 0)) && time.isBefore(LocalTime.of(18, 0))) {
                        greetingMsg = "Good afternoon\n";
                    } else {
                        greetingMsg = "Good evening\n";
                    }
                    return greetingMsg;
                }},
                {"menu", "\nWelcome to the animal expert system!\n" +
                "\n" +
                "What do you want to do:\n" +
                "\n" +
                "1. Play the guessing game\n" +
                "2. List of all animals\n" +
                "3. Search for an animal\n" +
                "4. Calculate statistics\n" +
                "5. Print the Knowledge Tree\n" +
                "0. Exit"},
                {"animal.question", (UnaryOperator<String>) animal -> "Is it " + animal + "?"},
                {"bye", (Supplier<String>) () -> bye[random.nextInt(bye.length)]},
                {"rules", "You think of an animal, and I guess it."},
                {"press_enter", "Press enter when you're ready."},
                {"split", (Function<String, String[]>) name -> {
                    Set<Character> vowels = Set.of('a' ,'e' ,'i' ,'o' ,'u' ,'y');
                    name = name.trim();
                    if (name.matches("^(a|an) [-a-zA-Z]+")) {
                        return name.split(" ", 2);
                    } else {
                        return new String[]{vowels.contains(name.charAt(0))?"an":"a", name};
                    }
                }},
                {"animalQuestionText", new String[]{"I want to learn about animals.\n" +
                        "Which animal do you like most?",
                        "I give up. What animal do you have in mind?"}},
                {"newStatement", (BiFunction<AnimalTreeNode, AnimalTreeNode, StatementTreeNode>) (first, second) -> {
                    String fact = null;
                    Verb verb = null;
                    while (fact == null) {
                        System.out.printf("Specify a fact that distinguishes %s from %s.\n" +
                                "The sentence should be of the format: 'It can/has/is ...'.\n", first.getFullName(), second.getFullName());
                        fact = scanner.nextLine().trim().toLowerCase();
                        if (fact.matches("it \\w+ .+")) {
                            String[] parts = fact.split(" ", 3);
                            verb = getVerb(parts[1]);
                            fact = parts[2];
                        } else {
                            fact = null;
                            System.out.println("The examples of a statement:\n" +
                                    " - It can fly\n" +
                                    " - It has horn\n" +
                                    " - It is a mammal");
                        }
                    }
                    Set<Character> endSign = Set.of('.', '!', '?');
                    if (endSign.contains(fact.charAt(fact.length() - 1))) {
                        fact = fact.substring(0, fact.length() - 1).trim();
                    }
                    System.out.printf("Is the statement correct for %s?%n", second.getFullName());

                    StatementTreeNode newNode;
                    if (getYesNo()) {
                        newNode = new StatementTreeNode(second, first, verb, fact);
                    } else {
                        newNode = new StatementTreeNode(first, second, verb, fact);
                    }
                    System.out.println(newNode.yesFactStr("The"));
                    System.out.println(newNode.noFactStr("The"));
                    System.out.println("Wonderful! I've learned so much about animals!\n");
                    return newNode;
                }},
                {"getYesNo", (Supplier<Boolean>) this::getYesNo},
                {"isIt", "Is it %s?%n"},
                {"playAgain", "Would you like to play again?"},
                {"enterTheAnimal", "Enter the animal:"},
                {"factsAbout", "Facts about the %s:%n"},
                {"it", "It"},
                {"stats", "The Knowledge Tree stats%n%n" +
                        "- root node                    %s%n" +
                        "- total number of nodes        %d%n" +
                        "- total number of animals      %d%n" +
                        "- total number of statements   %d%n" +
                        "- height of the tree           %d%n" +
                        "- minimum animal's depth       %d%n" +
                        "- average animal's depth       %.1f%n"}
        };
    }
}