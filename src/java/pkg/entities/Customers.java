/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pkg.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author vntru
 */
@Entity
@Table(name = "Customers", catalog = "FashionMan", schema = "dbo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Customers.findAll", query = "SELECT c FROM Customers c"),
    @NamedQuery(name = "Customers.findByEmail", query = "SELECT c FROM Customers c WHERE c.email = :email"),
    @NamedQuery(name = "Customers.findByPassword", query = "SELECT c FROM Customers c WHERE c.password = :password"),
    @NamedQuery(name = "Customers.findByFirstName", query = "SELECT c FROM Customers c WHERE c.firstName = :firstName"),
    @NamedQuery(name = "Customers.findByLastName", query = "SELECT c FROM Customers c WHERE c.lastName = :lastName"),
    @NamedQuery(name = "Customers.findByGender", query = "SELECT c FROM Customers c WHERE c.gender = :gender"),
    @NamedQuery(name = "Customers.findByPhone", query = "SELECT c FROM Customers c WHERE c.phone = :phone"),
    @NamedQuery(name = "Customers.findByCreatedDate", query = "SELECT c FROM Customers c WHERE c.createdDate = :createdDate"),
    @NamedQuery(name = "Customers.findByCustomerState", query = "SELECT c FROM Customers c WHERE c.customerState = :customerState")})
public class Customers implements Serializable {
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
    @Column(name = "FirstName", nullable = false, length = 50)
    private String firstName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "LastName", nullable = false, length = 50)
    private String lastName;
    @Column(name = "Gender")
    private Boolean gender;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 20)
    @Column(name = "Phone", length = 20)
    private String phone;
    @Size(max = 10)
    @Column(name = "CreatedDate", length = 10)
    private String createdDate;
    @Column(name = "CustomerState")
    private Boolean customerState;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customerEmail")
    private Collection<Orders> ordersCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customerEmail")
    private Collection<Wishlist> wishlistCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customerEmail")
    private Collection<Ratings> ratingsCollection;

    public Customers() {
    }

    public Customers(String email) {
        this.email = email;
    }

    public Customers(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean getCustomerState() {
        return customerState;
    }

    public void setCustomerState(Boolean customerState) {
        this.customerState = customerState;
    }

    @XmlTransient
    public Collection<Orders> getOrdersCollection() {
        return ordersCollection;
    }

    public void setOrdersCollection(Collection<Orders> ordersCollection) {
        this.ordersCollection = ordersCollection;
    }

    @XmlTransient
    public Collection<Wishlist> getWishlistCollection() {
        return wishlistCollection;
    }

    public void setWishlistCollection(Collection<Wishlist> wishlistCollection) {
        this.wishlistCollection = wishlistCollection;
    }

    @XmlTransient
    public Collection<Ratings> getRatingsCollection() {
        return ratingsCollection;
    }

    public void setRatingsCollection(Collection<Ratings> ratingsCollection) {
        this.ratingsCollection = ratingsCollection;
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
        if (!(object instanceof Customers)) {
            return false;
        }
        Customers other = (Customers) object;
        if ((this.email == null && other.email != null) || (this.email != null && !this.email.equals(other.email))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pkg.entities.Customers[ email=" + email + " ]";
    }
    
}
