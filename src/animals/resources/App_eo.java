package animals.resources;

import animals.*;

import java.time.LocalTime;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.lang.String;

public class App_eo extends ListResourceBundle {
    final Set<String> yes = Set.of("j", "jes");
    final Set<String> no = Set.of("n", "ne");
    final String[] clarify =  new String[]{"Mi ne certas, ke mi kaptis vin: ĉu jes aŭ ne?",
            "Amuza, mi ankoraŭ ne komprenas, ĉu jes aŭ ne?"};
    final String[] bye = new String[]{
            "Ĝis!",
            "Ĝis revido!",
            "Estis agrable vidi vin!"};

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
        return new Verb(name, "ne " + name, "Ĉu ĝi " + name);
//        switch (name) {
//            case "povas": return new Verb(name, "ne povas", "Ĉu ĝi povas");
//            case "havas": return new Verb(name, "ne havas", "Ĉu ĝi havas");
//            case "estas": return new Verb(name, "ne estas", "Ĉu ĝi estas");
//            default: return null;
//        }
    };

    @Override
    protected Object[][] getContents() {
        return new Object[][]{
                {"hello", (Supplier<String>) () -> {
                    LocalTime time = LocalTime.now();
                    String greetingMsg;
                    if (!time.isBefore(LocalTime.of(0, 0)) && time.isBefore(LocalTime.of(4, 0))) {
                        greetingMsg = "Saluton, Nokta Strigo\n";
                    } else if (!time.isBefore(LocalTime.of(4, 0)) && time.isBefore(LocalTime.of(5, 0))) {
                        greetingMsg = "Saluton, Frua Birdo\n";
                    } else if (!time.isBefore(LocalTime.of(5, 0)) && time.isBefore(LocalTime.of(12, 0))) {
                        greetingMsg = "Bonan matenon\n";
                    } else if (!time.isBefore(LocalTime.of(12, 0)) && time.isBefore(LocalTime.of(18, 0))) {
                        greetingMsg = "Bonan posttagmezon\n";
                    } else {
                        greetingMsg = "Bonan vesperon\n";
                    }
                    return greetingMsg;
                }},
                {"menu", "\nBonvenon al la sperta sistemo de la besto!\n" +
                        "\n" +
                        "Kion vi volas fari:\n" +
                        "\n" +
                        "1. Ludi la konjektas ludo\n" +
                        "2. Listo de ĉiuj bestoj\n" +
                        "3. Serĉi besto\n" +
                        "4. Kalkuli statistikoj\n" +
                        "5. Presi la Scio Arbo\n" +
                        "0. Eliri"},
                {"animal.question", (UnaryOperator<String>) animal -> "Ĉu ĝi estas " + animal + "?"},
                {"bye", (Supplier<String>) () -> bye[random.nextInt(bye.length)]},
                {"rules", "Vi pensu pri besto, kaj mi divenos ĝin."},
                {"press_enter", "Premu enen kiam vi pretas."},
                {"split", (Function<String, String[]>) name -> new String[]{"", name.trim()}},
                {"animalQuestionText", new String[]{"Mi volas lerni pri bestoj.\n" +
                        "kiun beston vi plej ŝatas?",
                        "Mi rezignas. Kiun beston vi havas en la kapo?"}},
                {"newStatement", (BiFunction<AnimalTreeNode, AnimalTreeNode, StatementTreeNode>) (first, second) -> {
                    String fact = null;
                    Verb verb = null;
                    while (fact == null) {
                        System.out.printf("Indiku fakton, kiu distingas %s de %s.\n" +
                                "The sentence should be of the format: 'Ĝi povas/havas/estas ...'.\n", first.getFullName(), second.getFullName());
                        fact = scanner.nextLine().trim().toLowerCase();
                        if (fact.matches("ĝi .+ .+")) {
                            String[] parts = fact.split(" ", 3);
                            verb = getVerb(parts[1]);
                            fact = parts[2];
                        } else {
                            fact = null;
                            System.out.println("La ekzemploj de aserto:\n" +
                                    " - Ĝi povas flugi\n" +
                                    " - Ĝi havas kornon\n" +
                                    " - Ĝi estas mamulo");
                        }
                    }
                    Set<Character> endSign = Set.of('.', '!', '?');
                    if (endSign.contains(fact.charAt(fact.length() - 1))) {
                        fact = fact.substring(0, fact.length() - 1).trim();
                    }
                    System.out.printf("Ĉu la aserto ĝustas por la %s?%n", second.getFullName());

                    StatementTreeNode newNode;
                    if (getYesNo()) {
                        newNode = new StatementTreeNode(second, first, verb, fact);
                    } else {
                        newNode = new StatementTreeNode(first, second, verb, fact);
                    }
                    System.out.println(newNode.yesFactStr("La"));
                    System.out.println(newNode.noFactStr("La"));
                    System.out.println("Mirinde! Mi lernis tiom multe pri bestoj!\n");
                    return newNode;
                }},
                {"getYesNo", (Supplier<Boolean>) this::getYesNo},
                {"isIt", "Ĉu ĝi estas %s?%n"},
                {"playAgain", "Ĉu vi ŝatus ludi denove?"},
                {"enterTheAnimal", "Enigu la beston:"},
                {"factsAbout", "Faktoj pri la %s:%n"},
                {"it", "Ĝi"},
                {"stats", "la statistiko%n%n" +
                        "- radika nodo              %s%n" +
                        "- tuta nombro de nodoj     %d%n" +
                        "- tuta nombro de bestoj    %d%n" +
                        "- tuta nombro de deklaroj       %d%n" +
                        "- alteco de la arbo        %d%n" +
                        "- minimuma profundo   %d%n" +
                        "- averaĝa profundo    %.1f%n"}
        };
    }
}