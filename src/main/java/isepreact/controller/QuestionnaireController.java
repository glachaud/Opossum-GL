package isepreact.controller;

import isepreact.model.*;
import isepreact.repository.*;
import isepreact.service.CreateNotificationsList;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller         // This means that this class is a Controller
@RequestMapping(path = "/questionnaire")
// This means URL's start with /database
//(after Application path)
public class QuestionnaireController {
    @Autowired
    private QuestionnaireRepository questionnaireRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private CommentaireRepository commentaireRepository;
    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private TemplateRepository templateRepository;
    @Autowired
    private CommentaireLiveRepository commentaireLiveRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConversationRepository conversationRepository;

    @RequestMapping(path = "/show") // URL /database/add
    public String showQuestionnaire(Model model) {
        model.addAttribute("questionnaires", questionnaireRepository.findAll());
        return "questionnaire";
    }

    @RequestMapping(path = "/view/{id_questionnaire}")
    public String getQuestionnaire(HttpServletRequest request, @PathVariable(value="id_questionnaire") Integer id, Model model){
        if(questionnaireRepository.exists(id)) {
            User user = userRepository.findByEmail(request.getRemoteUser());
            Questionnaire questionnaire = questionnaireRepository.findById(id);
            if(user.getId() == questionnaire.getUser().getId() || user.getRole().equals("admin")) {
                model.addAttribute("questionnaire", questionnaire);
                model.addAttribute("user", user);
                if(user.getRole().equals("teacher")) {
                    Template t = templateRepository.findByQuestionnaire_id(questionnaire.getId());
                    model.addAttribute("template", t);
                    model.addAttribute("templates", templateRepository.findByUser_id(user.getId()));
                    model.addAttribute("modules", moduleRepository.findByTeacher_id(user.getId()));
                    model.addAttribute("module_id", moduleRepository.findByTeacher_id(questionnaire.getModule().getId()));
                }
                if(user.getRole().equals("admin")) {
                    List<Conversation> alertes = conversationRepository.findByOffensifTrueAndOffensifViewedByAdminFalseOrderByDatePourNotif();
                    List<Module> notes = moduleRepository.findByViewedByAdminFalseOrderByDatePourNotif();
                    CreateNotificationsList notificationsBuilder = new CreateNotificationsList(alertes, notes);
                    HashMap<Integer, HashMap<String, String>> notifications = notificationsBuilder.getSortedList();
                    model.addAttribute("notifications", notifications);
                    model.addAttribute("questionnaires", questionnaireRepository.findAllByDeletedByAdminFalseOrderByDateDesc());
                }
                if (questionnaire.getType().equals("questionnaire")) {
                    model.addAttribute("questionnaires", questionnaireRepository.findByUser_idOrderByDateDesc(user.getId()));
                    model.addAttribute("commentaires", commentaireRepository.findByDestinataire_idOrderByDateDesc(user.getId()));
                    model.addAttribute("commentaires_non_lus", commentaireRepository.findByDestinataire_idAndLuFalse(user.getId()));
                    model.addAttribute("module_id", questionnaire.getModule().getId());
                    return "questionnaire/questionnaire";
                } else if(user.getRole().equals("teacher")){
                    model.addAttribute("commentaires", commentaireLiveRepository.findByQuestionnaire_id(id));
                    return "questionnaire/live";
                }
            }
        }
        return "403";
    }

    @RequestMapping(path = "/live/{id_questionnaire}")
    public String getQuestionnaireLive(HttpServletRequest request, @PathVariable(value="id_questionnaire") Integer id, Model model){
        if(questionnaireRepository.exists(id)) {
            Questionnaire questionnaire = questionnaireRepository.findById(id);
            Module moduleQuestionnaire = questionnaire.getModule();
            User user = userRepository.findByEmail(request.getRemoteUser());
            if(moduleQuestionnaire.isStudentInModule(user)) {
                model.addAttribute("questionnaire", questionnaire);
                model.addAttribute("user", user);
                model.addAttribute("modules", moduleRepository.findByStudents_id(user.getId()));
                if (questionnaire.getType().equals("live")) {
                    model.addAttribute("commentaires", commentaireLiveRepository.findByQuestionnaire_idAndUser_id(id, user.getId()));
                    return "questionnaire/live_eleve";
                }
            }
        }
        return "403";
    }

