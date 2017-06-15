package isepreact.controller;

import isepreact.controller.storage.StorageService;
import isepreact.repository.QuestionnaireRepository;
import isepreact.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class OpossumsController implements ErrorController {
  private static final String PATH = "/error";

  @RequestMapping(path = "/error")
  public String redirectError() {
    return "page_introuvable";
  }

  @Override
  public String getErrorPath() {
    return PATH;
  }

}
