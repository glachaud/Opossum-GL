package isepreact.controller;

import isepreact.controller.storage.StorageFileNotFoundException;
import isepreact.controller.storage.StorageService;
import isepreact.model.User;
import isepreact.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Created by guillaumelachaud on 5/29/17.
 */

@Controller
public class FileUploadController {

  private final StorageService storageService;

  @Autowired
  UserRepository userRepository;
  @Autowired
  public FileUploadController(StorageService storageService) {
    this.storageService = storageService;
  }

  @GetMapping("/uploadFile")
  public String listUploadedFiles(Model model) throws IOException {

    model.addAttribute("files", storageService
            .loadAll()
            .map(path ->
                    MvcUriComponentsBuilder
                            .fromMethodName(FileUploadController.class, "serveFile", path.getFileName().toString())
                            .build().toString())
            .collect(Collectors.toList()));

    return "uploadForm";
  }

  @GetMapping("/files/{filename:.+}")
  @ResponseBody
  public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

    Resource file = storageService.loadAsResource(filename);
    return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+file.getFilename()+"\"")
            .body(file);
  }

  @RequestMapping(path = "/getFile/{file}") // URL /database/add
  @ResponseBody
  public String getFilePath(@PathVariable String file) throws IOException{
    Resource resourceFile = storageService.loadAsResource(file);
    return resourceFile.getFile().getPath();
  }

  @PostMapping("/")
  public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                 RedirectAttributes redirectAttributes) {

    storageService.store(file);
    redirectAttributes.addFlashAttribute("message",
            "You successfully uploaded " + file.getOriginalFilename() + "!");

    return "redirect:/";
  }

  @RequestMapping(value = "/uploadPhoto", method = RequestMethod.POST)
  @ResponseBody
  public String uploadPhoto(HttpServletRequest request,
                            @RequestParam("uploadfile") MultipartFile uploadfile) {
    SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    String date = DATE_FORMAT.format(new Date());
    String fileName = date + uploadfile.getOriginalFilename();
    storageService.storeFile(uploadfile, fileName);
    User user = userRepository.findByEmail(request.getRemoteUser());
    user.setPhoto(fileName);
    userRepository.save(user);
    return "ok";
  }

  @ExceptionHandler(StorageFileNotFoundException.class)
  public ResponseEntity handleStorageFileNotFound(StorageFileNotFoundException exc) {
    return ResponseEntity.notFound().build();
  }

}
