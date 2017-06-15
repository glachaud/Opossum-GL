package isepreact.model;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by chrom on 23/05/2017.
 */
@Entity // This tells Hibernate to make a table ouf of this class
@Table(name = "folder")
public class Folder {
    private Integer id;

    private String name;

    private Set<Questionnaire> questionnaires;

    private Set<Conversation> conversations;

    private Module module;

    private User teacher;

    private String type; //Questionnaire / Commentaire

    @ManyToOne
    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
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

    @ManyToMany(cascade = CascadeType.ALL)
    // @JoinTable(name = "folder_questionnaire", joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "publisher_id", referencedColumnName = "id"))
    public Set<Questionnaire> getQuestionnaires() {
        return questionnaires;
    }

    public void setQuestionnaires(Set<Questionnaire> questionnaires) {
        this.questionnaires = questionnaires;
    }

    public void addQuestionnaires(Questionnaire questionnaire) {
        this.questionnaires.add(questionnaire);
    }

    public void removeQuestionnaires(Questionnaire questionnaire) {
        this.questionnaires.remove(questionnaire);
    }

    @ManyToOne
    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    public Set<Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(Set<Conversation> conversations) {
        this.conversations = conversations;
    }

    public void addConversations(Conversation conversation) {
        this.conversations.add(conversation);
    }

    public void removeConversations(Conversation conversation) {
        this.conversations.remove(conversation);
    }
}
