package isepreact.controller;

import isepreact.model.Answer;
import isepreact.model.Question;
import isepreact.model.Questionnaire;
import isepreact.model.User;
import isepreact.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import isepreact.repository.AnswerRepository;
import isepreact.repository.QuestionRepository;
import isepreact.repository.QuestionnaireRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by victo on 08/05/2017.
 */
@Controller         // This means that this class is a Controller
@RequestMapping(path = "/answer")
public class AnswerController {
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private QuestionnaireRepository questionnaireRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping(path = "/add") // URL /database/show
    @ResponseBody
    public String add_answer(HttpServletRequest request, @RequestParam(value = "id_question", required= true) int id_question,
                               @RequestParam(value = "answer", required = false) String answer,
                               Model model) {
        if(questionRepository.exists(id_question)) {
            Answer a = new Answer();
            Question q = questionRepository.findById(id_question);
            User user = userRepository.findByEmail(request.getRemoteUser());
            Questionnaire questionnaire = q.getQuestionnaire();
            if(questionnaire.getModule().isStudentInModule(user) && !q.hasAlreadyAnswered(user)){
                if (questionnaire.isAvailable()) {
                    questionnaire.setViewed(false);
                    questionnaireRepository.save(questionnaire);
                    a.setQuestion(q);
                    a.setUser(user);
                    a.setAnswer(answer);
                    a.setQuestionnaire(questionnaire);
                    a.setDate(new Date());
                    answerRepository.save(a);
                    return "ok";
                }
            }
            return "nok";
        }
        return "nok";
    }

    @PostMapping(path = "/add_live") // URL /database/show
    @ResponseBody
    public String add_answer_live(HttpServletRequest request, @RequestParam(value = "id_question", required = true) int id_question,
                             @RequestParam(value = "answer", required = false) String answer,
                             Model model) {
        if(questionRepository.exists(id_question)) {
            Answer a = new Answer();
            Question q = questionRepository.findById(id_question);
            User user = userRepository.findByEmail(request.getRemoteUser());
            Questionnaire questionnaire = q.getQuestionnaire();
            if(questionnaire.getModule().isStudentInModule(user) && !q.hasAlreadyAnswered(user)) {
                if (questionnaire.isAvailable()) {
                    questionnaireRepository.save(questionnaire);
                    a.setQuestion(q);
                    a.setUser(user);
                    a.setAnswer(answer);
                    a.setDate(new Date());
                    a.setQuestionnaire(questionnaire);
                    answerRepository.save(a);
                    String question = q.getQuestion();
                    String type = q.getType();
                    String ajout = "<div class=\"panel panel-primary panel-question\" data-id='" + q.getId().toString() + "'>\n" +
                            "                    <div class=\"panel-heading\"><strong>" + question + "</strong>\n" +
                            "                    </div>\n" +
                            "                    <div class=\"panel-body\">\n" +
                            "                        <div class=\"col-md-2\">\n" +
                            "                        <span class=\"glyphicon glyphicon-signal\"\n" +
                            "                              aria-hidden=\"true\"></span><span\n" +
                            "                                class=\"info-questionnaire\"><span class='nbAnswer'>0</span> réponses</span>\n" +
                            "                        </div>\n" +
                            "                        <div class=\"col-md-2\">\n" +
                            "                        <span class=\"glyphicon glyphicon-tags\"\n" +
                            "                              aria-hidden=\"true\"></span><span\n";
                    String typeString = type.substring(0, 1).toUpperCase() + type.substring(1);
                    if (type.equals("en_un_mot")) {
                        typeString = "En un mot";
                    }
                    ajout += "                                class=\"info-questionnaire\">" + typeString + "</span>\n" +
                            "                        </div>\n" +
                            "                        <div class=\"col-md-8\">\n";
                    if (type.equals("en_un_mot")) {
                        ajout += "<span><span class=\"glyphicon glyphicon-tasks\"\n" +
                                "\" +\n" +
                                "                    \"                                                        aria-hidden=\\\"true\\\"></span><span\\n\" +\n" +
                                "                    \"                                class=\"info-questionnaire\">Mot le plus donné : <span class=\"resultAnswer\"></span></span>";
                    } else if (type.equals("slider")) {
                        ajout += "                        <span><span class=\"glyphicon glyphicon-tasks\"\n" +
                                "                                                        aria-hidden=\"true\"></span><span\n" +
                                "                                class=\"info-questionnaire\">Réponse moyenne : <i class=\"resultAnswer\" th:class=\"'em '+${question.getAvgAnswer()}\"></i></span></span>\n";
                    } else if (type.equals("selecteur")) {
                        ajout += "                        <span><span class=\"glyphicon glyphicon-tasks\"\n" +
                                "                                                           aria-hidden=\"true\"></span><span\n" +
                                "                                class=\"info-questionnaire\">Réponse la plus donnée : <span class=\"resultAnswer\" th:text=\"${question.getAvgAnswer()}\"></span></span></span>\n";
                    } else if (type.equals("qcm")) {
                        ajout += "                        <span><span class=\"glyphicon glyphicon-tasks\"\n" +
                                "                                                     aria-hidden=\"true\"></span><span\n" +
                                "                                class=\"info-questionnaire\">";
                        if (q.isQcmWithRightAnswers()) {
                            ajout += "Nombre de bonne réponses :";
                        } else {
                            ajout += "Réponse la plus donnée :";
                        }
                        ajout += " <span class=\"resultAnswer\" th:text=\"${question.getAvgAnswer()}\"></span></span></span>\n";
                    } else {
                        ajout += "                        <span><span class=\"glyphicon glyphicon-tasks\"\n" +
                                "                                                 aria-hidden=\"true\"></span><span\n" +
                                "                                class=\"info-questionnaire\">Réponse moyenne : <span class=\"resultAnswer\" th:text=\"${question.getAvgAnswer()}\"></span></span></span>\n";
                    }
                    ajout += "                        </div>\n" +
                            "\n" +
                            "                        <div class=\"col-md-12 show_more_button\">\n" +
                            "                            <div  id='question" + q.getId().toString() + "'>\n" +
                            "<div><div>";
                    if (type.equals("en_un_mot")) {
                        ajout += "<div class='wordCloud' data-question='" + q.getId().toString() + "'></div>";
                    } else if (type.equals("slider")) {
                        ajout += "                                        <div class=\"qcm_answers\" data-id='" + q.getId().toString() + "'>";
                        for (String emoji : q.getEmojisInArray()) {
                            ajout += "<div class='emoji_div'>";
                            ajout += "                                            <span class=\"qcm_answer_title\"><i class=\"em " + emoji + "\"></i></span>\n" +
                                    "                                            <div class=\"progress\">\n" +
                                    "                                                <div class=\"progress-bar progress-bar-striped active\" role=\"progressbar\" aria-valuenow=\"0\"\n" +
                                    "                                                     aria-valuemin=\"0\" aria-valuemax=\"100\" style=\"width:0%\">\n" +
                                    "                                                    0 % (0)\n" +
                                    "                                                </div>\n" +
                                    "                                            </div></div>\n";
                        }
                        ajout += "                                        </div>\n";
                    } else {
                        ajout += "<div class=\"col-md-1\"></div>\n" +
                                "                                    <div class=\"col-md-10\">\n" +
                                "                                        <canvas id=\"" + q.getId() + "\" class=\"chart_js\"></canvas>\n" +
                                "                                    </div>\n" +
                                "                                    <div class=\"col-md-1\"></div>";
                    }
                    ajout += "</div>";
                    return ajout;
                }
            }
            return "nok";
        }
        return "nok";
    }
}
