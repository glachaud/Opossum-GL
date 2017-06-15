package isepreact.controller;

import isepreact.configuration.ldap.LDAPAccess;
import isepreact.configuration.ldap.LDAPObject;
import isepreact.controller.storage.StorageService;
import isepreact.model.Commentaire;
import isepreact.model.Conversation;
import isepreact.model.Module;
import isepreact.repository.*;
import isepreact.model.User;
import isepreact.service.CreateNotificationsList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;


@Controller         // This means that this class is a Controller
// This means URL's start with /database
//(after Application path)
public class HomeController {
  @Autowired
  private QuestionnaireRepository questionnaireRepository;
  @Autowired
  private ModuleRepository moduleRepository;
  @Autowired
  private CommentaireRepository commentaireRepository;
  @Autowired
  private TemplateRepository templateRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private FolderRepository folderRepository;
  @Autowired
  private ConversationRepository conversationRepository;

  private final StorageService storageService;

  @Autowired
  public HomeController(StorageService storageService) {
    this.storageService = storageService;
  }

  public HomeController(StorageService storageService, UserRepository userRepository) {
    this.storageService = storageService;
    this.userRepository = userRepository;
  }

  @RequestMapping(path ="")
  public String root(HttpServletRequest request, HttpSession session, Model model) {
    return showHome(request, session, model);
  }
  @RequestMapping(path ="/")
  public String rootRedirect(HttpServletRequest request, HttpSession session, Model model) {
    return showHome(request, session, model);
  }

  @RequestMapping(path = "/home")
  public String showHome(HttpServletRequest request, HttpSession session, Model model) {
    User user = userRepository.findByEmail(request.getRemoteUser());
    session.setAttribute("firstname", user.getFirstName());
    session.setAttribute("lastname", user.getLastName());
    session.setAttribute("username", user.getEmail());
    String name = session.getAttribute("firstname").toString();
    if (user.getRole().equals("student")) {
      return showStudentHome(request, 0, model);
    } else if (user.getRole().equals("teacher")) {
      return showTeacherHome(request, model);
    } else if (user.getRole().equals("admin")){
      return showAdminHome(request, session, 0, model);
    } else {
      return "login";
    }
  }

  @RequestMapping(path = "/home/prof/{id_module}") // URL /database/add
  public String showTeacherHome(HttpServletRequest request, @PathVariable(value = "id_module") Integer id, Model model) {
    User user = userRepository.findByEmail(request.getRemoteUser());
    if(moduleRepository.exists(id)) {
      Module module = moduleRepository.findById(id);
      if (user.getRole().equals("teacher") && module.getTeacher().getId() == user.getId()) {
        model.addAttribute("modules", moduleRepository.findByTeacher_id(user.getId()));
        model.addAttribute("questionnaires", questionnaireRepository.findAllByModule_idOrderByDateDesc(id));
        model.addAttribute("folders", folderRepository.findByModule_idAndTeacher_idAndType(id, user.getId(), "questionnaire"));
        model.addAttribute("templates", templateRepository.findByUser_id(user.getId()));
        model.addAttribute("commentaires", commentaireRepository.findByDestinataire_idOrderByDateDesc(user.getId()));
        model.addAttribute("commentaires_non_lus", commentaireRepository.findByDestinataire_idAndLuFalse(user.getId()));
        model.addAttribute("module_id", id);
        model.addAttribute("module", module);
        model.addAttribute("user", user);
        return "home/home_prof";
      }
      return "403";
    }
    return "page_introuvable";
  }

  @RequestMapping(path = "/home/prof") // URL /database/add
  public String showTeacherHome(HttpServletRequest request, Model model) {
    ModuleController moduleController = new ModuleController(moduleRepository, userRepository, storageService);
    return moduleController.showModuleSelector(request, model);
  }


  @RequestMapping(path = "/home/eleve/{id_module}") // URL /database/show
  public String showStudentHome(HttpServletRequest request, @PathVariable(value = "id_module") Integer id, Model model) {
    User user = userRepository.findByEmail(request.getRemoteUser());
    if(user.getRole().equals("student")) {
      model.addAttribute("modules", moduleRepository.findByStudents_id(user.getId()));
      model.addAttribute("selectedModule", moduleRepository.findById(id));
      model.addAttribute("user", user);
      if (id == 0) {
        model.addAttribute("questionnaires", questionnaireRepository.findWithStudentIdOrderByDateDesc(user.getId()));
      } else {
        if (moduleRepository.findById(id).isStudentInModule(user)) {
          model.addAttribute("questionnaires", questionnaireRepository.findAllByModule_idOrderByDateDesc(id));
        }else{
          return "403";
        }
      }
      model.addAttribute("commentaires", commentaireRepository.findByDestinataire_idOrderByDateDesc(user.getId()));
      model.addAttribute("commentaires_non_lus", commentaireRepository.findByDestinataire_idAndLuFalse(user.getId()));
      return "home/home_eleve";
    }
    return "login";
  }


  @RequestMapping(path = "/home/admin") // URL /database/add
  public String showAdminHome(HttpServletRequest request, HttpSession session, Integer id, Model model) {
    User user = userRepository.findByEmail(request.getRemoteUser());
    if(user.getRole().equals("admin")) {
      model.addAttribute("user", user);
      List<Conversation> alertes = conversationRepository.findByOffensifTrueAndOffensifViewedByAdminFalseOrderByDatePourNotif();
      List<Module> notes = moduleRepository.findByViewedByAdminFalseOrderByDatePourNotif();
      CreateNotificationsList notificationsBuilder = new CreateNotificationsList(alertes, notes);
      HashMap<Integer, HashMap<String, String>> notifications = notificationsBuilder.getSortedList();
      model.addAttribute("notifications", notifications);
      model.addAttribute("questionnaires", questionnaireRepository.findAllByDeletedByAdminFalseOrderByDateDesc());
      return "home/home_admin";
    }
    return "login";
  }

}

