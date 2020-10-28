package com.sg.blogcms.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Post {

    private int id;

    @NotBlank(message = "Post must have a title")
    @Size(max = 100, message = "Post title must 100 characters or less")
    private String title;

    @Size(max = 65535, message = "Post body cannot exceed 65,535")
    @NotBlank(message = "Please enter your blog post")
    private String body;

    @NotNull(message = "Please indicate approval status")
    private boolean approved;

    @NotNull(message = "Please indicate if post is a static page or not")
    private boolean staticPage;

    @NotNull(message = "Please indicate time of post creation")
    private LocalDateTime createdOn;

    @FutureOrPresent(message = "Cannot post in the past")
    @NotNull(message = "Please indicate date and time to post")
    private LocalDateTime postOn;

    @FutureOrPresent(message = "Cannot post when its already expired")
    @NotNull(message = "Please indicate time of post expiration")
    private LocalDateTime expireOn;

    @NotNull(message = "Please indicate the user who authored post")
    private User user;

    private String photoFilename;
    
    @NotNull(message = "Please indicate post category, or create a new one at link below category select")
    private List<Category> categories;

    /*gs*/
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean isStaticPage() {
        return staticPage;
    }

    public void setStaticPage(boolean staticPage) {
        this.staticPage = staticPage;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getPostOn() {
        return postOn;
    }

    public void setPostOn(LocalDateTime postOn) {
        this.postOn = postOn;
    }

    public LocalDateTime getExpireOn() {
        return expireOn;
    }

    public void setExpireOn(LocalDateTime expireOn) {
        this.expireOn = expireOn;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPhotoFilename() {
        return photoFilename;
    }

    public void setPhotoFilename(String photoFilename) {
        this.photoFilename = photoFilename;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    /*testing*/
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + this.id;
        hash = 37 * hash + Objects.hashCode(this.title);
        hash = 37 * hash + Objects.hashCode(this.body);
        hash = 37 * hash + (this.approved ? 1 : 0);
        hash = 37 * hash + (this.staticPage ? 1 : 0);
        hash = 37 * hash + Objects.hashCode(this.createdOn);
        hash = 37 * hash + Objects.hashCode(this.postOn);
        hash = 37 * hash + Objects.hashCode(this.expireOn);
        hash = 37 * hash + Objects.hashCode(this.user);
        hash = 37 * hash + Objects.hashCode(this.photoFilename);
        hash = 37 * hash + Objects.hashCode(this.categories);
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
        final Post other = (Post) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.approved != other.approved) {
            return false;
        }
        if (this.staticPage != other.staticPage) {
            return false;
        }
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.body, other.body)) {
            return false;
        }
        if (!Objects.equals(this.photoFilename, other.photoFilename)) {
            return false;
        }
        if (!Objects.equals(this.createdOn, other.createdOn)) {
            return false;
        }
        if (!Objects.equals(this.postOn, other.postOn)) {
            return false;
        }
        if (!Objects.equals(this.expireOn, other.expireOn)) {
            return false;
        }
        if (!Objects.equals(this.user, other.user)) {
            return false;
        }
        if (!Objects.equals(this.categories, other.categories)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Post{" + "id=" + id + ", title=" + title + ", body=" + body 
                + ", approved=" + approved + ", staticPage=" + staticPage 
                + ", createdOn=" + createdOn + ", postOn=" + postOn + ", expireOn=" 
                + expireOn + ", user=" + user + ", photoFilename=" + photoFilename 
                + ", categories=" + categories + '}';
    }
    
}