    @PostMapping(path = "/getAnswerModal")
    public String showGuestList(HttpServletRequest request, @RequestParam(value = "id", required = true) int id, Model model) {
        if(questionnaireRepository.exists(id)){
            Questionnaire q = questionnaireRepository.findById(id);
            User user = userRepository.findByEmail(request.getRemoteUser());
            if(q.getModule().isStudentInModule(user)){
                model.addAttribute("questionnaire", q);
                if(q.getDateDebut() != null){
                    if(q.isAvailable() && q.getQuestions().iterator().next().hasAlreadyAnswered(user)){
                        model.addAttribute("type", "wait");
                    }else if(!q.isAvailable() && q.getQuestions().iterator().next().hasAlreadyAnswered(user)){
                        model.addAttribute("type", "answer");
                        model.addAttribute("user", user);
                    }else if(!q.isAvailable() && !q.getQuestions().iterator().next().hasAlreadyAnswered(user)){
                        model.addAttribute("type", "tooLate");
                    }else if(q.isAvailable() && !q.getQuestions().iterator().next().hasAlreadyAnswered(user)){
                        model.addAttribute("type", "normal");
                    }
                }else{
                    if(q.getQuestions().iterator().next().hasAlreadyAnswered(user)){
                        model.addAttribute("type", "answer");
                        model.addAttribute("user", user);
                    }else{
                        model.addAttribute("type", "normal");
                    }
                }
                return "home/home_eleve :: questionnaire_answer_modal";
            }
        }
        return "nok";
    }

    @PostMapping(path = "/edit")
    @ResponseBody
    public String edit(HttpServletRequest request, @RequestParam(value = "id", required = true) int id,
                       @RequestParam(value = "titre", required = true) String titre,
                       @RequestParam(value = "nb_question", required = true) int nb_question,
                       @RequestParam(value = "dateDebut", required = true) String dateDebut,
                       @RequestParam(value = "dateFin", required = true) String dateFin,
                       @RequestParam(value = "anonymat", required = true) boolean anonymat,
                       @RequestParam(value = "dateChecked", required = true) boolean dateChecked,
                       Model model) {
        if(questionnaireRepository.exists(id)){
            Questionnaire q = questionnaireRepository.findById(id);
            User user = userRepository.findByEmail(request.getRemoteUser());
            if(q.getUser().getId() == user.getId()){
                q.setName(titre);
                q.setAnonyme(anonymat);
                q.setNumberOfQuestions(nb_question);
                if (!dateDebut.equals("") && !dateFin.equals("") && dateChecked) {
                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    try {
                        Date dateDeDebut = df.parse(dateDebut);
                        Date dateDeFin = df.parse(dateFin);
                        q.setDateDebut(dateDeDebut);
                        q.setDateFin(dateDeFin);
                    } catch (ParseException e) {
                        return "nok";
                    }
                }else{
                    q.setDateDebut(null);
                    q.setDateFin(null);
                }
                questionnaireRepository.save(q);
                return "ok";
            }
        }
        return "nok";
    }

    @PostMapping(path = "/add_questionnaire") // URL /database/show
    @ResponseBody
    public String add_questionnaire(HttpServletRequest request, @RequestParam(value = "titre_questionnaire", required = true) String titre_questionnaire,
                                    @RequestParam(value = "nb_question", required = true) int nb_question,
                                    @RequestParam(value = "module_id", required = true) int module_id,Model model,
                                    @RequestParam(value = "dateDebut", required = true) String dateDebut,
                                    @RequestParam(value = "dateFin", required = true) String dateFin,
                                    @RequestParam(value = "anonymat", required = true) boolean anonymat) {
        User user = userRepository.findByEmail(request.getRemoteUser());
        if(user.getRole().equals("teacher")) {
            if (!titre_questionnaire.equals("")) {
                Questionnaire n = new Questionnaire();
                n.setName(titre_questionnaire);
                n.setNumberOfQuestions(nb_question);
                n.setViewed(true);
                n.setUser(user);
                n.setDate(new Date());
                n.setAnonyme(anonymat);
                n.setDeletedByAdmin(false);
                Module module = moduleRepository.findById(module_id);
                n.setModule(module);
                n.setType("questionnaire");
                if (!dateDebut.equals("") && !dateFin.equals("")) {
                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    try {
                        Date dateDeDebut = df.parse(dateDebut);
                        Date dateDeFin = df.parse(dateFin);
                        n.setDateDebut(dateDeDebut);
                        n.setDateFin(dateDeFin);
                    } catch (ParseException e) {
                        return "nok";
                    }
                }
                questionnaireRepository.save(n);
                return n.getId().toString();
            }
        }
        return String.valueOf(-1);
    }

