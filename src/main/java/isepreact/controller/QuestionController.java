package isepreact.controller;

import isepreact.model.Answer;
import isepreact.model.Question;
import isepreact.model.Questionnaire;
import isepreact.model.User;
import isepreact.repository.UserRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import isepreact.repository.QuestionRepository;
import isepreact.repository.QuestionnaireRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


/**
 * Created by victo on 06/05/2017.
 */
@Controller         // This means that this class is a Controller
@RequestMapping(path = "/question")
public class QuestionController {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private QuestionnaireRepository questionnaireRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping(path = "/add_question") // URL /database/show
    @ResponseBody
    public String add_question(HttpServletRequest request, @RequestParam(value = "question", required = true) String question,
                               @RequestParam(value = "type", required = false) String type,
                               @RequestParam(value = "select", required = false) String select,
                               @RequestParam(value = "qcm", required = false) String qcm,
                               @RequestParam(value = "right_answers", required = false) String right_answers,
                               @RequestParam(value = "emojis", required = false) String emojis,
                               @RequestParam(value = "min_range", required = false) Double min_range,
                               @RequestParam(value = "max_range", required = false) Double max_range,
                               @RequestParam(value = "id_questionnaire", required = true) int id_questionnaire,
                               @RequestParam(value = "index", required = true) int index,
                               Model model) {
        User user = userRepository.findByEmail(request.getRemoteUser());
        if(questionnaireRepository.exists(id_questionnaire) && !type.equals("") && index > 0 && !question.equals("")) {
            Question q = new Question();
            q.setIndexQuestion(index);
            q.setQuestion(question);
            Questionnaire questionnaire = questionnaireRepository.findById(id_questionnaire);
            if(questionnaire.getUser().getId() == user.getId()) {
                q.setQuestionnaire(questionnaire);
                q.setType(type);
                q.setQcm(qcm);
                q.setRight_answers(right_answers);
                q.setSelectors(select);
                q.setEmojis(emojis);
                if (type.equals("notation")) {
                    q.setMin_grade(min_range);
                    q.setMax_grade(max_range);
                }
                questionRepository.save(q);
                return "ok";
            }
        }
        return "nok";
    }

    @PostMapping(path = "/getDataWordCloud") // URL /database/show
    @ResponseBody
    public String getDataWordCloud(HttpServletRequest request, @RequestParam(value = "id", required = true) Integer id,
                               Model model) {
        if(questionRepository.exists(id)){
            User user = userRepository.findByEmail(request.getRemoteUser());
            Question question = questionRepository.findById(id);
            if(user.getId() == question.getQuestionnaire().getUser().getId() || question.getQuestionnaire().getModule().isStudentInModule(user)) {
                HashMap<String, Integer> data = new HashMap<String, Integer>();
                ArrayList<Answer> listAnswers = new ArrayList(questionRepository.findById(id).getAnswers());
                Collections.sort(listAnswers);
                for (Answer a : listAnswers) {
                    if (data.containsKey(a.getAnswer())) {
                        data.replace(a.getAnswer(), (data.get(a.getAnswer()) + 1));
                    } else {
                        data.put(a.getAnswer(), 1);
                    }
                }
                String retour = "[";
                for (String key : data.keySet()) {
                    retour += "{\"text\": \"" + key + "\", \"weight\": " + data.get(key).toString() + "},";
                }
                if (retour.length() > 1) {
                    retour = retour.substring(0, retour.length() - 1);
                }
                retour += "]";
                return retour;
            }
        }
        return "nok";
    }

