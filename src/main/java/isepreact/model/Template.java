package isepreact.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by victo on 16/05/2017.
 *
 * Template d'un questionnaire
 */
@Entity
public class Template {

    private Integer id;

    private String nom;

    private Questionnaire questionnaire;

    private User user;

    private Date date;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @OneToOne(cascade = CascadeType.ALL)
    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }

    public void setQuestionnaire(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @OneToOne
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
