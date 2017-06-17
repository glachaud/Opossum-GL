package isepreact.model;

import org.thymeleaf.util.ArrayUtils;

import javax.persistence.*;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by guillaumelachaud on 5/2/17.
 */
@Entity
public class Question implements Comparable<Question> {
  private Integer id;

  private String type;

  private Questionnaire questionnaire;

  private String question;

  private Set<Answer> answers;

  private Integer indexQuestion;

  private Double min_grade;

  private Double max_grade;

  private String selectors;

  private String qcm;

  private String right_answers;

  private String emojis;

  private String instruction;



  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @ManyToOne
  public Questionnaire getQuestionnaire() {
    return questionnaire;
  }

  public void setQuestionnaire(Questionnaire questionnaire) {
    this.questionnaire = questionnaire;
  }


  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
  public Set<Answer> getAnswers() {
    return answers;
  }

  public void setAnswers(Set<Answer> answers) {
    this.answers = answers;
  }

  public void removeAnswer(Answer answer){ this.answers.remove(answer); }

  public Integer getIndexQuestion() {
    return indexQuestion;
  }

  public void setIndexQuestion(Integer indexQuestion) {
    this.indexQuestion = indexQuestion;
  }

  public Double getMin_grade() {
    return min_grade;
  }

  public void setMin_grade(Double min_grade) {
    this.min_grade = min_grade;
  }

  public Double getMax_grade() {
    return max_grade;
  }

  public void setMax_grade(Double max_grade) {
    this.max_grade = max_grade;
  }

  public String getSelectors() {
    return selectors;
  }

  public void setSelectors(String selectors) {
    this.selectors = selectors;
  }

  public String getEmojis() {
    return emojis;
  }

  public void setEmojis(String emojis) {
    this.emojis = emojis;
  }

  @Transient
  public boolean hasAlreadyAnswered(User user){
    for(Answer a : getAnswers()){
      if(a.getUser().getId() == user.getId()){
        return true;
      }
    }
    return false;
  }

  @Transient
  public String[] getEmojisInArray(){
    return this.emojis.split(",");
  }

  @Transient
  public Answer getAnswerFrom(User user){
    for(Answer a : answers){
      if(a.getUser().getId() == user.getId()){
        return a;
      }
    }
    return null;
  }

  @Transient
  public int getSelectorsPlace(String string){
    String[] selectorsArray = getSelectorsInArray();
    for(int i = 0 ; i<selectorsArray.length ; i++){
      if(selectorsArray[i].equals(string)){
        return i;
      }
    }
    return 0;
  }

  @Transient
  public int getQcmPlace(String string){
    String[] qcmArray = getQcmsInArray();
    for(int i = 0 ; i<qcmArray.length ; i++){
      if(qcmArray[i].equals(string)){
        return i;
      }
    }
    return 0;
  }

  @Transient
  public String[] getSelectorsInArray(){
    return this.selectors.split(";");
  }

  @Transient
  public String[] getQcmsInArray(){
    return this.qcm.split(";");
  }

  @Transient
  public String[] getQcmsAnswersInArray(){
    return this.right_answers.split(";");
  }

  @Transient
  //Nous dit si ca fait parti des bonnes réponses du QCM ou non...
  public boolean isItRightQcmAnswer(String answer){
    String[] qcmAnswers = getQcmsAnswersInArray();
    if(ArrayUtils.contains( qcmAnswers, answer )){
      return true;
    }
    return false;
  }

  @Transient
  //Donne classe (success ou danger) pour une réponse du QCM en fonction de si elle est juste ou non
  public String getClassThisQcmAnswer(String answer) {
    String[] qcmAnswers = getQcmsAnswersInArray();
    if(ArrayUtils.contains( qcmAnswers, answer )){
      return "progress-bar  progress-bar-success";
    }else{
      return "progress-bar progress-bar-danger";
    }
  }

  @Transient
  //Nombre de personnes ayant répondu avec cet emoji
  public int getNbAnswerForThisEmoji(String emoji){
    int nb = 0;
    for(Answer a : answers){
      if(emoji.equals(this.getEmojisForThisValue(Integer.parseInt(a.getAnswer())))){
        nb += 1;
      }
    }
    return nb;
  }

  @Transient
  //Nombre de personnes ayant répondu ça pour le QCM
  public int getNbAnswerForThisAnswer(String answer){
    int nb = 0;
    for(Answer a : answers){
      String[] answerList = a.getAnswer().split(";");
      if(Arrays.asList(answerList).containsAll(Arrays.asList(answer))){
        nb +=1;
      }
    }
    return nb;
  }