    @PostMapping(path = "/answerModals") // URL /database/show
    @ResponseBody
    public String add_modals(HttpServletRequest request, @RequestParam(value = "id", required = true) Integer id,
                                     Model model) {
        User user = userRepository.findByEmail(request.getRemoteUser());
        if(questionnaireRepository.exists(id)){
            Questionnaire questionnaire = questionnaireRepository.findById(id);
            if(questionnaire.getModule().isStudentInModule(user) || questionnaire.getUser().getId() == user.getId()){
                Map<String, Object> datas = new HashMap<String, Object>();
                for(Question q : questionnaire.getQuestions()) {
                    String modal = "";
                    if (q.getType().equals("instruction")) {
                        modal += "<div class=\"panel panel-primary panel-question\" data-id='" + q.getId().toString() + "'>\n" +
                                "                    <div class=\"panel-heading\"><strong>" + q.getQuestion() + "</strong>\n" +
                                "                    </div>\n" +
                                "                    <div class=\"panel-body\">\n" +
                                "                        <div class=\"col-md-2\">\n" +
                                "                        <span class=\"glyphicon glyphicon-tags\"\n" +
                                "                              aria-hidden=\"true\"></span><span\n" +
                                "                                class=\"info-questionnaire\">" + q.getType().substring(0, 1).toUpperCase() + q.getType().substring(1) + "</span>\n" +
                                "                        </div>\n" +
                                "                        <div class=\"col-md-8\">\n" +
                                "                        </div>\n" +
                                "\n" +
                                "                        <div class=\"col-md-12 show_more_button\">\n" +
                                "                            <div  id='question" + q.getId().toString() + "'>\n" +
                                "<div><div>" +
                                "<div><pre>" + q.getInstruction() + "</pre></div>" +
                                "</div>";
                    } else {
                        modal += "<div class=\"panel panel-primary add_element_live_div\" id=\"panel" + q.getId().toString() + "\">\n" +
                                "                <div class=\"panel-heading\"><span class=\"glyphicon glyphicon-question-sign\"></span><span>" + q.getQuestion() + "</span></div>\n" +
                                "                <div class=\"panel-body\">\n" +
                                "\n" +
                                "                    <form onsubmit=\"$(this).parent().find('.btn-live-eleve').click(); return false;\">\n" +
                                "\n" +
                                "<div class=\"col-md-12\">";
                    }
                    if (q.getType().equals("en_un_mot")) {
                        modal += "                            <div class=\"form-group\">\n" +
                                "                                <input type=\"text\" placeholder=\"En un mot...\" class=\"form-control\" name=\"reponse\" data-question='" + q.getId() + "'>\n" +
                                "                            </div>\n";
                    } else if (q.getType().equals("slider")) {
                        modal += "                            <div class=\"input-group slider_emoji\">\n" +
                                "                                <div class=\"col-md-11\">\n" +
                                "                                    <input class=\"form-control input_emoji\"\n" +
                                "                                           type=\"range\" value=\"50\" max=\"100\"\n" +
                                "                                           min=\"0\" step=\"5\"\n" +
                                "                                           data-question='" + q.getId() + "'>\n" +
                                "                                </div>\n" +
                                "                                <div class=\"col-md-1 smiley_slider\">\n" +
                                "                                    <i class=\"em " + q.getEmojisInArray()[2] + "\"\n" +
                                "                                       id=\"emoji-" + q.getId().toString() + "\" data-toggle=\"popover\"\n" +
                                "                                       data-html=\"true\"\n" +
                                "                                       data-placement=\"bottom\"></i>\n" +
                                "                                    <input type=\"hidden\"\n" +
                                "                                           name=\"tres_mauvais_" + q.getId().toString() + "\"\n" +
                                "                                           value='" + q.getEmojisInArray()[0] + "'/>\n" +
                                "                                    <input type=\"hidden\" name='mauvais_" + q.getId().toString() + "'\n" +
                                "                                           value='" + q.getEmojisInArray()[1] + "'/>\n" +
                                "                                    <input type=\"hidden\" name='mitige_" + q.getId().toString() + "'\n" +
                                "                                           value='" + q.getEmojisInArray()[2] + "'/>\n" +
                                "                                    <input type=\"hidden\" name='heureux_" + q.getId().toString() + "'\n" +
                                "                                           value='" + q.getEmojisInArray()[3] + "'/>\n" +
                                "                                    <input type=\"hidden\"\n" +
                                "                                           name='tres_heureux_" + q.getId().toString() + "'\n" +
                                "                                           value='" + q.getEmojisInArray()[4] + "'/>\n" +
                                "                                </div>\n" +
                                "                            </div>\n";
                    } else if (q.getType().equals("notation")) {
                        modal += "                            <span class=\"infos_sup\">" + "(Note entre " + q.getMin_grade().toString() + " et " + q.getMax_grade().toString() + ")" + "</span>\n" +
                                "                            <input type=\"hidden\" name=\"max\" value=\"" + q.getMax_grade().toString() + "\" id=\"max" + q.getId().toString() + "\"/>\n" +
                                "                            <input type=\"hidden\" name=\"min\" value=" + q.getMin_grade().toString() + "\" id=\"min" + q.getId().toString() + "\"/>\n" +
                                "                            <input id=\"value" + q.getId().toString() + "\" class=\"form-control\" type=\"number\"\n" +
                                "                                   data-question='" + q.getId().toString() + "' max=\"" + q.getMax_grade() + "\" min=\"" + q.getMin_grade() + "\">\n";
                    } else if (q.getType().equals("roti")) {
                        modal += "                            <div class=\"radio radio_roti icheck-primary\">\n" +
                                "                                <input id=\"" + q.getId().toString() + "1\" data-question='" + q.getId().toString() + "' type=\"radio\" name=\"roti\" value=\"1\"/>\n" +
                                "                                <label for=\"" + q.getId().toString() + "1\">1</label>\n" +
                                "                                <span>Je n'ai rien gagné et rien appris, j'ai vraiment perdu mon temps !</span>\n" +
                                "                            </div>\n" +
                                "                            <div class=\"radio radio_roti icheck-primary\">\n" +
                                "                                <input id=\"" + q.getId().toString() + "2\" data-question='" + q.getId().toString() + "' type=\"radio\" name=\"roti\" value=\"2\"/>\n" +
                                "                                <label for=\"" + q.getId().toString() + "2\">2</label>\n" +
                                "                                <span>Utile, mais ça ne valait pas le temps qu'on y a passé.</span>\n" +
                                "                            </div>\n" +
                                "                            <div class=\"radio radio_roti icheck-primary\">\n" +
                                "                                <input id=\"" + q.getId().toString() + "3\" data-question='" + q.getId().toString() + "' type=\"radio\" name=\"roti\" value=\"3\"/>\n" +
                                "                                <label for=\"" + q.getId().toString() + "3\">3</label>\n" +
                                "                                <span>Dans la moyenne... Correspondait à mes attentes.</span>\n" +
                                "                            </div>\n" +
                                "                            <div class=\"radio radio_roti icheck-primary\">\n" +
                                "                                <input id=\"" + q.getId().toString() + "4\" data-question='" + q.getId().toString() + "' type=\"radio\" name=\"roti\" value=\"4\"/>\n" +
                                "                                <label for=\"" + q.getId().toString() + "4\">4</label>\n" +
                                "                                <span>Bien ! J'ai appris plus que je ne l'espérais pour le temps qu'on y a passé.</span>\n" +
                                "                            </div>\n" +
                                "                            <div class=\"radio radio_roti icheck-primary\">\n" +
                                "                                <input id=\"" + q.getId().toString() + "5\" data-question='" + q.getId().toString() + "' type=\"radio\" name=\"roti\" value=\"5\"/>\n" +
                                "                                <label for=\"" + q.getId().toString() + "5\">5</label>\n" +
                                "                                <span>Excellent ! Cela valait bien plus que le temps qu'on y a passé !</span>\n" +
                                "                            </div>\n";
                    } else if (q.getType().equals("selecteur")) {
                        modal += "                            <select class=\"form-control\" id=\"selecteur" + q.getId().toString() + "\" data-question='" + q.getId().toString() + "'>\n";
                        for (String select : q.getSelectorsInArray()) {
                            modal += " <option value=\"" + select + "\">" + select + "</option>\n";
                        }
                        modal += "                            </select>\n";
                    } else if (q.getType().equals("qcm")) {
                        String[] qcms = q.getQcmsInArray();
                        for (int i = 0; i < qcms.length; i++) {
                            modal += "                            <div class=\"checkbox icheck-primary\">\n" +
                                    "                                <input id=\"qcm" + i + q.getId().toString() + "\" data-question='" + q.getId().toString() + "' type=\"checkbox\" name=\"qcm\" value=\"" + qcms[i] + "\"/>\n" +
                                    "                                <label for=\"qcm" + i + q.getId().toString() + "\">" + qcms[i] + "</label>\n" +
                                    "                            </div>\n";
                        }
                    }
                    if (!(q.getType().equals("instruction"))) {
                        modal += "</div>" +
                                "\n" +
                                "                        </form>\n" +
                                "                    <button class=\"btn btn-success btn-live-eleve\" data-question='" + q.getId().toString() + "' data-type='" + q.getType() + "'><span class=\"glyphicon glyphicon-send\"></span> Envoyer</button>\n" +
                                "                </div>\n" +
                                "            </div>";
                    }
                    HashMap<String, Object> array = new HashMap<String, Object>();
                    array.put("id", q.getId().toString());
                    array.put("type", q.getType());
                    array.put("modal", modal);
                    datas.put(q.getId().toString(), array);
                    JSONObject json = new JSONObject(datas);
                    return json.toString();
                }
            }
        }
        return "nok";
    }

