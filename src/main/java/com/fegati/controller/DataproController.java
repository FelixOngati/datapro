package com.fegati.controller;

import com.fegati.utilities.CSVPreprocessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by the_fegati on 6/7/16.
 */
@Controller
public class DataproController {
    CSVPreprocessor preprocessor = new CSVPreprocessor();

    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping("/data")
    public String getdata(Model model){
        List<String[]> data = new ArrayList<>();
        try {
           data = preprocessor.execute("/home/the_fegati/workspace/datapro/data/test.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.addAttribute("data",data);
        model.addAttribute("header",preprocessor.getHeader());
        return "index";
    }
}
