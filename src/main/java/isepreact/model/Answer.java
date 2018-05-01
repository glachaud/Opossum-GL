package isepreact.model;

import javax.persistence.*;
import java.util.Date;

import static java.lang.StrictMath.floor;

/**
 * Created by victo on 05/08/17.
 */
@Entity
public class Answer implements Comparable<Answer>{
    private Integer id;

    private Question question;

    private User user;

    private Questionnaire questionnaire;

    private String answer;

    private Date date;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne
    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @ManyToOne
    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }

    public void setQuestionnaire(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }

    @ManyToOne
    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Transient
    public String getAnswerEmoji(){
        int answerInt = Integer.parseInt(answer);
        return question.getEmojisForThisValue(answerInt);
    }

    @Override
    public int compareTo(Answer a) {
        if(this.getDate().after(a.getDate())){
            return 1;
        }else{
            return -1;
        }
    }

    @Transient
    public String[] getSplittedAnswer(){
        return answer.split(";");
    }
}