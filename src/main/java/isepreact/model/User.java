package isepreact.model;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.Set;

@Entity // This tells Hibernate to make a table out of this class
@Table(name = "user")
public class User {
  private Integer id;

  private String firstName;

  private String lastName;

  private String email;

  private String password;

  private int active;

  private String role; //teacher, student, admin

  private Set<Questionnaire> questionnaires;

  private String photo;

  private String ldap;

  @Id
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  public Set<Questionnaire> getQuestionnaires() {
    return questionnaires;
  }

  public void setQuestionnaires(Set<Questionnaire> questionnaires) {
    this.questionnaires = questionnaires;
  }

  @Length(min=5, message="*Your password must have at least 5 characters")
  @NotEmpty(message = "*Please provide your password")
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int getActive() {
    return active;
  }

  public void setActive(int active) {
    this.active = active;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Email(message = "*Please provide a valid email adress")
  @NotEmpty(message = "*Please provide an email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getPhoto() {
    return photo;
  }

  public void setPhoto(String photo) {
    this.photo = photo;
  }

  @Transient
  public boolean hasPhoto(){
    if(photo != null && !photo.equals("")){
      return true;
    }
    return false;
  }

  public String getLdap() {
    return ldap;
  }

  public void setLdap(String ldap) {
    this.ldap = ldap;
  }
}

