//นายจุตินภัส คลังเจริญกุล 5810400973

package clientProgram;

import java.util.*;

public class Model {

    private ArrayList<Quiz> quiz = new ArrayList<>();
    private String currentQuestion;
    private boolean currentAnswer;
    private int currentIndex = 0;
    private int score = 0;

    public Model() {
        quiz.add(new Quiz("There are ten zeros\n in the number one million.", false));
        quiz.add(new Quiz("The square root\n of one hundred is five.", false));
        quiz.add(new Quiz("Water is also called H2O.", true));
        quiz.add(new Quiz("Ice is made out of glass.", false));
        quiz.add(new Quiz("The fifth planet away from the sun\n in the solar system is mars.", false));
        quiz.add(new Quiz("Fish sleep with their eyes closed.", false));
        quiz.add(new Quiz("If you mix all the colors\n of the rainbow together\n you will get white.", false));
        quiz.add(new Quiz("Hawaii is part of the United States.", true));
        quiz.add(new Quiz("Twenty minus thirty plus ten\n equals zero.", true));
        quiz.add(new Quiz("If you are facing the right wall\n of your room and turn ninety degrees right, \nthen one hundred eighty degrees right,\nand finally ninety degrees left\n you are facing the left wall of your room.", false));
        Collections.shuffle(quiz);
    }

    public void reset() {
        currentIndex = 0;
        score = 0;
        Collections.shuffle(quiz);
    }

    public void nextQuiz() {
        currentQuestion = quiz.get(currentIndex).getQuestion();
        currentAnswer = quiz.get(currentIndex).getAnswer();
        currentIndex++;
    }

    public boolean hasMoreQuiz() {
        return currentIndex < quiz.size() ? true: false;
    }

    public void increaseScore() {
        score++;
    }

    public String getCurrentQuestion() {
        return currentQuestion;
    }

    public boolean isCurrentAnswer() {
        return currentAnswer;
    }

    public int getScore() {
        return score;
    }

}
