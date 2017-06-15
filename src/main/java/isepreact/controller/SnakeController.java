package isepreact.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by victor on 11/06/2017.
 */
@Controller         // This means that this class is a Controller
@RequestMapping(path = "/snake")
public class SnakeController {
    @GetMapping(path = "")
    public String playSnake() {
        return "snake";
    }
}
