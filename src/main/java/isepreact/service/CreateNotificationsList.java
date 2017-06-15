package isepreact.service;

import isepreact.model.Conversation;
import isepreact.model.Module;

import java.util.HashMap;
import java.util.List;

/**
 * Created by victor on 10/06/2017.
 */
public class CreateNotificationsList {

    private List<Conversation> conversations;

    private List<Module> modules;

    private HashMap<Integer, HashMap<String, String>> sortedList;

    public CreateNotificationsList(List<Conversation> conversations, List<Module> modules) {
        this.conversations = conversations;
        this.modules = modules;
        this.sortedList = new HashMap<>();
        this.buildSortList();
    }

    public void buildSortList(){
        int i = 0;
        int j = 0;
        int indexList = 0;
        while(i<conversations.size() || j<modules.size()){
            if(i == conversations.size()){
                addModule(modules.get(j),indexList);
                j += 1;
            }else if(j == modules.size()){
                addConversation(conversations.get(i), indexList);
                i += 1;
            }else if(conversations.get(i).getDatePourNotif().before(modules.get(j).getDatePourNotif())){
                addModule(modules.get(j), indexList);
                j += 1;
            }else{
                addConversation(conversations.get(i), indexList);
                i += 1;
            }
            indexList += 1;
        }
    }

    public void addModule(Module module, Integer index){
        HashMap<String, String> ajout = new HashMap<>();
        ajout.put("type", "module");
        ajout.put("professeur", module.getTeacher().getFirstName() + ' ' + module.getTeacher().getLastName());
        ajout.put("cours", module.getName());
        ajout.put("id", module.getId().toString());
        String photo = "/img/photos_profil/default-user-image.png";
        if(module.getTeacher().hasPhoto()){
            photo = "/files/" + module.getTeacher().getPhoto();
        }
        ajout.put("photo", photo);
        sortedList.put(index, ajout);
    }

    public void addConversation(Conversation conversation, Integer index){
        HashMap<String, String> ajout = new HashMap<>();
        ajout.put("type", "conversation");
        ajout.put("professeur", conversation.getProf().getFirstName() + ' ' + conversation.getProf().getLastName());
        ajout.put("cours", conversation.getModuleConcerne().getName());
        ajout.put("id", conversation.getCommentaires().iterator().next().getId().toString());
        String photo = "/img/photos_profil/default-user-image.png";
        if(conversation.getProf().hasPhoto()){
            photo = "/files/" + conversation.getProf().getPhoto();
        }
        ajout.put("photo", photo);
        sortedList.put(index, ajout);
    }

    //Fonction de debuggage
    public void print(){
        System.out.println("Taille = " + sortedList.size());
        for(int i = 0 ; i<sortedList.size() ; i++){
            System.out.println("//////////");
            System.out.println("Type = " + sortedList.get(i).get("type"));
            System.out.println("Professeur = " + sortedList.get(i).get("professeur"));
            System.out.println("Cours = " + sortedList.get(i).get("cours"));
            System.out.println("//////////");
        }
    }

    public List<Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(List<Conversation> conversations) {
        this.conversations = conversations;
    }

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    public HashMap<Integer, HashMap<String, String>> getSortedList() {
        return sortedList;
    }

    public void setSortedList(HashMap<Integer, HashMap<String, String>> sortedList) {
        this.sortedList = sortedList;
    }
}
