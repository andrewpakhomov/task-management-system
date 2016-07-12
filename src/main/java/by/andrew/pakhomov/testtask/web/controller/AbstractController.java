/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.andrew.pakhomov.testtask.web.controller;

import java.text.SimpleDateFormat;
import java.util.function.Predicate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Andrew P.
 */
public class AbstractController {

    private final String SYSTEM_ERROR_MODEL_KEY = "SYSTEM_ERROR";
    
    private final String FORM_ERRORS_LIST_KEY = "FORM_ERRORS_LIST";
   
    private final String INFO_MESSAGE_KEY = "INFO_MESSAGE";
     
    private final String WARNING_MESSAGE_KEY = "WARNING_MESSAGE"; 
    
    protected final static SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");
   
    protected final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    
    protected final SessionFactory sessionFactory;

    public AbstractController(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    protected void addSystemErrorMessage(Model model, String message){
        model.addAttribute(SYSTEM_ERROR_MODEL_KEY, message);
    }
    
    protected void addFormErrors(Model model, String ... messages){
        model.addAttribute(FORM_ERRORS_LIST_KEY, messages);
    }
    
     protected void addSystemErrorMessage(RedirectAttributes model, String message){
        model.addFlashAttribute(SYSTEM_ERROR_MODEL_KEY, message);
    }
    
    protected void addFormErrors(RedirectAttributes model, String ... messages){
        model.addFlashAttribute(FORM_ERRORS_LIST_KEY, messages);
    }
    
    protected void addInfoMessage(Model model, String message){
        model.addAttribute(INFO_MESSAGE_KEY, message);
    }
    
    protected void addInfoMessage(RedirectAttributes model, String message){
        model.addFlashAttribute(INFO_MESSAGE_KEY, message);
    }
    
    protected void addWarningMessage(Model model, String message){
         model.addAttribute(WARNING_MESSAGE_KEY, message);
    }
    
    protected void addWarningMessage(RedirectAttributes model, String message){
         model.addFlashAttribute(WARNING_MESSAGE_KEY, message);
    }
    
    
    protected boolean executeSafely(RedirectAttributes redirectAttributes, Predicate<Session> businessAction){
        Session session = this.sessionFactory.openSession();
        Transaction tx = null;
        boolean persistenceSuccess = false;
        try{
            tx = session.beginTransaction();
            persistenceSuccess = businessAction.test(session);
            if (persistenceSuccess){
                tx.commit();
            }else{
                tx.rollback();
            }
        }catch(HibernateException ex){
            persistenceSuccess = false;
            if (tx!=null){
                try{
                    tx.rollback();
                }catch (HibernateException ex1){
                    
                }
            }
            this.addSystemErrorMessage(redirectAttributes, "Ошибка обращения к БД:" + ex.getMessage());
        }finally{
            session.close();
        }
        return persistenceSuccess;
    }
    
    protected boolean executeSafely(Model model, Predicate<Session> businessAction){
        Session session = this.sessionFactory.openSession();
        Transaction tx = null;
        boolean persistenceSuccess = false;
        try{
            tx = session.beginTransaction();
            persistenceSuccess = businessAction.test(session);
            if (persistenceSuccess){
                tx.commit();
            }else{
                tx.rollback();
            }
        }catch(HibernateException ex){
            persistenceSuccess = false;
            if (tx!=null){
                try{
                    tx.rollback();
                }catch (HibernateException ex1){
                    
                }
            }
            this.addSystemErrorMessage(model, "Ошибка обращения к БД:" + ex.getMessage());
        }finally{
            session.close();
        }
        return persistenceSuccess;
    }
    
   
    
}
