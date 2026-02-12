package fr.epita.assistants.data.model;


import jakarta.persistence.*;

@Entity
@Table(name = "student_model")
public class StudentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id_student;

    @Column
    String name;

    @ManyToOne @JoinColumn(name="id_course")
    CourseModel course;



}