    @PostMapping(path = "/add_live") // URL /database/show
    @ResponseBody
    public String add_live(HttpServletRequest request, @RequestParam(value = "titre", required = true) String titre_questionnaire, @RequestParam(value = "module_id", required = true) int module_id, Model model) {
        User user = userRepository.findByEmail(request.getRemoteUser());
        if(!titre_questionnaire.equals("") && moduleRepository.exists(module_id) && user.getRole().equals("teacher")) {
            Questionnaire n = new Questionnaire();
            Module m = moduleRepository.findById(module_id);
            if(m.getTeacher().getId() == user.getId()) {
                n.setName(titre_questionnaire);
                n.setViewed(true);
                n.setModule(m);
                n.setUser(user);
                n.setDate(new Date());
                n.setType("live");
                n.setOngoing(true);
                questionnaireRepository.save(n);
                return n.getId().toString();
            }
        }
        return "nok";
    }


    @PostMapping(path = "/vu") // URL /database/show
    @ResponseBody
    public String set_view(HttpServletRequest request, @RequestParam(value = "id_questionnaire", required = true) Integer id, Model model) {
        Questionnaire q = questionnaireRepository.findById(id);
        User user = userRepository.findByEmail(request.getRemoteUser());
        if(q.getUser().getId() == user.getId()) {
            q.setViewed(true);
            questionnaireRepository.save(q);
        }
        return "ok";
    }

