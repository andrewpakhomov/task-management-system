/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.andrew.pakhomov.testtask.web.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author Andrew P.
 */
@Entity
public class Task implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne
    @JoinColumn(name="project_id")
    private Project project;
    
    @Column(name = "tile")
    private String title;
    
    @Column (name = "responsible")
    private String responsible;
    
    @OneToMany(cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<SpentTimeRecord> spentTimeRecords;

    protected Task() {
        //hibernate defualt constructor
    }
    
    protected Task(Project project, String title, String responsible){
        this.project = project;
        this.title = title;
        this.responsible = responsible;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getResponsible() {
        return responsible;
    }

    public Set<SpentTimeRecord> getSpentTimeRecords() {
        return new HashSet<>(spentTimeRecords);
    }
    
    public SpentTimeRecord addSpentTimeRecord(Date registrationDate, Long timeSpent, String employee){
        SpentTimeRecord record = new SpentTimeRecord(this, registrationDate, timeSpent, employee);
        this.spentTimeRecords.add(record);
        return record;
    }
    
}
