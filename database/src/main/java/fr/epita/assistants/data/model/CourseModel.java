package fr.epita.assistants.data.model;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "course_model")
public class CourseModel {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id_course;

    @Column
    String name;

    @ElementCollection
    @CollectionTable(name = "course_model_tags", joinColumns = @JoinColumn(name="id_course"))
    List<String> tag;

    @OneToMany (targetEntity = StudentModel.class)
    List<StudentModel> liste = new ArrayList<>();

}