    @PostMapping(path = "/changeStatus") // URL /database/show
    @ResponseBody
    public String changeStatus(HttpServletRequest request, @RequestParam(value = "id", required = true) Integer id,
                               @RequestParam(value = "bool", required = true) boolean bool,
                               Model model) {
        User user = userRepository.findByEmail(request.getRemoteUser());
        if(questionnaireRepository.exists(id)){
            Questionnaire q = questionnaireRepository.findById(id);
            if(q.getUser().getId() == user.getId()) {
                q.setOngoing(bool);
                questionnaireRepository.save(q);
                return "ok";
            }
        }
        return "nok";
    }

    @PostMapping(path = "/delete") // URL /database/show
    @ResponseBody
    public void delete(HttpServletRequest request, @RequestParam(value = "id", required = true) int id, Model model) {
        if(id > 0) {
            User user = userRepository.findByEmail(request.getRemoteUser());
            Questionnaire n = questionnaireRepository.findById(id);
            if(n.getUser().getId() == user.getId() || user.getRole().equals("admin")) {
                List<Question> questions = questionRepository.findByQuestionnaire_id(id);
                if(user.getRole().equals("teacher")) {
                    for (Question question : questions) {
                        questionRepository.delete(question);
                    }
                    if(templateRepository.findByQuestionnaire_id(id) != null){
                        templateRepository.delete(templateRepository.findByQuestionnaire_id(id));
                    }
                    questionnaireRepository.delete(n);
                }else{
                    n.setDeletedByAdmin(true);
                    questionnaireRepository.save(n);
                }
            }
        }
    }

}

