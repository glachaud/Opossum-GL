package isepreact.controller;

import isepreact.model.Question;
import isepreact.model.Questionnaire;
import isepreact.model.Template;
import isepreact.model.User;
import isepreact.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by victo on 16/05/2017.
 */
@Controller         // This means that this class is a Controller
@RequestMapping(path = "/template")
public class TemplateController {
    @Autowired
    private QuestionnaireRepository questionnaireRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private CommentaireRepository commentaireRepository;
    @Autowired
    private TemplateRepository templateRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping(path = "/add") // URL /database/show
    @ResponseBody
    public String add_template(HttpServletRequest request, @RequestParam(value = "nom", required = true) String nom,
                               @RequestParam(value = "id_questionnaire", required = true) Integer id_questionnaire, Model model) {
        User user = userRepository.findByEmail(request.getRemoteUser());
        if(!nom.equals("") && questionnaireRepository.exists(id_questionnaire)){
            Questionnaire questionnaire = questionnaireRepository.findById(id_questionnaire);
            if(user.getId() == questionnaire.getUser().getId()) {
                Template t = new Template();
                t.setDate(new Date());
                t.setNom(nom);
                t.setUser(user);
                t.setQuestionnaire(questionnaire);
                templateRepository.save(t);
                return t.getId().toString();
            }
        }
        return "nok";
    }

    @PostMapping(path = "/edit") // URL /database/show
    @ResponseBody
    public String edit_template(HttpServletRequest request, @RequestParam(value = "nom", required = true) String nom,
                               @RequestParam(value = "id_questionnaire", required = true) Integer id_questionnaire,
                                @RequestParam(value = "checked", required = true) Boolean checked, Model model) {
        User user = userRepository.findByEmail(request.getRemoteUser());
        if(questionnaireRepository.exists(id_questionnaire)){
            Template template = templateRepository.findByQuestionnaire_id(id_questionnaire);
            Questionnaire questionnaire = questionnaireRepository.findById(id_questionnaire);
            if(user.getId() == questionnaire.getUser().getId()) {
                if(checked && !nom.equals("")) {
                    if (template == null) {
                        template = new Template();
                        template.setDate(new Date());
                        template.setNom(nom);
                        template.setUser(user);
                        template.setQuestionnaire(questionnaire);
                    } else {
                        template.setNom(nom);
                        template.setUser(user);
                        template.setQuestionnaire(questionnaire);
                    }
                    templateRepository.save(template);
                }else{
                    if(template != null){
                        templateRepository.delete(template);
                    }
                }
                return "ok";
            }
        }
        return "nok";
    }