    @PostMapping(path = "/edit") // URL /database/show
    @ResponseBody
    public String edit(HttpServletRequest request, @RequestParam(value = "datas", required = true) String data,
                       @RequestParam(value = "id_questionnaire", required = true) Integer id_questionnaire,
                                   Model model) {
        User user = userRepository.findByEmail(request.getRemoteUser());
        if(questionnaireRepository.exists(id_questionnaire)){
            Questionnaire questionnaire = questionnaireRepository.findById(id_questionnaire);
            if(questionnaire.getUser().getId() == user.getId()){
                HashMap<String, Object> map = new HashMap<String, Object>();
                JSONArray jObject = new JSONArray(data);
                Set<Question> toDelete = new HashSet<Question>();;
                toDelete.addAll(questionnaire.getQuestions());
                for(int i=0;i<jObject.length();i++){
                    JSONObject json_data = jObject.getJSONObject(i);
                    Integer id = -1;
                    if(json_data.has("id") && !json_data.getString("id").equals("null")) {
                        id = Integer.parseInt(json_data.getString("id"));
                    }
                    String question =json_data.getString("question");
                    String type =json_data.getString("type");
                    String emojis =json_data.getString("emojis");
                    String select =json_data.getString("select");
                    String qcm =json_data.getString("qcm");
                    String right_answers =json_data.getString("right_answers");
                    Double max_range = 0.00;
                    Double min_range = 0.00;
                    if(type.equals("notation")) {
                        max_range = (json_data.getDouble("max_range"));
                        min_range = (json_data.getDouble("min_range"));
                    }
                    Integer index = json_data.getInt("index");
                    Question q;
                    if(questionRepository.exists(id)) {
                        q = questionRepository.findById(id);
                        if (q.getQuestionnaire().getUser().getId() == user.getId()) {
                            toDelete.remove(q);
                        }
                    }else{
                        q = new Question();
                        q.setQuestionnaire(questionnaire);
                    }
                    q.setIndexQuestion(index);
                    q.setQuestion(question);
                    q.setType(type);
                    q.setEmojis(emojis);
                    q.setSelectors(select);
                    q.setQcm(qcm);
                    q.setRight_answers(right_answers);
                    q.setMax_grade(max_range);
                    q.setMin_grade(min_range);
                    questionnaire.addQuestion(q);
                    questionRepository.save(q);
                }
                for (Iterator<Question> it = toDelete.iterator(); it.hasNext();) {
                    Question q = it.next();
                    it.remove();
                    System.out.println(q.getQuestion());
                    questionnaire.removeQuestion(q);
                    questionRepository.delete(q);
                }
                questionnaireRepository.save(questionnaire);
                return "ok";
            }
        }
        return "nok";
    }

