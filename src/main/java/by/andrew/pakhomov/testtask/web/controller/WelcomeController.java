/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.andrew.pakhomov.testtask.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Andrew P.
 */
@RequestMapping("/")
@Controller
public class WelcomeController {
    
    @RequestMapping("")
    public String welcome(){
        return "redirect:/projects";
    }
    
}
