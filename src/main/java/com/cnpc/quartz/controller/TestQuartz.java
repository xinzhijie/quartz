package com.cnpc.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by gongye1 on 2017/5/2.
 */
@Controller
public class TestQuartz {
    @ResponseBody
    @RequestMapping(value = "/world")
    public void hello(ModelMap modelMap) {
        System.out.println("111122");
    }

}