  @Transient
  //Nombre de personnes ayant répondu ça pour le QCM
  public String getPourcentageForThisAnswer(String answer){
    int nbAnswer = getNbAnswerForThisAnswer(answer);
    return (new DecimalFormat("#.##").format(((float)nbAnswer/answers.size())*100)).replace(',','.');
  }

  @Transient
  //Nombre de personnes ayant répondu avec cet emoji en %
  public String getPourcentageForThisEmoji(String emoji){
    int nbAnswer = getNbAnswerForThisEmoji(emoji);
    return (new DecimalFormat("#.##").format(((float)nbAnswer/answers.size())*100)).replace(',','.');
  }

  @Transient
  public boolean isQcmWithRightAnswers() {
    if(type.equals("qcm")){
      if(getRight_answers().equals("")){
        return false;
      }
      return true;
    }
    return false;
  }

  @Transient
  public String getEmojisForThisValue(int value) {
    String[] emojis = getEmojisInArray();
    if(value < 20){
      return emojis[0];
    }else if(value < 40){
      return  emojis[1];
    }else if(value < 60){
      return emojis[2];
    }else if(value < 80){
      return emojis[3];
    }else{
      return emojis[4];
    }
  }

  @Transient
  public String getAvgAnswer(){
    if(answers.size() > 0) {
      if (type.equals("ouverte")) {
        return "";
      } else if (type.equals("notation") || type.equals("roti")) {
        int avg = 0;
        for (Answer answer : answers) {
          avg += Integer.parseInt(answer.getAnswer());
        }
        return Double.toString(avg / answers.size());
      }else if(type.equals("en_un_mot")){
        HashMap<String, Integer> data = new HashMap<String, Integer>();
        for(Answer a : getAnswers()){
          if(data.containsKey(a.getAnswer())){
            data.replace(a.getAnswer(), (data.get(a.getAnswer())+1));
          }else{
            data.put(a.getAnswer(), 1);
          }
        }
        if(answers.size() > 0){
          return data.entrySet().stream().max((a,b) -> a.getValue().compareTo(b.getValue())).get().getKey();
        }else{
          return "";
        }
      } else if (type.equals("slider")) {
        int avg = 0;
        for (Answer answer : answers) {
          avg += Integer.parseInt(answer.getAnswer());
        }
        return getEmojisForThisValue(avg / answers.size());
      } else if (type.equals("qcm")) {
        if(isQcmWithRightAnswers()) {
          int nbRightAnswer = 0;
          String[] rightAnswers = getQcmsAnswersInArray();
          for (Answer answer : answers) {
            String[] answerList = answer.getAnswer().split(";");
            if (Arrays.asList(answerList).containsAll(Arrays.asList(rightAnswers)) && Arrays.asList(rightAnswers).containsAll(Arrays.asList(answerList))) {
              nbRightAnswer += 1;
            }
          }
          return nbRightAnswer + " (" + new DecimalFormat("#.##").format(((float) nbRightAnswer / answers.size()) * 100) + " %)";
        }else{
          int[] nbAnswer = new int[getQcmsInArray().length];
          for(Answer answer : answers){
            String[] answerList = answer.getAnswer().split(";");
            for(String a : answerList) {
              nbAnswer[getQcmPlace(a)] += 1;
            }
          }
          int max = 0;
          for (int i = 0; i < nbAnswer.length; i++) {
            if (nbAnswer[i] > nbAnswer[max]) {
              max = i;
            }
          }
          return getQcmsInArray()[max];
        }
      } else {
        int[] nbAnswer = new int[getSelectorsInArray().length];
        for (Answer answer : answers) {
          nbAnswer[getSelectorsPlace(answer.getAnswer())] += 1;
        }
        int max = 0;
        for (int i = 0; i < nbAnswer.length; i++) {
          if (nbAnswer[i] > nbAnswer[max]) {
            max = i;
          }
        }
        return getSelectorsInArray()[max];
      }
    }
    return "";
  }
  @Override
  public int compareTo(Question q) {
    if(this.getIndexQuestion() > q.getIndexQuestion()){
      return 1;
    }else{
      return -1;
    }
  }

  public String getQcm() {
    return qcm;
  }

  public void setQcm(String qcm) {
    this.qcm = qcm;
  }

  public String getRight_answers() {
    return right_answers;
  }

  public void setRight_answers(String right_answers) {
    this.right_answers = right_answers;
  }

  public String getInstruction() {
    return instruction;
  }

  public void setInstruction(String instruction) {
    this.instruction = instruction;
  }
}
