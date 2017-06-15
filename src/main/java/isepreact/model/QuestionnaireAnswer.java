package isepreact.model;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by guillaumelachaud on 5/9/17.
 */
@Entity
public class QuestionnaireAnswer {
  private Integer id;

  private Questionnaire questionnaire;

  private Set<Answer> answers;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  @ManyToOne
  public Questionnaire getQuestionnaire() {
    return questionnaire;
  }

  public void setQuestionnaire(Questionnaire questionnaire) {
    this.questionnaire = questionnaire;
  }

  @OneToMany(mappedBy = "questionnaireAnswer", cascade = CascadeType.ALL)
  public Set<Answer> getAnswers() {
    return answers;
  }

  public void setAnswers(Set<Answer> answers) {
    this.answers = answers;
  }
}
