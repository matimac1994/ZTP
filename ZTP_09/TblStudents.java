/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ztp_09;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Mati
 * @version 1.0
 */
@Entity
@Table(name = "TBL_STUDENTS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblStudents.findAll",
            query = "SELECT t FROM TblStudents t")
    , @NamedQuery(name = "TblStudents.findById",
        query = "SELECT t FROM TblStudents t WHERE t.id = :id")
    , @NamedQuery(name = "TblStudents.findByFirstname",
        query = "SELECT t FROM TblStudents t WHERE t.firstname = :firstname")
    , @NamedQuery(name = "TblStudents.findByLastname",
        query = "SELECT t FROM TblStudents t WHERE t.lastname = :lastname")
    , @NamedQuery(name = "TblStudents.findByLastFirstname",
        query = "SELECT t FROM TblStudents t " +
                "WHERE upper(t.lastname) = :lastname " +
                "AND upper(t.firstname) = :firstname")})
public class TblStudents implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "FIRSTNAME")
    private String firstname;
    @Column(name = "LASTNAME")
    private String lastname;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tblStudents")
    private Collection<TblStudentcourse> tblStudentcourseCollection;

    /**
     * default
     */
    public TblStudents() {
    }

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @return collection
     */
    @XmlTransient
    public Collection<TblStudentcourse> getTblStudentcourseCollection() {
        return tblStudentcourseCollection;
    }

    /**
     * @return hash
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /**
     * @param object obj
     * @return bool
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TblStudents)) {
            return false;
        }
        TblStudents other = (TblStudents) object;
        if ((this.id == null && other.id != null)
                || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
}
