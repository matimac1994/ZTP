/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ztp_09;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Mati
 * @version 1.0
 */
@Entity
@Table(name = "TBL_COURSES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblCourses.findAll",
            query = "SELECT t FROM TblCourses t")
    , @NamedQuery(name = "TblCourses.findById",
        query = "SELECT t FROM TblCourses t WHERE t.id = :id")
    , @NamedQuery(name = "TblCourses.findByCoursename",
        query = "SELECT t FROM TblCourses t WHERE upper(t.coursename) = :coursename")})
public class TblCourses implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "COURSENAME")
    private String coursename;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tblCourses")
    private Collection<TblStudentcourse> tblStudentcourseCollection;

    /**
     * default constructor
     */
    public TblCourses() {
    }

    /**
     * @param id id
     */
    public TblCourses(Integer id) {
        this.id = id;
    }

    /**
     * @param id id
     * @param coursename course name
     */
    public TblCourses(Integer id, String coursename) {
        this.id = id;
        this.coursename = coursename;
    }

    /**
     * @return id of course
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return course name
     */
    public String getCoursename() {
        return coursename;
    }

    /**
     * @param coursename course name
     */
    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    /**
     * @return collection of student courses
     */
    @XmlTransient
    public Collection<TblStudentcourse> getTblStudentcourseCollection() {
        return tblStudentcourseCollection;
    }

    /**
     * @param tblStudentcourseCollection collection
     */
    public void setTblStudentcourseCollection(Collection<TblStudentcourse> tblStudentcourseCollection) {
        this.tblStudentcourseCollection = tblStudentcourseCollection;
    }

    /**
     * @return hashCode
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /**
     * @param object obj
     * @return boolean
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblCourses)) {
            return false;
        }
        TblCourses other = (TblCourses) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
}
