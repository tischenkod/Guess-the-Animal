package animals;

public class Verb{
    String name;
    String negate;
    String question;

    public Verb(String name, String negate, String question) {
        this.name = name;
        this.negate = negate;
        this.question = question;
    }

    public Verb() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNegate() {
        return negate;
    }

    public void setNegate(String negate) {
        this.negate = negate;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String name() {
        return name;
    }

    public String negate() {
        return negate;
    }

    public String question() {
        return question;
    }

    @Override
    public String toString() {
        return name;
    }
}
