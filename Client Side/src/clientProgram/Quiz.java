//นายจุตินภัส คลังเจริญกุล 5810400973

package clientProgram;

public class Quiz {

    private String question;
    private boolean answer;

    public Quiz(String question, boolean answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public boolean getAnswer() {
        return answer;
    }

}
