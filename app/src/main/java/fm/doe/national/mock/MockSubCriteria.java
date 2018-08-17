package fm.doe.national.mock;

import java.io.Serializable;

public class MockSubCriteria implements Serializable {
    private String question;
    private String hint;
    private State state;

    public MockSubCriteria(String question, String hint, State state) {
        this.question = question;
        this.hint = hint;
        this.state = state;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public enum State {
        NOT_ANSWERED,
        POSITIVE,
        NEGATIVE
    }
}
