/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ztp_09;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Mati
 * @version 1.0
 */
@Entity
@Table(name = "TBL_STUDENTCOURSE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblStudentcourse.findAll", query = "SELECT t FROM TblStudentcourse t")
    , @NamedQuery(name = "TblStudentcourse.findByStudentid",
        query = "SELECT t FROM TblStudentcourse t WHERE t.tblStudentcoursePK.studentid = :studentid")
    , @NamedQuery(name = "TblStudentcourse.findByCourseid",
        query = "SELECT t FROM TblStudentcourse t WHERE t.tblStudentcoursePK.courseid = :courseid")
    , @NamedQuery(name = "TblStudentcourse.findByCourseidStudentid",
        query = "SELECT t FROM TblStudentcourse t " +
                "WHERE t.tblStudentcoursePK.courseid = :courseid " +
                "AND t.tblStudentcoursePK.studentid = :studentid")
    , @NamedQuery(name = "TblStudentcourse.findByMark",
        query = "SELECT t FROM TblStudentcourse t WHERE t.mark = :mark")})
public class TblStudentcourse implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TblStudentcoursePK tblStudentcoursePK;
    @Basic(optional = false)
    @Column(name = "MARK")
    private int mark;
    @JoinColumn(name = "COURSEID", referencedColumnName = "ID",
            insertable = false,
            updatable = false)
    @ManyToOne(optional = false)
    private TblCourses tblCourses;
    @JoinColumn(name = "STUDENTID", referencedColumnName = "ID",
            insertable = false,
            updatable = false)
    @ManyToOne(optional = false)
    private TblStudents tblStudents;

    /**
     * default constructor
     */
    public TblStudentcourse() {
    }

    /**
     * @param tblStudentcoursePK  pk
     */
    public TblStudentcourse(TblStudentcoursePK tblStudentcoursePK) {
        this.tblStudentcoursePK = tblStudentcoursePK;
    }

    /**
     * @param tblStudentcoursePK student
     * @param mark mark
     */
    public TblStudentcourse(TblStudentcoursePK tblStudentcoursePK, int mark) {
        this.tblStudentcoursePK = tblStudentcoursePK;
        this.mark = mark;
    }

    /**
     * @param studentid id
     * @param courseid id
     */
    public TblStudentcourse(int studentid, int courseid) {
        this.tblStudentcoursePK = new TblStudentcoursePK(studentid, courseid);
    }

    /**
     * @return student
     */
    public TblStudentcoursePK getTblStudentcoursePK() {
        return tblStudentcoursePK;
    }

    /**
     * @param tblStudentcoursePK student
     */
    public void setTblStudentcoursePK(TblStudentcoursePK tblStudentcoursePK) {
        this.tblStudentcoursePK = tblStudentcoursePK;
    }

    /**
     * @return mark
     */
    public int getMark() {
        return mark;
    }

    /**
     * @return hashCode
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tblStudentcoursePK != null ? tblStudentcoursePK.hashCode() : 0);
        return hash;
    }

    /**
     * @param object obj
     * @return equals
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TblStudentcourse)) {
            return false;
        }
        TblStudentcourse other = (TblStudentcourse) object;
        if ((this.tblStudentcoursePK == null
                && other.tblStudentcoursePK != null)
                || (this.tblStudentcoursePK != null
                && !this.tblStudentcoursePK.equals(other.tblStudentcoursePK))) {
            return false;
        }
        return true;
    }
}
