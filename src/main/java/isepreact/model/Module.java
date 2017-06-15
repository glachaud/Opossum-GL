package isepreact.model;

import javax.persistence.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@Entity // This tells Hibernate to make a table ouf of this class
@Table(name = "module")
public class Module {
  private Integer id;

  private String name;

  private User teacher;

  private Set<User> students;

  private Set<Questionnaire> questionnaires;

  private Set<Folder> folders;

  @OneToMany(mappedBy = "module", cascade = CascadeType.ALL)
  public Set<Folder> getFolders() {
    return folders;
  }

  public void setFolders(Set<Folder> folders) {
    this.folders = folders;
  }

  private String commentaire;

  private Integer note;

  private boolean viewedByAdmin;

  private Date datePourNotif;

  public String getCommentaire() {
    return commentaire;
  }

  public Integer getNote() {
    return note;
  }

  public void setNote(Integer note) {
    this.note = note;
  }

  public void setCommentaire(String commentaire) {

    this.commentaire = commentaire;
  }

  @Transient
  public boolean isStudentInModule(User user){
    for(User u : students) {
      if(u.getId() == user.getId()){
        return true;
      }
    }
    return false;
  }


  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
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

  @ManyToOne
  public User getTeacher() {
    return teacher;
  }

  public void setTeacher(User teacher) {
    this.teacher = teacher;
  }

  @ManyToMany
  public Set<User> getStudents() {
    return students;
  }

  public void addStudent(User student){
    if(this.students == null) {
      this.students = new HashSet<>();
    }
    this.students.add(student);
  }

  public void removeStudent(User student){
    this.students.remove(student);
  }

  public void setStudents(Set<User> students) {
    this.students = students;
  }

  @OneToMany(mappedBy = "module", cascade = CascadeType.ALL)
  public Set<Questionnaire> getQuestionnaires() {
    return questionnaires;
  }

  public void setQuestionnaires(Set<Questionnaire> questionnaires) {
    this.questionnaires = questionnaires;
  }

  public boolean isViewedByAdmin() {
    return viewedByAdmin;
  }

  public Date getDatePourNotif() {
    return datePourNotif;
  }

  public void setDatePourNotif(Date datePourNotif) {
    this.datePourNotif = datePourNotif;
  }

  public void setViewedByAdmin(boolean viewedByAdmin) {
    this.viewedByAdmin = viewedByAdmin;


  }
}