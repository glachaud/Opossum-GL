package isepreact.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity // This tells Hibernate to make a table ouf of this class
@Table(name = "questionnaire")
public class Questionnaire implements Comparable<Questionnaire>{
  private Integer id;

  private String name;

  private String type;

  private Integer numberOfQuestions;

  private boolean viewed;

  private boolean ongoing;

  private boolean anonyme;

  private Date date;

  private Date dateDebut;

  private Date dateFin;

  private Set<Question> questions;

  private Set<CommentaireLive> commentaireLives;

  private Module module;

  private User user;

  private boolean deletedByAdmin;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getNumberOfQuestions() {
    return numberOfQuestions;
  }

  public void setNumberOfQuestions(Integer numberOfQuestions) {
    this.numberOfQuestions = numberOfQuestions;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public boolean isViewed() {
    return viewed;
  }

  public void setViewed(boolean viewed) {
    this.viewed = viewed;
  }

  public Date getDateDebut() {
    return dateDebut;
  }

  public void setDateDebut(Date dateDebut) {
    this.dateDebut = dateDebut;
  }

  public Date getDateFin() {
    return dateFin;
  }

  public void setDateFin(Date dateFin) {
    this.dateFin = dateFin;
  }

  @OneToMany(mappedBy = "questionnaire", cascade = CascadeType.ALL)
  public Set<Question> getQuestions() {
    return questions;
  }

  public void setQuestions(Set<Question> questions) {
    this.questions = questions;
  }

  public void removeQuestion(Question q){ this.questions.remove(q); }

  public void addQuestion(Question q){ this.questions.add(q); }

  @OneToMany(mappedBy = "questionnaire", cascade = CascadeType.ALL)
  public Set<CommentaireLive> getCommentaireLives() {
    return commentaireLives;
  }

  public void setCommentaireLives(Set<CommentaireLive> commentaireLives) {
    this.commentaireLives = commentaireLives;
  }

  @ManyToOne
  public Module getModule() {
    return module;
  }

  public void setModule(Module module) {
    this.module = module;
  }

  @ManyToOne
  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Transient
  public boolean isAvailable(){
    Date now = new Date();
    if(dateFin == null || dateDebut == null){
      return true;
    }
    if(now.after(dateFin) || now.before(dateDebut)){
      return false;
    }
    return true;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public boolean isOngoing() {
    return ongoing;
  }

  public void setOngoing(boolean ongoing) {
    this.ongoing = ongoing;
  }

  public boolean isDeletedByAdmin() {
    return deletedByAdmin;
  }

  public void setDeletedByAdmin(boolean deletedByAdmin) {
    this.deletedByAdmin = deletedByAdmin;
  }

  @Override
  public int compareTo(Questionnaire q) {
    if(this.getDate().after(q.getDate())){
      return -1;
    }else{
      return 1;
    }
  }

  public boolean isAnonyme() {
    return anonyme;
  }

  public void setAnonyme(boolean anonyme) {
    this.anonyme = anonyme;
  }
}

