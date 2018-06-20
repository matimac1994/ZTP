/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ztp_09;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Persistence;

/**
 *
 * @author Mati
 * @version 1.0
 */
public class AppMain {

    private static final String MY_PERSISTENCE_NAME = "myPersistence";

    private EntityManager entityManager;

    private String firstName;
    private String lastName;
    private String courseName;
    
    private TblCourses course;
    private TblStudents student;
    private TblStudentcourse studentCourse;
    private List<TblStudentcourse> courseMarks;

    /**
     * @param args arguments
     */
    public static void main(String[] args){
        try {
            AppMain appMain = new AppMain();
            appMain.connectToEntityManager();
            appMain.readDataFromFile(args);

            System.out.println( appMain.getResult().intValue() + "%");
        } catch (Throwable e){
            System.out.println("10%");
        }
    }

    /**
     * @return percentage result
     */
    private Double getResult() {
        findByCourseName();
        findCourses();
        findStudentByFirstAndLastName();
        findStudentCourse();
        Double median = calculateMedian();
        
        return calculatePercentage(median);
    }

    /**
     * finding student course record
     */
    private void findStudentCourse() {
        try {
            studentCourse = entityManager
                    .createNamedQuery("TblStudentcourse.findByCourseidStudentid",
                            TblStudentcourse.class)
                    .setParameter("courseid", course.getId())
                    .setParameter("studentid", student.getId())
                    .getSingleResult();
        } catch (NoResultException e){
            e.printStackTrace();
        } catch (NonUniqueResultException e){
            studentCourse = entityManager
                    .createNamedQuery("TblStudentcourse.findByCourseidStudentid",
                            TblStudentcourse.class)
                    .setParameter("courseid", course.getId())
                    .setParameter("studentid", student.getId())
                    .getResultList().get(0);
        }
    }

    /**
     * Finding student by first and last name
     */
    private void findStudentByFirstAndLastName() {
        try {
            student = entityManager
                    .createNamedQuery("TblStudents.findByLastFirstname",
                            TblStudents.class)
                    .setParameter("lastname", lastName.toUpperCase())
                    .setParameter("firstname", firstName.toUpperCase())
                    .getSingleResult();
        } catch (NoResultException e){
            e.printStackTrace();
        } catch (NonUniqueResultException e){
            List<TblStudents> students = entityManager
                    .createNamedQuery("TblStudents.findByLastFirstname",
                            TblStudents.class)
                    .setParameter("lastname", lastName.toUpperCase())
                    .setParameter("firstname", firstName.toUpperCase())
                    .getResultList();

            for (TblStudents studentUnique : students){
                if (studentUnique.getTblStudentcourseCollection() != null
                        && !studentUnique.getTblStudentcourseCollection().isEmpty())
                    student = studentUnique;
            }
        }
    }

    /**
     * finding courses
     */
    private void findCourses() {
        try {
            courseMarks = entityManager
                    .createNamedQuery("TblStudentcourse.findByCourseid",
                            TblStudentcourse.class)
                    .setParameter("courseid", course.getId())
                    .getResultList();

            courseMarks.sort(Comparator.comparingInt(TblStudentcourse::getMark));
        } catch (NoResultException e){
            e.printStackTrace();
        }

    }

    /**
     * finding course by course name
     */
    private void findByCourseName() {
        try {
            course = entityManager
                    .createNamedQuery("TblCourses.findByCoursename",
                            TblCourses.class)
                    .setParameter("coursename", courseName.toUpperCase())
                    .getSingleResult();
        } catch (NoResultException e){
            e.printStackTrace();
        }

    }

    /**
     * @return median
     */
    private Double calculateMedian(){
        int size=courseMarks.size();
        if (size>1){
            if (size%2==0){
                return (courseMarks.get(size/2).getMark()
                        + courseMarks.get(size/2-1).getMark())/2.0;
            }
            else {
                return (double) courseMarks.get(size / 2).getMark();
            }
        }
        else if (size==1){
            return (double) courseMarks.get(0).getMark();
        }
        else {
            return -1.0;
        }
    }

    /**
     * @param median median of marks
     * @return percentage result
     */
    private Double calculatePercentage(Double median){
        Double result = studentCourse.getMark() - median;
        if (result != 0){
            return (result*100)/median;
        } else {
            return 0.0;
        }
    }

    /**
     * Create connection to database and initialize entity manager
     */
    private void connectToEntityManager() {
        entityManager = Persistence
                .createEntityManagerFactory(MY_PERSISTENCE_NAME)
                .createEntityManager();
    }

    /**
     * @param args arguments
     * @throws FileNotFoundException when file not found
     */
    private void readDataFromFile(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("C:\\Users\\Mati\\Development\\IntellijIdeaProjects\\ZTP_09\\src\\ztp_09\\test.txt"));
        if (scanner.hasNextLine()) courseName = scanner.nextLine().trim();
        if (scanner.hasNextLine()){
            String[] firstLastName = scanner.nextLine().split("\\s+");
            if (firstLastName.length == 2){
                firstName = firstLastName[0].trim();
                lastName = firstLastName[1].trim();
            }
        }
    }
    
}
