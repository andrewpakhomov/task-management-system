/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.andrew.pakhomov.testtask.web.controller;

import static by.andrew.pakhomov.testtask.web.controller.AbstractController.DATE_FORMAT;
import by.andrew.pakhomov.testtask.web.domain.SpentTimeRecord;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Andrew P.
 */

@Controller
@RequestMapping("/spenttime")
public class SpentTimeRecordController extends AbstractController{

    @Autowired
    public SpentTimeRecordController(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    
    @RequestMapping("/betweendates")
    public String getSpentTimeBetweenDates(@RequestParam(name = "startDate", required = false) String startDate, @RequestParam(name = "finishDate", required = false) String finishDate, Model model) {
        model.addAttribute("records", Collections.emptyList());
        if (startDate == null && finishDate == null) {
            return "report-spent-time-between-dates";
        }else{
            model.addAttribute("startDate", startDate);
            model.addAttribute("finishDate", finishDate);
        }

        List<String> errors = new ArrayList<>(2);

        Date startDateTime = null;
        Date finishDateTime = null;
        try {
            startDateTime = DATE_FORMAT.parse(startDate);
        } catch (ParseException ex) {
            errors.add("Укажите начальную дату генерации отчёта в формате гггг-мм-дд");
        }
        try {
            finishDateTime = DATE_FORMAT.parse(finishDate);
        } catch (ParseException ex) {
            errors.add("Укажите конечную дату генерации отчёта в формате гггг-мм-дд");
        }

        if (!errors.isEmpty()) {
            this.addFormErrors(model, errors.toArray(new String[errors.size()]));
            return "report-spent-time-between-dates";
        }

        final Date finalStartDate = startDateTime;
        final Date finalFinishDate = finishDateTime;

        this.executeSafely(model, (Session session) -> {
            Criteria cr = session.createCriteria(SpentTimeRecord.class);
            cr.add(Restrictions.between("registrationDate", finalStartDate, finalFinishDate));
            List<SpentTimeRecord> records = cr.list();
            model.addAttribute("records", records);
            return true;
        });
        
        return "report-spent-time-between-dates";
    }
    
    
    @RequestMapping("/bydevelopers")
    public String getSpentTimeBetweenDatesSumByDevelopers(@RequestParam(name = "startDate", required = false) String startDate, @RequestParam(name = "finishDate", required = false) String finishDate, Model model) {
        model.addAttribute("records", Collections.emptyMap());
        if (startDate == null && finishDate == null) {
            return "report-spent-time-by-developers";
        }else{
            model.addAttribute("startDate", startDate);
            model.addAttribute("finishDate", finishDate);
        }

        List<String> errors = new ArrayList<>(2);

        Date startDateTime = null;
        Date finishDateTime = null;
        try {
            startDateTime = DATE_FORMAT.parse(startDate);
        } catch (ParseException ex) {
            errors.add("Укажите начальную дату генерации отчёта в формате гггг-мм-дд");
        }
        try {
            finishDateTime = DATE_FORMAT.parse(finishDate);
        } catch (ParseException ex) {
            errors.add("Укажите конечную дату генерации отчёта в формате гггг-мм-дд");
        }

        if (!errors.isEmpty()) {
            this.addFormErrors(model, errors.toArray(new String[errors.size()]));
            return "report-spent-time-by-developers";
        }

        final Date finalStartDate = startDateTime;
        final Date finalFinishDate = finishDateTime;

        this.executeSafely(model, (Session session) -> {
            Criteria cr = session.createCriteria(SpentTimeRecord.class);
//            cr.add(Restrictions.between("registrationDate", finalStartDate, finalFinishDate));
            
            cr.setProjection(Projections.projectionList()
                    .add(Projections.sum("spentTimeInSeconds"))
                    .add(Projections.groupProperty("employee")));
            
            
            List<Object[]> records = cr.list();
            Map<String, Long> report = new HashMap<>();
            for (Object[] current : records){
                report.put((String)current[1], (long)current[0]);
            }
            
            model.addAttribute("records", report);
            return true;
        });

        return "report-spent-time-by-developers";
    }
    
}
