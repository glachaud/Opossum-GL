package isepreact.controller;

import isepreact.model.Commentaire;
import isepreact.model.Conversation;
import isepreact.model.Module;
import isepreact.model.User;
import isepreact.repository.*;
import isepreact.service.CreateNotificationsList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by victo on 13/05/2017.
 */
@Controller         // This means that this class is a Controller
@RequestMapping(path = "/commentaire")
public class CommentaireController {
    @Autowired
    private CommentaireRepository commentaireRepository;
    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private TemplateRepository templateRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private FolderRepository folderRepository;

    @PostMapping(path = "/add") // URL /database/show
    @ResponseBody
    public String add_commentaire(HttpServletRequest request, @RequestParam(value = "titre", required = true) String titre,
                                  @RequestParam(value = "commentaire", required = true) String commentaire,
                                  @RequestParam(value = "module_id", required = true) Integer module_id,
                                  Model model) {
        if(titre != "" && commentaire != "" && moduleRepository.exists(module_id)) {
            Module module = moduleRepository.findById(module_id);
            User user = userRepository.findByEmail(request.getRemoteUser());
            if(module.isStudentInModule(user)) {
                Commentaire comment = new Commentaire();
                comment.setLu(false);
                comment.setMessage(commentaire);
                comment.setDestinataire(module.getTeacher());
                comment.setExpediteur(user);
                Conversation conversation = new Conversation();
                conversation.setTitre(titre);
                conversation.setModuleConcerne(module);
                conversation.setDate(new Date());
                conversation.setEleve(user);
                conversation.setProf(module.getTeacher());
                conversation.setOffensif(false);
                conversationRepository.save(conversation);
                comment.setConversation(conversation);
                comment.setDate(new Date());
                commentaireRepository.save(comment);
                return "ok";
            }
        }
        return "nok";
    }

    @RequestMapping(path = "/view/{id_commentaire}")
    public String view(HttpServletRequest request, @PathVariable(value="id_commentaire") Integer id,
                          Model model){
        User user = userRepository.findByEmail(request.getRemoteUser());
        Commentaire c = commentaireRepository.findById(id);
        if(c.getDestinataire().getId() == user.getId() || c.getExpediteur().getId() == user.getId() || (user.getRole().equals("admin") && c.getConversation().isOffensif())) {
            if (c.getDestinataire().getId() == user.getId()) {
                c.setLu(true);
                commentaireRepository.save(c);
            }if(user.getRole().equals("admin")){
                c.getConversation().setOffensifViewedByAdmin(true);
                conversationRepository.save(c.getConversation());
            }
            model.addAttribute("commentaire", c);
            model.addAttribute("commentaires", commentaireRepository.findByDestinataire_idOrderByDateDesc(user.getId()));
            model.addAttribute("commentaires_non_lus", commentaireRepository.findByDestinataire_idAndLuFalse(user.getId()));
            model.addAttribute("user", user);
            if(user.getRole().equals("teacher")) {
                model.addAttribute("templates", templateRepository.findByUser_id(user.getId()));
                model.addAttribute("type", "prof");
                model.addAttribute("module_id", c.getConversation().getModuleConcerne().getId());
                model.addAttribute("modules", moduleRepository.findByTeacher_id(user.getId()));
                model.addAttribute("folders", folderRepository.findByModule_idAndTeacher_idAndType(user.getId(), id, "commentaire"));
            } else if(user.getRole().equals("student")){
                model.addAttribute("type", "eleve");
                model.addAttribute("modules", moduleRepository.findByStudents_id(user.getId()));
            } else if(user.getRole().equals("admin")){
                c.getConversation().setOffensifViewedByAdmin(true);
                conversationRepository.save(c.getConversation());
                List<Conversation> alertes = conversationRepository.findByOffensifTrueAndOffensifViewedByAdminFalseOrderByDatePourNotif();
                List<Module> notes = moduleRepository.findByViewedByAdminFalseOrderByDatePourNotif();
                CreateNotificationsList notificationsBuilder = new CreateNotificationsList(alertes, notes);
                HashMap<Integer, HashMap<String, String>> notifications = notificationsBuilder.getSortedList();
                model.addAttribute("notifications", notifications);
                model.addAttribute("type", "admin");
            }
            return "commentaire/commentaire";
        }
        return "403";
    }

    @RequestMapping(path = "/view_all/{id_module}")
    public String viewAll(HttpServletRequest request, @PathVariable(value="id_module", required=false) Integer id, Model model){
        User user = userRepository.findByEmail(request.getRemoteUser());
        model.addAttribute("commentaires", commentaireRepository.findByDestinataire_idOrderByDateDesc(user.getId()));
        model.addAttribute("commentaires_non_lus", commentaireRepository.findByDestinataire_idAndLuFalse(user.getId()));
        model.addAttribute("user", user);
        if(user.getRole().equals("teacher")) {
            model.addAttribute("templates", templateRepository.findByUser_id(user.getId()));
            model.addAttribute("conversations", conversationRepository.findByProf_idAndModuleConcerne_idOrderByDateDesc(user.getId(), id));
            model.addAttribute("module_id", id);
            model.addAttribute("modules", moduleRepository.findByTeacher_id(user.getId()));
            model.addAttribute("folders", folderRepository.findByModule_idAndTeacher_idAndType(id, user.getId(), "commentaire"));
            return "commentaire/liste_commentaires_prof";
        }
        return "403";
    }

