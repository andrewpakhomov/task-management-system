/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.andrew.pakhomov.testtask.web.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author Andrew P.
 */
@Entity
public class SpentTimeRecord implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne
    @JoinColumn(name = "related_task_id")
    private Task relatedTask;
    
    @Column(name = "registration_date")
    private Date registrationDate;
    
    @Column (name = "spent_time_in_seconds")
    private Long spentTimeInSeconds;
    
    @Column(name = "employee")
    private String employee;

    protected  SpentTimeRecord() {
        //Hibernate default constructor
    }

    public SpentTimeRecord(Task relatedTask, Date registrationDate, Long spentTimeInSeconds, String employee) {
        this.registrationDate = registrationDate;
        this.spentTimeInSeconds = spentTimeInSeconds;
        this.employee = employee;
        this.relatedTask = relatedTask;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public Long getSpentTimeInSeconds() {
        return spentTimeInSeconds;
    }

    public int getId() {
        return id;
    }

    public String getEmployee() {
        return employee;
    }

}
