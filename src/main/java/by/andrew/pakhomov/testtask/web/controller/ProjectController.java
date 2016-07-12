/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.andrew.pakhomov.testtask.web.controller;

import by.andrew.pakhomov.testtask.web.domain.Project;
import by.andrew.pakhomov.testtask.web.domain.Task;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
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
@RequestMapping("/projects")
public class ProjectController extends AbstractController {

    @Autowired
    public ProjectController(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public String renderProjectList(Model model) {
        this.executeSafely(model, (Session session) -> {
            this.populateModelWithProjectList(model, session);
            return true;
        });
        return "projects";
    }

    @RequestMapping(path = "/new", method = RequestMethod.POST)
    public String createNewProject(@RequestParam String title, @RequestParam String responsible,
            final Model model, final RedirectAttributes redirectAttrs) {
        title = title.trim();
        responsible = responsible.trim();
        List<String> errors = new ArrayList<>(2);
        if (title.isEmpty()) {
            errors.add("Заполните поле \"название проекта\"");
        }
        if (responsible.isEmpty()) {
            errors.add("Заполните поле \"Ответственный\"");
        }
        if (!errors.isEmpty()) {
            this.addFormErrors(redirectAttrs, errors.toArray(new String[errors.size()]));
            redirectAttrs.addFlashAttribute("title", title);
            redirectAttrs.addFlashAttribute("responsible", responsible);
            return "redirect:/projects";
        }

        Project project = new Project(title, responsible);
        final boolean saveResult = this.executeSafely(redirectAttrs, (Session session) -> {
            session.save(project);
            this.populateModelWithProjectList(model, session);
            return true;
        });
        if (saveResult) {
            this.addInfoMessage(model, "Проект №" + project.getId() + " успешно зарегистрирован");
        }else{
            redirectAttrs.addFlashAttribute("title", title);
            redirectAttrs.addFlashAttribute("responsible", responsible);
        }
        return "redirect:/projects";
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public String getProject(Model model, @PathVariable(value = "id") int id) {

        this.executeSafely(model, (Session session) -> {
            Project project = (Project) session.get(Project.class, id);
            model.addAttribute("project", project);
            return true;
        });

        return "project";
    }

    @RequestMapping(path = "/{id}/tasks/new", method = RequestMethod.POST)
    public String addTaskToProject(@RequestParam String title, @RequestParam String responsible, @PathVariable("id") int projectId, RedirectAttributes redirectAttrs) {
        final String taskTitle = title.trim();
        final String taskResponsible = responsible.trim();
        List<String> errors = new ArrayList<>(2);
        if (title.isEmpty()) {
            errors.add("Заполните поле \"Название задачи\"");
        }
        if (responsible.isEmpty()) {
            errors.add("Заполните поле \"Ответственный\"");
        }
        if (!errors.isEmpty()) {
            this.addFormErrors(redirectAttrs, errors.toArray(new String[errors.size()]));
            redirectAttrs.addFlashAttribute("title", title);
            redirectAttrs.addFlashAttribute("responsible", responsible);
            return "redirect:/projects/" + projectId;
        }

        boolean success = this.executeSafely(redirectAttrs, (Session session) -> {
            Project project = (Project) session.get(Project.class, projectId);
            if (project == null) {
                throw new ResourceNotFoundException();
            }
            Task task = project.addTask(taskTitle, taskResponsible);
            session.saveOrUpdate(project);
            this.addInfoMessage(redirectAttrs, "Задача "+task.getTitle()+"\" успешно зарегистрирована");
            return true;
        });
        
        if (!success){
            redirectAttrs.addFlashAttribute("title", title);
            redirectAttrs.addFlashAttribute("responsible", responsible);
        }
        
        return "redirect:/projects/" + projectId;
    }

    /**
     *
     * @param model
     * @param session
     * @throws HibernateException
     */
    private void populateModelWithProjectList(Model model, Session session) {
        List<Project> allProjects = null;
        Criteria cr = session.createCriteria(Project.class);
        allProjects = cr.list();

        if (allProjects == null) {
            allProjects = Collections.emptyList();
        }
        model.addAttribute("projects", allProjects);
    }

}
