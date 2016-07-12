/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.andrew.pakhomov.testtask.web.view;

import org.springframework.stereotype.Component;

/**
 *
 * @author Andrew P.
 */
@Component(value = "datesHelper")
public class DatesHelper {
    
    
    public String formatSecondsAsTotalHoursAndMinutes(Long seconds){
        int hh = (int) (seconds / 3600);
        int mm = (int)  ((seconds - hh*3600) / 60);
        return String.format("%02d:%02d",hh,mm );
    }
    
}
