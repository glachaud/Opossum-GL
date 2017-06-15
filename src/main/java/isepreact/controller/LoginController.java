package isepreact.controller;

import isepreact.configuration.ldap.LDAPAccess;
import isepreact.configuration.ldap.LDAPObject;
import isepreact.repository.UserRepository;
import isepreact.service.UserService;
import isepreact.model.User;
import org.json.JSONObject;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import isepreact.model.Questionnaire;
import isepreact.repository.QuestionnaireRepository;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Controller         // This means that this class is a Controller
public class LoginController {
  @Autowired
  private QuestionnaireRepository questionnaireRepository;
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserService userService;

  @GetMapping(path = "/login")
  public ModelAndView login() {
    ModelAndView modelAndView = new ModelAndView();
    modelAndView.addObject("questionnaire", new Questionnaire());
    modelAndView.setViewName("login");
    return modelAndView;
  }

  @PostMapping(path = "/loginLDAP") // URL /database/add
  @ResponseBody
  public String getRetour(@RequestParam(value = "email") String username, @RequestParam(value = "password") String password) {
      LDAPAccess access = new LDAPAccess();
      try {
        HashMap<String, String> data = new HashMap<>();
        LDAPObject ldapUser = access.LDAPget(username, password);
        if (userRepository.exists(Integer.parseInt(ldapUser.getNumber()))) {
          User user = userRepository.findById(Integer.parseInt(ldapUser.getNumber()));
          data.put("email", user.getEmail());
          data.put("password", user.getLdap());
        } else {
          User user = new User();
          String type = "";
          if (ldapUser.getType().equals("eleve")) {
            type = "student";
          } else if (ldapUser.getType().equals("prof")) {
            type = "teacher";
          } else if (ldapUser.getType().equals("admin")) {
            type = "admin";
          }
          user.setId(Integer.parseInt(ldapUser.getNumber()));
          user.setFirstName(ldapUser.getPrenom());
          user.setLastName(ldapUser.getNomFamille());
          user.setActive(1);
          user.setEmail(ldapUser.getMail());
          String random = UUID.randomUUID().toString();
          BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
          String mdp = bCryptPasswordEncoder.encode(random);
          user.setPassword(mdp);
          user.setRole(type);
          user.setLdap(random);
          userRepository.save(user);
          data.put("email", user.getEmail());
          data.put("password", random);
        }
        JSONObject json = new JSONObject(data);
        return json.toString();
      } catch (Exception e) {
        return "nok";
      }
  }


}
