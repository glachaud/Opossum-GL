package isepreact.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by victo on 13/05/2017.
 */
@Entity
public class CommentaireLive implements Comparable<CommentaireLive> {

    private Integer id;

    private String message;

    private Questionnaire questionnaire;

    private User user;

    private Date date;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @ManyToOne
    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }

    public void setQuestionnaire(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }

    @Override
    public int compareTo(CommentaireLive c) {
        if(this.getDate().after(c.getDate())){
            return 1;
        }else{
            return -1;
        }
    }

    @ManyToOne
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
