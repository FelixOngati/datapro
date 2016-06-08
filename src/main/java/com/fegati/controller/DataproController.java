package com.fegati.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by the_fegati on 6/7/16.
 */
@Controller
public class DataproController {
    @RequestMapping("/")
    public String index(){
        return "index";
    }
}
