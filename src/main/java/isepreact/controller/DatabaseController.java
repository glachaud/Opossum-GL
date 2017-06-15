package isepreact.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import isepreact.model.Questionnaire;
import isepreact.repository.QuestionnaireRepository;


@Controller         // This means that this class is a Controller
@RequestMapping(path = "/database") // This means URL's start with /database
//(after Application path)
public class DatabaseController {
  @Autowired
  private QuestionnaireRepository questionnaireRepository;

  @RequestMapping(path = "/add") // URL /database/add
  public String addNewQuestionnaire(@RequestParam String name, @RequestParam
          Integer number, Model model) {

    Questionnaire n = new Questionnaire();
    n.setName(name);
    n.setNumberOfQuestions(number);
    questionnaireRepository.save(n);
    model.addAttribute("questionnaires", questionnaireRepository.findAll());
    return "questionnaire";
  }

  @RequestMapping(path = "/show") // URL /database/show
  public String showQuestionnaires(Model model) {
    Questionnaire n = questionnaireRepository.findOne(1);
    model.addAttribute("questionnaires", questionnaireRepository.findAll());
    model.addAttribute("john", n);
    return "questionnaire";
  }

}

