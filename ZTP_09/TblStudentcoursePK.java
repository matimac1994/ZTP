/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ztp_09;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Mati
 * @version 1.0
 */
@Embeddable
public class TblStudentcoursePK implements Serializable {

    @Basic(optional = false)
    @Column(name = "STUDENTID")
    private int studentid;
    @Basic(optional = false)
    @Column(name = "COURSEID")
    private int courseid;

    /**
     * default
     */
    public TblStudentcoursePK() {
    }

    /**
     * @param studentid id
     * @param courseid id
     */
    public TblStudentcoursePK(int studentid, int courseid) {
        this.studentid = studentid;
        this.courseid = courseid;
    }

    /**
     * @return hashCode
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += studentid;
        hash += courseid;
        return hash;
    }

    /**
     * @param object obj
     * @return equals
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblStudentcoursePK)) {
            return false;
        }
        TblStudentcoursePK other = (TblStudentcoursePK) object;
        if (this.studentid != other.studentid) {
            return false;
        }
        if (this.courseid != other.courseid) {
            return false;
        }
        return true;
    }
    
}
