package isepreact.controller;

import isepreact.model.Module;
import isepreact.model.User;
import isepreact.repository.ModuleRepository;
import isepreact.repository.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import isepreact.model.Questionnaire;
import isepreact.repository.QuestionnaireRepository;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Controller         // This means that this class is a Controller
@RequestMapping(path = "/profile") // This means URL's start with /database
//(after Application path)
public class ProfileController {
  @Autowired
  private QuestionnaireRepository questionnaireRepository;
  @Autowired
  private ModuleRepository moduleRepository;
  @Autowired
  private UserRepository userRepository;

  @RequestMapping(path = "") // URL /database/add
  public String showTeacherHome(HttpServletRequest request, Model model) {
    User user = userRepository.findByEmail(request.getRemoteUser());
    model.addAttribute("user", user);
    if(user.getRole().equals("teacher") || user.getRole().equals("student") || user.getRole().equals("admin")) {
      if(user.getRole().equals("teacher")) {
        model.addAttribute("modules", moduleRepository.findByTeacher_id(user.getId()));
      }else if(user.getRole().equals("student")){
        model.addAttribute("modules", moduleRepository.findByStudents_id(user.getId()));
      }else{
        model.addAttribute("modules", moduleRepository.findAll());
      }
      model.addAttribute("user", user);
      return "profile/profil";
    }
    return "403";
  }

  @RequestMapping(path = "/prof/note") // URL /database/add
  public String noteModule(HttpServletRequest request, @RequestParam(value = "commentaire") String commentaire, @RequestParam(value = "note") Integer note, @RequestParam(value = "module") Integer module_id, Model model) {
    User user = userRepository.findByEmail(request.getRemoteUser());
    if(user.getRole().equals("teacher")) {
      Module module = moduleRepository.findById(module_id);
      if(user.getId() == module.getTeacher().getId()) {
        module.setCommentaire(commentaire);
        module.setNote(note);
        module.setViewedByAdmin(false);
        module.setDatePourNotif(new Date());
        moduleRepository.save(module);
      }
    }
    return "profile/profil";
  }

  /*@RequestMapping(path = "/prof/module") // URL /database/add
  @ResponseBody
  public String getCommentaire(HttpServletRequest request, @RequestParam(value = "id") Integer module_id,Model model) {
    Module module = moduleRepository.findById(module_id);
    User user = userRepository.findByEmail(request.getRemoteUser());
    if(user.getId() == module.getTeacher().getId() || module.isStudentInModule(user)) {
      Map<String, Object> datasetsArray = new HashMap<String, Object>();
      datasetsArray.put("commentaire", module.getCommentaire());
      datasetsArray.put("note", module.getNote());
      datasetsArray.put("titre", module.getName());
      JSONObject json = new JSONObject(datasetsArray);
      return json.toString();
    }
    return "";
  }*/

  @RequestMapping(path = "/eleve") // URL /database/show
  public String showStudentHome(Model model) {
    return "profile/profil_eleve";
  }

  @RequestMapping(path = "/admin")
  public String showAdminHome() {
    return "profile/profil_admin";
  }

}

