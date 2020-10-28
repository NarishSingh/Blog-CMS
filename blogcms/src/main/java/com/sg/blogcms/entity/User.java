package com.sg.blogcms.entity;

import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class User {

    private int id;

    @NotBlank(message = "Username cannot be blank")
    @Size(max = 50, message = "Max of 30 characters for username")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(max = 100, message = "Password must be less than 50 characters in length")
    private String password;

    @NotNull(message = "Please indicate account status")
    private boolean enabled;

    @Size(max = 50, message = "First name must be less than 50 characters in length")
    private String firstName;

    @Size(max = 50, message = "Last name must be less than 50 characters in length")
    private String lastName;

    @Size(max = 50, message = "Email must be well formed, and less than 50 characters in length")
    private String email;

    @Size(max = 255)
    private String photoFilename;
    
    private Set<Role> roles;

    /*gs*/
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoFilename() {
        return photoFilename;
    }

    public void setPhotoFilename(String photoFilename) {
        this.photoFilename = photoFilename;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    /*testing*/
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.id;
        hash = 97 * hash + Objects.hashCode(this.username);
        hash = 97 * hash + Objects.hashCode(this.password);
        hash = 97 * hash + (this.enabled ? 1 : 0);
        hash = 97 * hash + Objects.hashCode(this.firstName);
        hash = 97 * hash + Objects.hashCode(this.lastName);
        hash = 97 * hash + Objects.hashCode(this.email);
        hash = 97 * hash + Objects.hashCode(this.photoFilename);
        hash = 97 * hash + Objects.hashCode(this.roles);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.enabled != other.enabled) {
            return false;
        }
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        if (!Objects.equals(this.firstName, other.firstName)) {
            return false;
        }
        if (!Objects.equals(this.lastName, other.lastName)) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        if (!Objects.equals(this.photoFilename, other.photoFilename)) {
            return false;
        }
        if (!Objects.equals(this.roles, other.roles)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", username=" + username + ", password=" 
                + password + ", enabled=" + enabled + ", firstName=" + firstName 
                + ", lastName=" + lastName + ", email=" + email + ", photoFilename=" 
                + photoFilename + ", roles=" + roles + '}';
    }
    
}
