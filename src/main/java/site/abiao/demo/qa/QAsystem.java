package site.abiao.demo.qa;

import org.apdplat.qa.SharedQuestionAnsweringSystem;
import org.apdplat.qa.model.CandidateAnswer;
import org.apdplat.qa.model.Question;

import java.util.List;

public class QAsystem {
    public static void main(String[] args) {
        String questionStr = "TCP是什么";
        Question question = SharedQuestionAnsweringSystem.getInstance().answerQuestion(questionStr);
        if (question != null) {
            List<CandidateAnswer> candidateAnswers = question.getAllCandidateAnswer();
            int i=1;
            for(CandidateAnswer candidateAnswer : candidateAnswers){
                System.out.println((i++)+"、"+candidateAnswer.getAnswer()+":"+candidateAnswer.getScore());
            }
        }
    }
}
