/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.andrew.pakhomov.testtask.web.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class Project implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "title")
    private String title;
    
    @Column (name = "responsible")
    private String responsible;
    
    @OneToMany(cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Task> tasks;
    
    protected Project(){
        //Hibernate default constructor
    }

    public Project(String title, String responsible) {
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
    
    public Task addTask(String title, String responsible){
        Task task = new Task(this, title, responsible);
        this.tasks.add(task);
        return task;
    }
    
    public Set<Task> getTasks(){
        return new HashSet(this.tasks);
    }
    
}
