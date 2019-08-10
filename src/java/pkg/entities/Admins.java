/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pkg.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author vntru
 */
@Entity
@Table(name = "Admins", catalog = "FashionMan", schema = "dbo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Admins.findAll", query = "SELECT a FROM Admins a"),
    @NamedQuery(name = "Admins.findByEmail", query = "SELECT a FROM Admins a WHERE a.email = :email"),
    @NamedQuery(name = "Admins.findByPassword", query = "SELECT a FROM Admins a WHERE a.password = :password"),
    @NamedQuery(name = "Admins.findByFullName", query = "SELECT a FROM Admins a WHERE a.fullName = :fullName"),
    @NamedQuery(name = "Admins.findByCreatedDate", query = "SELECT a FROM Admins a WHERE a.createdDate = :createdDate"),
    @NamedQuery(name = "Admins.findByAdminState", query = "SELECT a FROM Admins a WHERE a.adminState = :adminState")})
public class Admins implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "Email", nullable = false, length = 100)
    private String email;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "Password", nullable = false, length = 20)
    private String password;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "FullName", nullable = false, length = 50)
    private String fullName;
    @Size(max = 10)
    @Column(name = "CreatedDate", length = 10)
    private String createdDate;
    @Column(name = "AdminState")
    private Boolean adminState;

    public Admins() {
    }

    public Admins(String email) {
        this.email = email;
    }

    public Admins(String email, String password, String fullName) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean getAdminState() {
        return adminState;
    }

    public void setAdminState(Boolean adminState) {
        this.adminState = adminState;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (email != null ? email.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Admins)) {
            return false;
        }
        Admins other = (Admins) object;
        if ((this.email == null && other.email != null) || (this.email != null && !this.email.equals(other.email))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pkg.entities.Admins[ email=" + email + " ]";
    }
    
}