    @PostMapping(path = "/add_question_live") // URL /database/show
    @ResponseBody
    public String add_question_live(HttpServletRequest request, @RequestParam(value = "question", required = true) String question,
                               @RequestParam(value = "type", required = false) String type,
                               @RequestParam(value = "select", required = false) String select,
                               @RequestParam(value = "qcm", required = false) String qcm,
                               @RequestParam(value = "right_answers", required = false) String right_answers,
                               @RequestParam(value = "emojis", required = false) String emojis,
                               @RequestParam(value = "min_range", required = false) Double min_range,
                               @RequestParam(value = "max_range", required = false) Double max_range,
                               @RequestParam(value = "id_questionnaire", required = true) int id_questionnaire,
                               @RequestParam(value = "instruction", required = false) String instruction,
                               @RequestParam(value = "index", required = true) int index,
                               Model model) {
        if(questionnaireRepository.exists(id_questionnaire) && !type.equals("") && index > 0 && !question.equals("")) {
            User user = userRepository.findByEmail(request.getRemoteUser());
            Question q = new Question();
            q.setIndexQuestion(index);
            q.setQuestion(question);
            Questionnaire questionnaire = questionnaireRepository.findById(id_questionnaire);
            if(questionnaire.getUser().getId() == user.getId()) {
                q.setQuestionnaire(questionnaire);
                q.setType(type);
                q.setQcm(qcm);
                q.setRight_answers(right_answers);
                q.setSelectors(select);
                q.setEmojis(emojis);
                q.setInstruction(instruction);
                if (type.equals("notation")) {
                    q.setMin_grade(min_range);
                    q.setMax_grade(max_range);
                }
                questionRepository.save(q);
                String ajout = "<div class=\"panel panel-primary panel-question\" data-id='" + q.getId().toString() + "'>\n" +
                        "                    <div class=\"panel-heading\"><strong>" + question + "</strong>\n" +
                        "                    </div>\n" +
                        "                    <div class=\"panel-body\">\n";
                if (!type.equals("instruction")) {
                    ajout += "                        <div class=\"col-md-2\">\n" +
                            "                        <span class=\"glyphicon glyphicon-signal\"\n" +
                            "                              aria-hidden=\"true\"></span><span\n" +
                            "                                class=\"info-questionnaire\"><span class='nbAnswer'>0</span> réponses</span>\n</div>";
                }
                ajout += "                        <div class=\"col-md-2\">\n" +
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
                } else if (type.equals("instruction")) {
                    ajout += "";
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
                } else if (type.equals("instruction")) {
                    ajout += "<div><pre>" + q.getInstruction() + "</pre></div>";
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

    @PostMapping(path = "/getGraphData") // URL /database/show
    @ResponseBody
    public String getGraphData(HttpServletRequest request, @RequestParam(value = "id_question", required = true) int id,
                               Model model) {
        User user = userRepository.findByEmail(request.getRemoteUser());
        if(questionRepository.exists(id)) {
            Question q = questionRepository.findById(id);
            if(q.getQuestionnaire().getUser().getId() == user.getId() || q.getQuestionnaire().getModule().isStudentInModule(user)) {
                int[] datas;
                String[] labels;
                List<String> backgroundColors = new ArrayList<String>();
                if (q.getType().equals("selecteur")) {
                    labels = q.getSelectorsInArray();
                    datas = new int[labels.length];
                    for (Answer a : q.getAnswers()) {
                        int place = q.getSelectorsPlace(a.getAnswer());
                        datas[place] += 1;
                    }
                } else if (q.getType().equals("qcm")) {
                    labels = q.getQcmsInArray();
                    datas = new int[labels.length];
                    for (String answer : q.getQcmsInArray()) {
                        int place = q.getQcmPlace(answer);
                        datas[place] += q.getNbAnswerForThisAnswer(answer);
                        if (q.isQcmWithRightAnswers()) {
                            if (q.isItRightQcmAnswer(answer)) {
                                backgroundColors.add("limegreen");
                            } else {
                                backgroundColors.add("indianred");
                            }
                        }
                    }
                } else if (q.getType().equals("notation")) {
                    Double min = q.getMin_grade();
                    Double max = q.getMax_grade();
                    labels = new String[(int) (max - min + 1)];
                    for (Double i = min; i <= max; i++) {
                        labels[(int) (i - min)] = String.valueOf(i);
                    }
                    datas = new int[labels.length];
                    for (Answer answer : q.getAnswers()) {
                        for (int i = 0; i < labels.length; i++) {
                            if (Double.parseDouble(labels[i]) == Double.parseDouble(answer.getAnswer())) {
                                datas[i] += 1;
                            }
                        }
                    }
                } else if (q.getType().equals("roti")) {
                    labels = new String[]{"1", "2", "3", "4", "5", "6"};
                    datas = new int[labels.length];
                    for (Answer answer : q.getAnswers()) {
                        for (int i = 0; i < labels.length; i++) {
                            if (Double.parseDouble(labels[i]) == Double.parseDouble(answer.getAnswer())) {
                                datas[i] += 1;
                            }
                        }
                    }
                } else if (q.getType().equals("slider")) {
                    String[] emojis = q.getEmojisInArray();
                    Map<String, Object> dataSlider = new HashMap<String, Object>();
                    for (String emoji : emojis) {
                        Object[] array = {q.getNbAnswerForThisEmoji(emoji), q.getPourcentageForThisEmoji(emoji)};
                        dataSlider.put(emoji, array);
                    }
                    JSONObject jsonSlider = new JSONObject(dataSlider);
                    return jsonSlider.toString();
                } else {
                    return "";
                }
                Map<String, Object> datasets = new HashMap<String, Object>();
                datasets.put("label", "Nombre de votes");
                datasets.put("data", Arrays.toString(datas).split("[\\[\\]]")[1].split(", "));
                if (!q.isQcmWithRightAnswers()) {
                    datasets.put("backgroundColor", "rgba(0, 97, 161, 0.9)");
                } else {
                    datasets.put("backgroundColor", Arrays.toString(backgroundColors.toArray()).split("[\\[\\]]")[1].split(", "));
                }
                datasets.put("borderColor", "rgba(244,161,0,1)");
                datasets.put("borderWidth", 1);
                Map<String, Object> retour = new HashMap<String, Object>();
                retour.put("labels", labels);
                Map<String, Object> datasetsArray = new HashMap<String, Object>();
                datasetsArray.put("datasets", datasets);
                retour.put("datasets", datasetsArray);
                JSONObject json = new JSONObject(retour);
                return json.toString();
            }
        }
        return "";
    }

    @PostMapping(path = "/getInfos") // URL /database/show
    @ResponseBody
    public String getInfos(HttpServletRequest request, @RequestParam(value = "id_question", required = true) int id,
                               Model model) {
        User user = userRepository.findByEmail(request.getRemoteUser());
        if(questionRepository.exists(id)) {
            Question q = questionRepository.findById(id);
            if(q.getQuestionnaire().getModule().isStudentInModule(user) || q.getQuestionnaire().getUser().getId() == user.getId()) {
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("nbAnswer", q.getAnswers().size());
                data.put("avg", q.getAvgAnswer());
                data.put("type", q.getType());
                JSONObject retour = new JSONObject(data);
                return retour.toString();
            }
        }
        return "";
    }
}
