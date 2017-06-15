package isepreact.controller;

import isepreact.model.*;
import isepreact.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by chrom on 23/05/2017.
 */

@Controller         // This means that this class is a Controller
@RequestMapping(path = "/folder") // This means URL's start with /database
public class FolderController {

    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private QuestionnaireRepository questionnaireRepository;
    @Autowired
    private TemplateRepository templateRepository;
    @Autowired
    private CommentaireRepository commentaireRepository;
    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping(path = "/add") // URL /database/show
    @ResponseBody
    public Integer add_folder(HttpServletRequest request, @RequestParam(value = "title", required = true) String title,
                              @RequestParam(value = "module_id", required = false) Integer id,
                              @RequestParam(value = "type", required = false) String type, Model model) {
        Module module = moduleRepository.findById(id);
        User user = userRepository.findByEmail(request.getRemoteUser());
        if(module.getTeacher().getId() == user.getId()) {
            Folder folder = new Folder();
            folder.setName(title);
            folder.setTeacher(user);
            folder.setModule(module);
            folder.setType(type);
            folderRepository.save(folder);
            return folder.getId();
        }
        return -1;
    }

    @PostMapping(path = "/addToFolder") // URL /database/show
    @ResponseBody
    public String addToFolder(HttpServletRequest request, @RequestParam(value = "id_folder", required = true) Integer id_folder,
                              @RequestParam("id") Integer id,Model model) {
        User user = userRepository.findByEmail(request.getRemoteUser());
        Folder folder = folderRepository.findById(id_folder);
        if(folder.getTeacher().getId() == user.getId()) {
            if(folder.getType().equals("questionnaire")) {
                Questionnaire questionnaire = questionnaireRepository.findById(id);
                folder.addQuestionnaires(questionnaire);
                folderRepository.save(folder);
            }else{
                Conversation conversation = conversationRepository.findById(id);
                folder.addConversations(conversation);
                folderRepository.save(folder);
            }
            return "ok";
        }
        return "nok";
    }

    @PostMapping(path = "/removeFromFolder") // URL /database/show
    @ResponseBody
    public String removeFromFolder(HttpServletRequest request, @RequestParam(value = "id_folder", required = true) Integer id_folder,
                              @RequestParam("id") Integer id,Model model) {
        User user = userRepository.findByEmail(request.getRemoteUser());
        Folder folder = folderRepository.findById(id_folder);
        if(folder.getTeacher().getId() == user.getId()) {
            if(folder.getType().equals("questionnaire")) {
                Questionnaire questionnaire = questionnaireRepository.findById(id);
                folder.removeQuestionnaires(questionnaire);
            }else{
                Conversation conversation = conversationRepository.findById(id);
                folder.removeConversations(conversation);
            }
            folderRepository.save(folder);
            return "ok";
        }
        return "nok";
    }

    @PostMapping(path = "/show") // URL /database/show
    public String showFolder(HttpServletRequest request, @RequestParam(value = "folder_id") Integer id, @RequestParam(value = "type") String type, @RequestParam(value = "module_id") Integer module_id, Model model) {
        Folder folder = new Folder();
        User user = userRepository.findByEmail(request.getRemoteUser());
        if(id != 0) {
            folder = folderRepository.findById(id);
            if (folder.getTeacher().getId() == user.getId()) {
                if(folder.getType().equals("questionnaire")) {
                    List<Questionnaire> listQuestionnaires = new ArrayList<Questionnaire>(folder.getQuestionnaires());
                    Collections.sort(listQuestionnaires);
                    model.addAttribute("questionnaires", listQuestionnaires);
                    return "home/home_prof :: questionnaire_table";
                }else{
                    List<Conversation> listConversations = new ArrayList<Conversation>(folder.getConversations());
                    Collections.sort(listConversations);
                    model.addAttribute("conversations", listConversations);
                    model.addAttribute("user", user);
                    return "commentaire/liste_commentaires_prof :: commentaire_table";
                }
            }
        }else if(id == 0){
            if(type.equals("questionnaire")) {
                model.addAttribute("questionnaires", questionnaireRepository.findAllByModule_idOrderByDateDesc(module_id));
                return "home/home_prof :: questionnaire_table";
            }else{
                model.addAttribute("conversations", conversationRepository.findByProf_idAndModuleConcerne_idOrderByDateDesc(user.getId(), module_id));
                model.addAttribute("user", user);
                return "commentaire/liste_commentaires_prof :: commentaire_table";
            }
        }
        return null;
    }

    @PostMapping(path = "/delete") // URL /database/show
    @ResponseBody
    public String deleteFolder(HttpServletRequest request, @RequestParam(value = "folder_id") Integer id, Model model) {
        User user = userRepository.findByEmail(request.getRemoteUser());
        Folder folder =  folderRepository.findById(id);
        if (folder != null && folder.getTeacher().getId() == user.getId()){
            folderRepository.delete(folder);
        }
        return "home/home_prof";
    }

    @PostMapping(path = "/rename") // URL /database/show
    @ResponseBody
    public String renameFolder(HttpServletRequest request, @RequestParam(value = "title", required = true) String title,
                              @RequestParam("folder_id") Integer id,Model model) {
        Folder folder = folderRepository.findById(id);
        User user = userRepository.findByEmail(request.getRemoteUser());
        if(user.getId() == folder.getTeacher().getId()) {
            folder.setName(title);
            folderRepository.save(folder);
        }
        return title;
    }


}
