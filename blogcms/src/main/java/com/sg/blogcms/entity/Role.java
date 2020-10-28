package com.sg.blogcms.entity;

import java.util.Objects;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class Role {

    private int id;

    @NotBlank(message = "Role title cannot be blank")
    @Size(max = 30, message = "Max of 30 characters for role title")
    private String role;

    /*gs*/
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    /*testing*/
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this.id;
        hash = 79 * hash + Objects.hashCode(this.role);
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
        final Role other = (Role) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.role, other.role)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Role{" + "id=" + id + ", role=" + role + '}';
    }

}
