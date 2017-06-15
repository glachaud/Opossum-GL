package isepreact.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by victo on 13/05/2017.
 */
@Entity
public class Commentaire implements Comparable<Commentaire> {

    private Integer id;

    private String message;

    private User destinataire;

    private User expediteur;

    private boolean lu;

    private Date date;

    private Conversation conversation;

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

    @ManyToOne
    public User getDestinataire() {
        return destinataire;
    }

    public void setDestinataire(User destinataire) {
        this.destinataire = destinataire;
    }

    @ManyToOne
    public User getExpediteur() {
        return expediteur;
    }

    public void setExpediteur(User expediteur) {
        this.expediteur = expediteur;
    }

    public boolean isLu() {
        return lu;
    }

    public void setLu(boolean lu) {
        this.lu = lu;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @ManyToOne
    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    @Override
    public int compareTo(Commentaire c) {
        if(this.getDate().after(c.getDate())){
            return 1;
        }else{
            return -1;
        }
    }
}
