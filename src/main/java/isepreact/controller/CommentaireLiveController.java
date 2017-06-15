package isepreact.controller;

import isepreact.model.CommentaireLive;
import isepreact.model.Questionnaire;
import isepreact.model.User;
import isepreact.repository.CommentaireLiveRepository;
import isepreact.repository.QuestionnaireRepository;
import isepreact.repository.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by victo on 03/06/2017.
 */

@Controller
@RequestMapping(path = "/commentaireLive")
public class CommentaireLiveController {
    @Autowired
    private CommentaireLiveRepository commentaireLiveRepository;
    @Autowired
    private QuestionnaireRepository questionnaireRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping(path = "/add") // URL /database/show
    @ResponseBody
    public String add_commentaire(HttpServletRequest request, @RequestParam(value = "id_questionnaire", required = true) Integer id_questionnaire,
                                  @RequestParam(value = "message", required = true) String message,
                                  Model model) {
        User user = userRepository.findByEmail(request.getRemoteUser());
        if(questionnaireRepository.exists(id_questionnaire)){
            Questionnaire questionnaire = questionnaireRepository.findById(id_questionnaire);
            if(questionnaire.getType().equals("live") && questionnaire.getModule().isStudentInModule(user)){
                CommentaireLive c = new CommentaireLive();
                c.setDate(new Date());
                c.setMessage(message);
                c.setQuestionnaire(questionnaire);
                commentaireLiveRepository.save(c);
                SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String retour =
                        "                <div class=\"panel panel-default panel_live_comment\" style='display:none;'>\n" +
                        "                    <div class=\"panel-heading date-comment\" data-time='"+DATE_FORMAT.format(c.getDate())+"'>"+DATE_FORMAT.format(c.getDate())+"</div>\n" +
                        "                    <div class=\"panel-body\">"+message+"</div>\n" +
                        "                </div>\n";
                return retour;
            }
        }
        return "nok";
    }

    @PostMapping(path = "/getComments") // URL /database/show
    @ResponseBody
    public String getComments(HttpServletRequest request, @RequestParam(value = "id_questionnaire", required = true) Integer id_questionnaire,
                                  Model model) {
        User user = userRepository.findByEmail(request.getRemoteUser());
        if(questionnaireRepository.exists(id_questionnaire)){
            Questionnaire q = questionnaireRepository.findById(id_questionnaire);
            if(q.getModule().isStudentInModule(user) || q.getUser().getId() == user.getId()) {
                Set<CommentaireLive> commentaires = commentaireLiveRepository.findByQuestionnaire_id(id_questionnaire);
                if(user.getRole().equals("student")) {
                    commentaires = commentaireLiveRepository.findByQuestionnaire_idAndUser_id(id_questionnaire, user.getId());
                }
                SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                HashMap<String, String> comments = new HashMap<String, String>();
                for (CommentaireLive c : commentaires) {
                    comments.put(c.getId().toString(), "                <div id='commentaire_" + c.getId().toString() + "' class=\"panel panel-danger panel_live_comment live_prof_comment\" style='display:none;'>\n" +
                            "                    <div class=\"panel-heading date-comment\" data-time='" + DATE_FORMAT.format(c.getDate()) + "'>" + DATE_FORMAT.format(c.getDate()) + "</div>\n" +
                            "                    <div class=\"panel-body\">" + c.getMessage() + "</div>\n" +
                            "                </div>\n");
                }
                JSONObject json = new JSONObject(comments);
                return json.toString();
            }
        }
        return "nok";
    }

}
