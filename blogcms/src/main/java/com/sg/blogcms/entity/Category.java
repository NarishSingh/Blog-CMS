package com.sg.blogcms.entity;

import java.util.Objects;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class Category {

    private int id;

    @NotBlank(message = "Category/hashtag cannot be blank")
    @Size(max = 50, message = "Max of 50 characters for category/hashtag")
    private String category;

    /*gs*/
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    /*testing*/
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + this.id;
        hash = 59 * hash + Objects.hashCode(this.category);
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
        final Category other = (Category) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.category, other.category)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Category{" + "id=" + id + ", category=" + category + '}';
    }

}
