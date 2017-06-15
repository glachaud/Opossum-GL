package isepreact.controller;

import isepreact.controller.storage.FileSystemStorageService;
import isepreact.controller.storage.StorageFileNotFoundException;
import isepreact.controller.storage.StorageService;
import isepreact.model.Conversation;
import isepreact.model.User;
import isepreact.repository.ConversationRepository;
import isepreact.repository.ModuleRepository;
import isepreact.model.Module;
import isepreact.repository.UserRepository;
import isepreact.service.CreateNotificationsList;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by chrom on 06/05/2017.
 */
@Controller         // This means that this class is a Controller
@RequestMapping(path = "/module")
public class ModuleController {
    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private UserRepository studentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConversationRepository conversationRepository;

    private final StorageService storageService;

    @Autowired
    public ModuleController(StorageService storageService) {
        this.storageService = storageService;
    }

    ModuleController(ModuleRepository moduleRepository, UserRepository userRepository, StorageService storageService) {
        this.moduleRepository = moduleRepository;
        this.studentRepository = userRepository;
        this.storageService = storageService;
    }

    @PostMapping("/add")
    public String handleFileUpload(HttpServletRequest request, @RequestParam("file") MultipartFile file, @RequestParam("name") String moduleName,
                                   Model model) throws java.io.IOException {
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String date = DATE_FORMAT.format(new Date());
        Module module = new Module();
        module.setName(moduleName);
        User teacher = userRepository.findByEmail(request.getRemoteUser());
        module.setTeacher(teacher);
        module.setViewedByAdmin(true);

        if(file.isEmpty()) {
            moduleRepository.save(module);
            return showModuleSelector(request, model);
        }
        if(!file.getContentType().equals("text/csv") && !file.getContentType().equals("application/vnd.ms-excel")) {
            String fileError = "Mauvais type de fichier";
            model.addAttribute("fileError", fileError);
            return showModuleSelector(request, model);

        }
        String fileName = date + file.getOriginalFilename();
        storageService.storeFile(file, fileName);
        Resource csvFile = storageService.loadAsResource(fileName);
        File studentsFile = csvFile.getFile();
        List<String> lines = Files.readAllLines(studentsFile.toPath(), Charset.forName("Cp1252"));
        Iterator<String> iterator = lines.iterator();
        if(iterator.hasNext()) {
            String line = iterator.next();
        }
        while(iterator.hasNext()) {
            User user;
            String line = iterator.next();
            String[] contentsComma = line.split(",");
            String[] contentsSemiColon = line.split(";");
            String[] contents;
            if(contentsComma.length == 6) {
                contents = contentsComma;
            } else if (contentsSemiColon.length == 6) {
                contents = contentsSemiColon;
            } else {
                String fileError = "Le fichier n'a pas les bonnes colonnes ou n'est pas séparé par des virgules ou des points-virgules";
                model.addAttribute("fileError", fileError);
                return showModuleSelector(request, model);
            }
            Integer id = Integer.parseInt(contents[0].split(" ")[0]);
            String lastName = contents[1];
            String firstName = contents[2];
            String email = contents[5];
            user = userRepository.findById(id);
            if(user == null) {
                user = new User();
                user.setId(id);
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setActive(1);
                user.setEmail(email);
                String random = UUID.randomUUID().toString();
                user.setLdap(random);
                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                String password = bCryptPasswordEncoder.encode(random);
                user.setPassword(password);
                user.setRole("student");
                userRepository.save(user);
                user = userRepository.findById(id);
                module.addStudent(user);
            } else {
                module.addStudent(user);
            }
        }
        moduleRepository.save(module);
        return showModuleSelector(request, model);
    }

    /*@RequestMapping(path = "/add")
    @ResponseBody
    public String addModule(HttpServletRequest request, @RequestParam(value = "title") String title,
                            Model model) {
        User user = studentRepository.findByEmail(request.getRemoteUser());
        if(user.getRole().equals("teacher")) {
            Module module = new Module();
            *//*if(fichier !=""){
               module = readCsv(module);
            }*//*
            module.setName(title);
            module.setTeacher(user);
            moduleRepository.save(module);
            return module.getId().toString();
        }
        return "-1";
    }*/

    @RequestMapping(path = "/getRetour") // URL /database/add
    @ResponseBody
    public String getRetour(HttpServletRequest request, @RequestParam(value = "id") Integer module_id,Model model) {
        Module module = moduleRepository.findById(module_id);
        User user = userRepository.findByEmail(request.getRemoteUser());
        if(user.getId() == module.getTeacher().getId() || module.isStudentInModule(user) || user.getRole().equals("admin")) {
            Map<String, Object> datasetsArray = new HashMap<String, Object>();
            if(module.getNote() == null){
                datasetsArray.put("commentaire", "");
                datasetsArray.put("note", "");
            }else {
                datasetsArray.put("commentaire", module.getCommentaire());
                datasetsArray.put("note", module.getNote());
            }
            datasetsArray.put("titre", module.getName());
            JSONObject json = new JSONObject(datasetsArray);
            return json.toString();
        }
        return "";
    }

