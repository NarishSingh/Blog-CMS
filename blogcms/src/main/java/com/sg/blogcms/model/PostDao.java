package com.sg.blogcms.model;

import com.sg.blogcms.entity.Post;
import java.util.List;

public interface PostDao {

    /**
     * Create a new Post
     *
     * @param post {Post} well formed obj
     * @return {Post} successfully create obj from db
     */
    Post createPost(Post post);

    /**
     * Retrieve a Post from db
     *
     * @param id {int} a valid id
     * @return {Post} obj from db
     */
    Post readPostById(int id);

    /**
     * Retrieve all Posts from db
     *
     * @return {List} all posts, regardless of active date range or editorial
     *         status
     */
    List<Post> readAllPosts();

    /**
     * Retrieve all Posts with the current date time being within range of the
     * post's postOn and expireOn datetime, regardless of approval status
     *
     * @return {List} all posts within active date range
     */
    List<Post> readPostsByDate();

    /**
     * Retrieve all Posts that have admin approval, regardless of active date
     * range
     *
     * @return {List} all approved posts
     */
    List<Post> readPostsByApproval();

    /**
     * Retrieve all Posts that can be posted on today - meets approval status
     * and active date range requirements
     *
     * @return {List} all posts that can be published/rendered on load
     */
    List<Post> readAllForPublication();

    /**
     * Retrieve all static posts
     *
     * @return {List} only static posts
     */
    List<Post> readAllStatic();

    /**
     * Retrieve posts by Category
     *
     * @param categoryId {int} valid category id
     * @return {List} all posts with tagged with that category
     */
    List<Post> readPostsByCategory(int categoryId);

    /**
     * Retrieve publishable posts by Category
     *
     * @param categoryId {int} valid category id
     * @return {List} all publishable posts with tagged with that category
     */
    List<Post> readPublishedPostsByCategory(int categoryId);

    /**
     * Edit a post (admin only feature)
     *
     * @param post {Post} a well formed obj with matching id for edit
     * @return {Post} the edited obj from db
     */
    Post updatePost(Post post);

    /**
     * Delete a post from db
     *
     * @param id {int} a valid id
     * @return {boolean} true if deleted, false otherwise
     */
    boolean deletePostById(int id);
}
