/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.andrew.pakhomov.testtask.web.controller;

import by.andrew.pakhomov.testtask.web.domain.SpentTimeRecord;
import by.andrew.pakhomov.testtask.web.domain.Task;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Andrew P.
 */
@Controller
@RequestMapping("/tasks")
public class TaskController extends AbstractController {

    @Autowired
    public TaskController(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @RequestMapping("/{id}")
    public String getTask(@PathVariable("id") int id, Model model) {
        this.executeSafely(model, (Session session) -> {
            Task task = (Task) session.get(Task.class, id);
            if (task == null) {
                throw new ResourceNotFoundException();
            }
            model.addAttribute("task", task);
            return true;
        });
        return "task";
    }

    @RequestMapping(path = "/{id}/add", method = RequestMethod.POST)
    public String addTimeSpentRecord(@PathVariable("id") int taskId, @RequestParam("spentTime") String spentTime, @RequestParam("employee") String employee, @RequestParam("registrationDate") String registrationDate, RedirectAttributes redirectAttrs) {
        List<String> errors = new ArrayList<>(3);
        Date registrationDateTime = null;

        String[] timeParts = spentTime.split(":");
        int hh = 0;
        int mm = 0;
        if (timeParts.length == 2) {
            try {
                hh = Integer.valueOf(timeParts[0]);
                mm = Integer.valueOf(timeParts[1]);
                if (mm > 59) {
                    errors.add("Укажите затраченное время в формате чч:мм");
                }
            } catch (NumberFormatException ex) {
                errors.add("Укажите затраченное время в формате чч:мм");
            }
        } else {
            errors.add("Укажите затраченное время в формате чч:мм");
        }

        try {
            registrationDateTime = DATE_FORMAT.parse(registrationDate);
        } catch (ParseException ex) {
            errors.add("Укажите время регистрации в формате гггг-мм-дд");
        }
        if (employee.isEmpty()) {
            errors.add("Заполните поле \"Сотрудник\"");
        }
        if (!errors.isEmpty()) {
            this.addFormErrors(redirectAttrs, errors.toArray(new String[errors.size()]));
            redirectAttrs.addFlashAttribute("registrationDate", registrationDate);
            redirectAttrs.addFlashAttribute("employee", employee);
            redirectAttrs.addFlashAttribute("spentTime", spentTime);
            return "redirect:/tasks/" + taskId;
        }

        final String spentTimeEmployee = employee.trim();
        final Long finalSpentTime = hh * 3600L + mm * 60;
        final Date finalRegistrationDate = registrationDateTime;

        boolean success = this.executeSafely(redirectAttrs, (Session session) -> {
            Task task = (Task) session.get(Task.class, taskId);
            if (task == null) {
                throw new ResourceNotFoundException();
            }
            SpentTimeRecord record = task.addSpentTimeRecord(finalRegistrationDate, finalSpentTime, spentTimeEmployee);
            session.saveOrUpdate(task);
            this.addInfoMessage(redirectAttrs, "Запись затраченного времени для сотрудника " + record.getEmployee() + " на дату " + DATE_FORMAT.format(record.getRegistrationDate()) + " зарегистрирована");
            return true;
        });
        
        if (! success){
            redirectAttrs.addFlashAttribute("registrationDate", registrationDate);
            redirectAttrs.addFlashAttribute("employee", employee);
            redirectAttrs.addFlashAttribute("spentTime", spentTime);
        }

        return "redirect:/tasks/" + taskId;
    }
}