    @RequestMapping(path = "/view_all")
    public String viewAll(HttpServletRequest request, Model model){
        User user = userRepository.findByEmail(request.getRemoteUser());
        model.addAttribute("commentaires", commentaireRepository.findByDestinataire_idOrderByDateDesc(user.getId()));
        model.addAttribute("commentaires_non_lus", commentaireRepository.findByDestinataire_idAndLuFalse(user.getId()));
        model.addAttribute("user", user);
        if(user.getRole().equals("student")){
            model.addAttribute("modules", moduleRepository.findByStudents_id(user.getId()));
            model.addAttribute("conversations", conversationRepository.findByEleve_idOrderByDateDesc(user.getId()));
            return "commentaire/liste_commentaires_eleve";
        }else if(user.getRole().equals("admin")){
            List<Conversation> alertes = conversationRepository.findByOffensifTrueAndOffensifViewedByAdminFalseOrderByDatePourNotif();
            List<Module> notes = moduleRepository.findByViewedByAdminFalseOrderByDatePourNotif();
            CreateNotificationsList notificationsBuilder = new CreateNotificationsList(alertes, notes);
            HashMap<Integer, HashMap<String, String>> notifications = notificationsBuilder.getSortedList();
            model.addAttribute("notifications", notifications);
            model.addAttribute("conversations", conversationRepository.findByOffensifTrue());
            return "commentaire/liste_commentaires_admin";
        }
        return "403";
    }

    @PostMapping(path = "/lu") // URL /database/show
    @ResponseBody
    public String add_commentaire(HttpServletRequest request, Model model) {
        User user = userRepository.findByEmail(request.getRemoteUser());
        Set<Commentaire> commentaires = commentaireRepository.findByDestinataire_idAndLuFalse(user.getId());
        for(Commentaire commentaire : commentaires){
            commentaire.setLu(true);
            commentaireRepository.save(commentaire);
        }
        return "ok";
    }

    @PostMapping(path = "/delete") // URL /database/show
    @ResponseBody
    public String delete(HttpServletRequest request, @RequestParam(value = "id", required = true) Integer id, Model model) {
        if(conversationRepository.exists(id)) {
            User user = userRepository.findByEmail(request.getRemoteUser());
            Conversation conversation = conversationRepository.findById(id);
            if(conversation.getEleve().getId() == user.getId() || conversation.getProf().getId() == user.getId() || user.getRole().equals("admin")){
                if(user.getRole().equals("student")){
                    conversation.setDeletedByStudent(true);
                }else if(user.getRole().equals("teacher")){
                    conversation.setDeletedByTeacher(true);
                }else{
                    conversation.setDeletedByAdmin(true);
                }
                conversationRepository.save(conversation);
                return "ok";
            }
        }
        return "nok";
    }

    @PostMapping(path = "/alerte") // URL /database/show
    @ResponseBody
    public String alert_commentaire(HttpServletRequest request, @RequestParam(value = "id", required = true) Integer id,
                                  Model model) {
        if(commentaireRepository.exists(id)){
            User user = userRepository.findByEmail(request.getRemoteUser());
            Commentaire com = commentaireRepository.findById(id);
            Conversation conv = com.getConversation();
            if(user.getRole().equals("teacher") && conv.getProf().getId() == user.getId()) {
                boolean offensif = conv.isOffensif();
                if (offensif) {
                    conv.setOffensif(false);
                } else {
                    conv.setOffensif(true);
                    conv.setOffensifViewedByAdmin(false);
                    conv.setDatePourNotif(new Date());
                }
                conv.setDeletedByAdmin(false);
                conversationRepository.save(conv);
                return "ok";
            }
        }
        return "nok";
    }

    @PostMapping(path = "/answer") // URL /database/show
    @ResponseBody
    public String answer_commentaire(HttpServletRequest request, @RequestParam(value = "id", required = true) Integer id,
                                     @RequestParam(value = "message", required = true) String message,
                                    Model model) {
        if(conversationRepository.exists(id)){
            Conversation conv = conversationRepository.findById(id);
            User user = userRepository.findByEmail(request.getRemoteUser());
            if(conv.getEleve().getId() == user.getId() || conv.getProf().getId() == user.getId()) {
                Commentaire com = new Commentaire();
                com.setConversation(conv);
                com.setDate(new Date());
                com.setLu(false);
                com.setExpediteur(user);
                if(user.getRole().equals("student")){
                    com.setDestinataire(com.getConversation().getProf());
                }else{
                    com.setDestinataire(com.getConversation().getEleve());
                }
                com.setMessage(message);
                Conversation conversation = com.getConversation();
                conversation.setDeletedByTeacher(false);
                conversation.setDate(new Date());
                conversation.setDeletedByStudent(false);
                conversationRepository.save(conversation);
                DateFormat mediumDateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
                commentaireRepository.save(com);
                String photo = "/img/photos_profil/default-user-image.png";
                if(user.hasPhoto()){
                    photo = "/files/" + user.getPhoto();
                }
                String renvoie = "<div class=\"media\">\n" +
                        "                        <div class=\"media-left\">\n" +
                        "                            <img src=\""+photo+"\"\n" +
                        "                                 class=\"media-object\" style=\"width:60px\">\n" +
                        "                        </div>\n" +
                        "                        <div class=\"media-body\">\n" +
                        "                            <h4 class=\"media-heading\">"+user.getFirstName()+" "+user.getLastName()+"\n" +
                        "                                <small><i>" + mediumDateFormat.format(com.getDate()) + "</i>\n" +
                        "                                </small>\n" +
                        "                            </h4>\n" +
                        "                            <p>\n" + com.getMessage() +
                        "                            </p>\n" +
                        "                        </div>\n" +
                        "                    </div>\n" +
                        "                    <hr class=\"doted\">";
                return renvoie;
            }
        }
        return "nok";
    }
}