    @RequestMapping(path = "/notes")
    public String notesModules(HttpServletRequest request, Model model) {
        User user = studentRepository.findByEmail(request.getRemoteUser());
        if(user.getRole().equals("admin")) {
            List<Conversation> alertes = conversationRepository.findByOffensifTrueAndOffensifViewedByAdminFalseOrderByDatePourNotif();
            List<Module> notes = moduleRepository.findByViewedByAdminFalseOrderByDatePourNotif();
            CreateNotificationsList notificationsBuilder = new CreateNotificationsList(alertes, notes);
            HashMap<Integer, HashMap<String, String>> notifications = notificationsBuilder.getSortedList();
            model.addAttribute("notifications", notifications);
            model.addAttribute("user", user);
            model.addAttribute("modules", moduleRepository.findByDatePourNotifIsNotNullOrderByDatePourNotifDesc());
            return "notation/liste_notations";
        }
        return "403";
    }

    @RequestMapping(path = "/view/{id_module}")
    public String view(HttpServletRequest request, @PathVariable(value="id_module") Integer id,
                       Model model){
        User user = userRepository.findByEmail(request.getRemoteUser());
        if(user.getRole().equals("admin")){
            Module module = moduleRepository.findById(id);
            module.setViewedByAdmin(true);
            moduleRepository.save(module);
            List<Conversation> alertes = conversationRepository.findByOffensifTrueAndOffensifViewedByAdminFalseOrderByDatePourNotif();
            List<Module> notes = moduleRepository.findByViewedByAdminFalseOrderByDatePourNotif();
            CreateNotificationsList notificationsBuilder = new CreateNotificationsList(alertes, notes);
            HashMap<Integer, HashMap<String, String>> notifications = notificationsBuilder.getSortedList();
            model.addAttribute("notifications", notifications);
            model.addAttribute("module", module);
            model.addAttribute("user", user);
            return "notation/notation_admin";
        }
        return "403";
    }

    @RequestMapping(path = "/delete")
    @ResponseBody
    public String addModule(HttpServletRequest request, @RequestParam(value = "id") Integer id, Model model) {
        User user = studentRepository.findByEmail(request.getRemoteUser());
        Module module = moduleRepository.findById(id);
        if(module.getTeacher().getId() == user.getId()) {
            moduleRepository.delete(id);
        }
        return "module/module_selector";
    }
    @RequestMapping(path = "/selector")
    public String showModuleSelector(HttpServletRequest request, Model model) {
        User user = studentRepository.findByEmail(request.getRemoteUser());
        if(user.getRole().equals("teacher")) {
            model.addAttribute("modules", moduleRepository.findByTeacher_id(user.getId()));
            return "module/module_selector";
        }
        return "403";
    }


    @RequestMapping(path = "/getAutocomplete")
    @ResponseBody
    public String getAutocomplete(HttpServletRequest request, @RequestParam(value = "id") Integer id, Model model) {
        User user = studentRepository.findByEmail(request.getRemoteUser());
        Module module = moduleRepository.findById(id);
        if(module.getTeacher().getId() == user.getId()) {
            String retour = "";
            int i = 0;
            for(User student : module.getStudents()){
                retour += student.getFirstName() + " " + student.getLastName() + " (" + student.getId().toString() + ")";
                if(i < module.getStudents().size()){
                    retour += ",";
                }
            }
            return retour;
        }
        return "nok";
    }

    @RequestMapping(path = "/deleteStudent")
    @ResponseBody
    public String deleteStudent(HttpServletRequest request, @RequestParam(value = "id") Integer id, @RequestParam(value = "numero") Integer numero, Model model) {
        User user = studentRepository.findByEmail(request.getRemoteUser());
        Module module = moduleRepository.findById(id);
        if(module.getTeacher().getId() == user.getId()) {
            User student = userRepository.findById(numero);
            if(module.isStudentInModule(student)){
                module.removeStudent(student);
                moduleRepository.save(module);
                return "ok";
            }
        }
        return "nok";
    }

    @RequestMapping(path = "/addStudent")
    @ResponseBody
    public String addStudent(HttpServletRequest request, @RequestParam(value = "id") Integer id, @RequestParam(value = "numero") Integer numero, Model model) {
        User user = studentRepository.findByEmail(request.getRemoteUser());
        Module module = moduleRepository.findById(id);
        if(module.getTeacher().getId() == user.getId()) {
            User student;
            if(!userRepository.exists(numero)){
                return "nok";
            }
            student = userRepository.findById(numero);
            if(!module.isStudentInModule(student)){
                module.addStudent(student);
                moduleRepository.save(module);
                return "ok";
            }
        }
        return "nok";
    }

    @RequestMapping(path = "/changeName")
    @ResponseBody
    public String changeName(HttpServletRequest request, @RequestParam(value = "id") Integer id, @RequestParam(value = "nom") String nom, Model model) {
        User user = studentRepository.findByEmail(request.getRemoteUser());
        Module module = moduleRepository.findById(id);
        if(module.getTeacher().getId() == user.getId()) {
            module.setName(nom);
            moduleRepository.save(module);
            return "ok";
        }
        return "nok";
    }

    /*public Module readCsv(Module module){
        String csvFile="C:/Users/chrom/Downloads/test.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        List<Integer> numeros = new ArrayList();
        try {

            br = new BufferedReader(new FileReader(csvFile));
            br.readLine();
            while ((line = br.readLine()) != null) {
                int i =0;
                String numberStr = "";
                while (line.charAt(i) != ' '){
                    numberStr += line.charAt(i );
                    i += 1;
                }
                Integer number = Integer.parseInt(numberStr);
                numeros.add(number);
            }
            for (Integer numero : numeros){
                User student = new User();
                student.setFirstName();
                student.setLastName();
                student.setEmail();
                student.setActive(1);
                studentRepository.save(student);
                module.addStudent(student);

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return module;
    }*/

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}

