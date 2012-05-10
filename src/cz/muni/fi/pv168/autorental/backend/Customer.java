package cz.muni.fi.pv168.autorental.backend;

import java.sql.Date;

/**
 * This entity represents Customer.
 * Car has some first name, last name, birth and email
 * 
 */
public class Customer {
    
    private Long id;
    private String firstname;
    private String lastname;
    private Date birth;
    private String email;

    public Customer() {
    }

    public Date getBirth() {
        return (Date)birth.clone();
    }

    public void setBirth(Date birth) {
        this.birth = new Date(birth.getTime());
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Customer other = (Customer) obj;
        if ((this.firstname == null) ? (other.firstname != null) : !this.firstname.equals(other.firstname)) {
            return false;
        }
        if ((this.lastname == null) ? (other.lastname != null) : !this.lastname.equals(other.lastname)) {
            return false;
        }
        if (this.birth != other.birth && (this.birth == null || !this.birth.equals(other.birth))) {
            return false;
        }
        if ((this.email == null) ? (other.email != null) : !this.email.equals(other.email)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (this.firstname != null ? this.firstname.hashCode() : 0);
        hash = 71 * hash + (this.lastname != null ? this.lastname.hashCode() : 0);
        hash = 71 * hash + (this.birth != null ? this.birth.hashCode() : 0);
        hash = 71 * hash + (this.email != null ? this.email.hashCode() : 0);
        return hash;
    }

     @Override
    public String toString() {
	return firstname + " " + lastname + " (" + birth + ") [" + email + ']';
    }

}
