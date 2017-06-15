package isepreact.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by victo on 13/05/2017.
 */
@Entity
public class Conversation implements Comparable<Conversation>{

    private Integer id;

    private Set<Commentaire> commentaires;

    private String titre;

    private User eleve;     //Toujours entre 2 utilisateurs...

    private User prof;

    private boolean offensif;

    private boolean offensifViewedByAdmin;

    private Module moduleConcerne;

    private boolean deletedByTeacher = false;

    private boolean deletedByStudent = false;

    private boolean deletedByAdmin = false;

    private Date datePourNotif;

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
    public User getEleve() {
        return eleve;
    }

    public void setEleve(User eleve) {
        this.eleve = eleve;
    }

    @ManyToOne
    public User getProf() {
        return prof;
    }

    public void setProf(User prof) {
        this.prof = prof;
    }

   @ManyToOne
   public Module getModuleConcerne() {
        return moduleConcerne;
    }

    public void setModuleConcerne(Module moduleConcerne) {
        this.moduleConcerne = moduleConcerne;
    }

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL)
    public Set<Commentaire> getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(Set<Commentaire> commentaires) {
        this.commentaires = commentaires;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public boolean isOffensif() {
        return offensif;
    }

    public void setOffensif(boolean offensif) {
        this.offensif = offensif;
    }

    public boolean isDeletedByTeacher() {
        return deletedByTeacher;
    }

    public void setDeletedByTeacher(boolean deletedByTeacher) {
        this.deletedByTeacher = deletedByTeacher;
    }

    public boolean isDeletedByStudent() {
        return deletedByStudent;
    }

    public void setDeletedByStudent(boolean deletedByStudent) {
        this.deletedByStudent = deletedByStudent;
    }

    public boolean isDeletedByAdmin() {
        return deletedByAdmin;
    }

    public void setDeletedByAdmin(boolean deletedByAdmin) {
        this.deletedByAdmin = deletedByAdmin;
    }

    public boolean isOffensifViewedByAdmin() {
        return offensifViewedByAdmin;
    }

    public void setOffensifViewedByAdmin(boolean offensifViewedByAdmin) {
        this.offensifViewedByAdmin = offensifViewedByAdmin;
    }

    public Date getDatePourNotif() {
        return datePourNotif;
    }

    public void setDatePourNotif(Date datePourNotif) {
        this.datePourNotif = datePourNotif;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int compareTo(Conversation o) {
        if(getDate().after(o.getDate())){
            return -1;
        }
        return 1;
    }
}