    @PostMapping(path = "/getHTML") // URL /database/show
    @ResponseBody
    public String get_template(HttpServletRequest request, @RequestParam(value = "id", required = true) Integer id,
                               Model model) {
        if(templateRepository.exists(id)) {
            User user = userRepository.findByEmail(request.getRemoteUser());
            Template template = templateRepository.findById(id);
            if(template.getUser().getId() == user.getId()) {
                Questionnaire q = template.getQuestionnaire();
                int index = 0;
                String ajout = "<div id='all_questions' style='display:none;'>";
                List<Question> questionsSorted = q.getQuestions().stream().collect(Collectors.toList());
                Collections.sort(questionsSorted);
                for (Question question : questionsSorted) {
                    index += 1;
                    ajout += "<div style='display:none' class='panel panel-primary div_question' id='form_question_" + question.getId().toString() + "' data-id='" + question.getId().toString() + "'>"
                            + "<div class='panel-heading'><strong>Question " + Integer.toString(index) + "</strong> <span class='glyphicon glyphicon-remove delete_question' onclick='deleteQuestion(" + question.getId().toString() + ");'></span></div>"
                            + "<div class='panel-body'>"
                            + "<div class='col-md-1'></div>"
                            + "<div class='col-md-11 panel_form_questionnaire'>"
                            + "<div class='form-group row'>"
                            + "<div class='col-md-3 label_question_modal'>"
                            + "<label for='titre_question'>Question</label>"
                            + "</div>"
                            + "<div class='col-md-7'>"
                            + "<input type='text' class='form-control' id='titre_question' value='" + question.getQuestion() + "'>"
                            + "</div>"
                            + "</div>"
                            + "<div class='form-group row'>"
                            + "<div class='col-md-3 label_question_modal'>"
                            + "<label for='titre'>Type de question</label>"
                            + "</div>"
                            + "<div class='col-md-9'>"
                            + "<div class='btn-group btn-group-justified'>"
                            + "<div class='btn-group'>"
                            + "<button type='button' class='btn btn-primary type_question' data-type='ouverte' data-question='" + question.getId().toString() + "'>Ouverte</button>"
                            + "</div>"
                            + "<div class='btn-group'>"
                            + "<button type='button' class='btn btn-primary type_question' data-type='selecteur' data-question='" + question.getId().toString() + "'>Selecteur</button>"
                            + "</div>"
                            + "<div class='btn-group'>"
                            + "<button type='button' class='btn btn-primary type_question' data-type='notation' data-question='" + question.getId().toString() + "'>Notation</button>"
                            + "</div>"
                            + "<div class='btn-group'>"
                            + "<button type='button' class='btn btn-primary type_question' data-type='slider' data-question='" + question.getId().toString() + "'>Slider</button>"
                            + "</div>"
                            + "<div class='btn-group'>"
                            + "<button type='button' class='btn btn-primary type_question' data-type='roti' data-question='" + question.getId().toString() + "'>ROTI</button>"
                            + "</div>"
                            + "<div class='btn-group'>"
                            + "<button type='button' class='btn btn-primary type_question' data-type='qcm' data-question='" + question.getId().toString() + "'>QCM</button>"
                            + "</div>"
                            + "</div>"
                            + "<input type='hidden' id='type_question_" + question.getId().toString() + "' name='type_question' value='" + question.getType() + "'/>"
                            + "</div>"
                            + "</div>"
                            + "<div class='form-group row valeur_question' id='ouverte_" + question.getId().toString() + "' " + (question.getType().equals("ouverte") ? "style='display:block'" : "") + " >"
                            + "<div class='col-md-3 label_question_modal'>"
                            + "<strong>Ouverte</strong>"
                            + "</div>"
                            + "<div class='col-md-7'>"
                            + "Réponse sous forme de commentaire libre de la part de l\'élève"
                            + "</div>"
                            + "</div>"
                            + "<div class='form-group row valeur_question' id='selecteur_" + question.getId().toString() + "' " + (question.getType().equals("selecteur") ? "style='display:block'" : "") + ">"
                            + "<div class='col-md-3 label_question_modal'>"
                            + "<strong>Selecteur</strong>"
                            + "</div>"
                            + "<div class='col-md-7'>"
                            + "<div class='form-group row'>"
                            + "<div class='col-md-12'>"
                            + "<div id='groupe_select-" + question.getId().toString() + "'>"
                            + "<div class='input-group'>";
                    if (question.getType().equals("selecteur")) {
                        String[] selectors = question.getSelectorsInArray();
                        for (int i = 0; i < selectors.length; i++) {
                            if (i > 0) {
                                ajout += "<div class='input-group select_input_question'>";
                            }
                            ajout += "<input type='text' value='" + selectors[i] + "' class='form-control' name='valeur_question'><span class='input-group-addon remove_addon' onclick='remove_select_input.call( this );'><span class='glyphicon glyphicon-remove'></span></span>";
                            if (i < selectors.length - 1) {
                                ajout += "</div>";
                            }
                        }
                    } else {
                        ajout += "<input type='text' class='form-control' name='valeur_question'><span class='input-group-addon remove_addon' onclick='remove_select_input.call( this );'><span class='glyphicon glyphicon-remove'></span></span>";
                    }
                    ajout += "</div>"
                            + "</div>"
                            + "<button type='button' class='btn btn-primary add_question_button' onclick='addSelect(" + question.getId().toString() + ");'>"
                            + "<span class='glyphicon glyphicon-plus'></span>"
                            + "</button>"
                            + "</div>"
                            + "</div>"
                            + "</div>"
                            + "</div>"
                            + "<div class='form-group row valeur_question' id='notation_" + question.getId().toString() + "' " + (question.getType().equals("notation") ? "style='display:block'" : "") + " >"
                            + "<div class='col-md-3 label_question_modal'>"
                            + "<strong>Notation</strong>"
                            + "</div>"
                            + "<div class='col-md-3'>"
                            + "<div class='input-group'>";
                    if (question.getType().equals("notation")) {
                        ajout += "<input type='number' class='form-control' name='valeur_question' placeholder='De' value='" + question.getMin_grade().toString() + "'>"
                                + "</div>"
                                + "</div>"
                                + "<div class='col-md-1'></div>"
                                + "<div class='col-md-3'>"
                                + "<div class='input-group'>"
                                + "<input type='number' class='form-control' name='valeur_question2' placeholder='A' value='" + question.getMax_grade().toString() + "'>"
                                + "</div>"
                                + "</div>";
                    } else {
                        ajout += "<input type='number' class='form-control' name='valeur_question' placeholder='De'>"
                                + "</div>"
                                + "</div>"
                                + "<div class='col-md-1'></div>"
                                + "<div class='col-md-3'>"
                                + "<div class='input-group'>"
                                + "<input type='number' class='form-control' name='valeur_question2' placeholder='A'>"
                                + "</div>"
                                + "</div>";
                    }
                    ajout += "</div>"
                            + "<div class='form-group row valeur_question' id='slider_" + question.getId().toString() + "' " + (question.getType().equals("slider") ? "style='display:block'" : "") + ">"
                            + "<div class='col-md-3 label_question_modal'>"
                            + "<strong>Slider</strong>"
                            + "</div>"
                            + "<div class='col-md-7'>"
                            + "<div class='input-group slider_emoji'>"
                            + "<div class='col-md-11'>"
                            + "<input class='form-control input_emoji' type='range' value='50' max='100' min='0' step='5' data-question='" + question.getId().toString() + "'>"
                            + "</div>"
                            + "<div class='col-md-1 smiley_slider'>";
                    if (question.getType().equals("slider")) {
                        ajout += "<i class='popover-emoji em " + question.getEmojisInArray()[2] + "' id='emoji-" + question.getId().toString() + "' data-id='" + question.getId().toString() + "' data-toggle='popover' data-html='true' data-placement='bottom'></i>"
                                + "<input type='hidden' name='tres_mauvais_" + question.getId().toString() + "' value='" + question.getEmojisInArray()[0] + "'/>"
                                + "<input type='hidden' name='mauvais_" + question.getId().toString() + "' value='" + question.getEmojisInArray()[1] + "'/>"
                                + "<input type='hidden' name='mitige_" + question.getId().toString() + "' value='" + question.getEmojisInArray()[2] + "'/>"
                                + "<input type='hidden' name='heureux_" + question.getId().toString() + "' value='" + question.getEmojisInArray()[3] + "'/>"
                                + "<input type='hidden' name='tres_heureux_" + question.getId().toString() + "' value='" + question.getEmojisInArray()[4] + "'/>";
                    } else {
                        ajout += "<i class='popover-emoji em em-expressionless' id='emoji-" + question.getId().toString() + "' data-id='" + question.getId().toString() + "' data-toggle='popover' data-html='true' data-placement='bottom'></i>"
                                + "<input type='hidden' name='tres_mauvais_" + question.getId().toString() + "' value='em-tired_face'/>"
                                + "<input type='hidden' name='mauvais_" + question.getId().toString() + "' value='em-disappointed'/>"
                                + "<input type='hidden' name='mitige_" + question.getId().toString() + "' value='em-expressionless'/>"
                                + "<input type='hidden' name='heureux_" + question.getId().toString() + "' value='em-smiley'/>"
                                + "<input type='hidden' name='tres_heureux_" + question.getId().toString() + "' value='em-heart_eyes'/>";
                    }
                    ajout += "</div>"
                            + "</div>"
                            + "</div>"
                            + "</div>"
                            + "<div class='form-group row valeur_question' id='roti_" + question.getId().toString() + "' " + (question.getType().equals("roti") ? "style='display:block'" : "") + ">"
                            + "<div class='col-md-3 label_question_modal'>"
                            + "<strong>ROTI</strong>"
                            + "</div>"
                            + "<div class='col-md-7'>"
                            + "Réponse où l'élève pourra choisir une note entre 0 et 5."
                            + "</div>"
                            + "</div>"
                            + "<div class=\"form-group row valeur_question\"\n" +
                            "                                             id=\"qcm_" + question.getId().toString() + "\" " + (question.getType().equals("qcm") ? "style='display:block'" : "") + ">\n" +
                            "                                            <div class=\"col-md-3 label_question_modal\">\n" +
                            "                                                <strong>QCM</strong>\n" +
                            "                                            </div>\n" +
                            "\n" +
                            "                                            <div class=\"col-md-7\">\n" +
                            "                                                <div class=\"form-group row\">\n" +
                            "\n" +
                            "                                                    <div class=\"col-md-12\">\n" +
                            "                                                        <div id=\"groupe_qcm-" + question.getId().toString() + "\">\n" +
                            "                                                            <div class=\"input-group\">\n";
                    if (question.getType().equals("qcm")) {
                        String[] qcms = question.getQcmsInArray();
                        for (int i = 0; i < qcms.length; i++) {
                            if (i > 0) {
                                ajout += "<div class='input-group select_input_question'>";
                            }
                            ajout += "                                                                <input type=\"text\"\n" +
                                    "                                                                       class=\"form-control" + (question.isItRightQcmAnswer(qcms[i]) ? " is_true" : "") + "\"\n" +
                                    "                                                                       name=\"valeur_question\" value=\"" + qcms[i] + "\"><span\n" +
                                    "                                                                    class=\"input-group-addon remove_addon\"\n" +
                                    "                                                                    onclick=\"remove_select_input.call( this );\"><span\n" +
                                    "                                                                    class=\"glyphicon glyphicon-remove\"></span></span>\n" +
                                    "                                                                    <span\n" +
                                    "                                                                        class=\"input-group-addon istrue_addon\"\n" +
                                    "                                                                        onclick=\"is_true.call( this );\"><span\n" +
                                    "                                                                        class=\"glyphicon glyphicon-ok\"></span></span>\n";
                            if (i < qcms.length - 1) {
                                ajout += "</div>";
                            }
                        }
                    } else {
                        ajout += "                                                                <input type=\"text\"\n" +
                                "                                                                       class=\"form-control\"\n" +
                                "                                                                       name=\"valeur_question\"><span\n" +
                                "                                                                    class=\"input-group-addon remove_addon\"\n" +
                                "                                                                    onclick=\"remove_select_input.call( this );\"><span\n" +
                                "                                                                    class=\"glyphicon glyphicon-remove\"></span></span>\n" +
                                "                                                                    <span\n" +
                                "                                                                        class=\"input-group-addon istrue_addon\"\n" +
                                "                                                                        onclick=\"is_true.call( this );\"><span\n" +
                                "                                                                        class=\"glyphicon glyphicon-ok\"></span></span>\n";
                    }
                    ajout += "                                                            </div>\n" +
                            "                                                        </div>\n" +
                            "                                                        <button type=\"button\"\n" +
                            "                                                                class=\"btn btn-primary add_question_button\"\n" +
                            "                                                                onclick=\"addQCM(" + question.getId().toString() + ");\">\n" +
                            "                                                            <span class=\"glyphicon glyphicon-plus\"></span>\n" +
                            "                                                        </button>\n" +
                            "                                                    </div>\n" +
                            "                                                </div>\n" +
                            "                                            </div>\n" +
                            "                                        </div>"
                            + "<div class='col-md-1'></div>"
                            + "</div>"
                            + "</div>"
                            + "</div>";
                }
                ajout += "</div>";
                return ajout;
            }
        }
        return "";
    }
}
