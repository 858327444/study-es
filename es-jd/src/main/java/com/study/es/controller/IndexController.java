package com.study.es.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Program Name: es-study-api
 * Created by yanlp on 2021-01-03
 *
 * @author yanlp
 * @version 1.0
 */
@Controller
@RequestMapping("/index")
public class IndexController {

    @GetMapping("/index")
    public String index() {
        return "index";
    }

}
