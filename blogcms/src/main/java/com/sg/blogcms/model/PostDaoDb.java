package com.sg.blogcms.model;

import com.sg.blogcms.entity.Category;
import com.sg.blogcms.entity.Post;
import com.sg.blogcms.entity.Role;
import com.sg.blogcms.entity.User;
import com.sg.blogcms.model.CategoryDaoDb.CategoryMapper;
import com.sg.blogcms.model.RoleDaoDb.RoleMapper;
import com.sg.blogcms.model.UserDaoDb.UserMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class PostDaoDb implements PostDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    @Transactional
    public Post createPost(Post post) {
        //insert
        String insertQuery = "INSERT INTO post(title, body, isApproved, isStaticPage, createdOn, postOn, expireOn, photoFilename, userId) "
                + "VALUES(?,?,?,?,?,?,?,?,?);";
        jdbc.update(insertQuery,
                post.getTitle(),
                post.getBody(),
                post.isApproved(),
                post.isStaticPage(),
                post.getCreatedOn(),
                post.getPostOn(),
                post.getExpireOn(),
                post.getPhotoFilename(),
                post.getUser().getId());

        //set id from db
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID();", Integer.class);
        post.setId(newId);

        //insert to bridge
        insertPostCategory(post);

        return post;
    }

    @Override
    public Post readPostById(int id) {
        try {
            String readQuery = "SELECT * FROM post "
                    + "WHERE postId = ?;";
            Post post = jdbc.queryForObject(readQuery, new PostMapper(), id);
            post.setUser(readUserForPost(id));
            post.setCategories(readCategoriesForPost(id));

            return post;
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Post> readAllPosts() {
        String readAllQuery = "SELECT * FROM post;";
        List<Post> posts = jdbc.query(readAllQuery, new PostMapper());

        for (Post p : posts) {
            p.setUser(readUserForPost(p.getId()));
            p.setCategories(readCategoriesForPost(p.getId()));
        }

        return posts;
    }

    @Override
    public List<Post> readPostsByDate() {
        String readActivePosts = "SELECT * FROM post "
                + "WHERE postOn <= NOW() "
                + "AND expireOn >= NOW();";
        List<Post> activePosts = jdbc.query(readActivePosts, new PostMapper());

        for (Post p : activePosts) {
            p.setUser(readUserForPost(p.getId()));
            p.setCategories(readCategoriesForPost(p.getId()));
        }

        return activePosts;
    }

    @Override
    public List<Post> readPostsByApproval() {
        String readApprovedPosts = "SELECT * FROM post "
                + "WHERE isApproved != 0;";
        List<Post> approvedPosts = jdbc.query(readApprovedPosts, new PostMapper());

        for (Post p : approvedPosts) {
            p.setUser(readUserForPost(p.getId()));
            p.setCategories(readCategoriesForPost(p.getId()));
        }

        return approvedPosts;
    }

    @Override
    public List<Post> readAllForPublication() {
        String readPublish = "SELECT * FROM post "
                + "WHERE postOn <= NOW() AND expireOn >= NOW() "
                + "AND isApproved != 0 "
                + "AND isStaticPage = 0;";
        List<Post> publishPosts = jdbc.query(readPublish, new PostMapper());

        for (Post p : publishPosts) {
            p.setUser(readUserForPost(p.getId()));
            p.setCategories(readCategoriesForPost(p.getId()));
        }

        return publishPosts;
    }

    @Override
    public List<Post> readAllStatic() {
        String readStatic = "SELECT * FROM post "
                + "WHERE isStaticPage != 0;";
        List<Post> staticPosts = jdbc.query(readStatic, new PostMapper());

        for (Post sp : staticPosts) {
            sp.setUser(readUserForPost(sp.getId()));
            sp.setCategories(readCategoriesForPost(sp.getId()));
        }

        return staticPosts;
    }

    @Override
    public List<Post> readPostsByCategory(int categoryId) {
        String readPCQuery = "SELECT p.* FROM post p "
                + "JOIN postCategory pc ON pc.postId = p.postId "
                + "WHERE pc.categoryId = ?;";
        List<Post> posts = jdbc.query(readPCQuery, new PostMapper(), categoryId);

        //associate
        for (Post p : posts) {
            p.setUser(readUserForPost(p.getId()));
            p.setCategories(readCategoriesForPost(p.getId()));
        }

        return posts;
    }

    @Override
    public List<Post> readPublishedPostsByCategory(int categoryId) {
        String readPCQuery = "SELECT p.* FROM post p "
                + "JOIN postCategory pc ON pc.postId = p.postId "
                + "WHERE pc.categoryId = ? "
                + "AND p.postOn <= NOW() "
                + "AND p.expireOn >= NOW() "
                + "AND p.isApproved != 0;";
        List<Post> posts = jdbc.query(readPCQuery, new PostMapper(), categoryId);

        //associate
        for (Post p : posts) {
            p.setUser(readUserForPost(p.getId()));
            p.setCategories(readCategoriesForPost(p.getId()));
        }

        return posts;
    }

    @Override
    @Transactional
    public Post updatePost(Post post) {
        String updateQuery = "UPDATE post "
                + "SET "
                + "title = ?, "
                + "body = ?, "
                + "isApproved = ?, "
                + "isStaticPage = ?, "
                + "createdOn = ?, "
                + "postOn = ?, "
                + "expireOn = ?, "
                + "photoFilename = ?, "
                + "userId = ? "
                + "WHERE postId = ?;";
        int updated = jdbc.update(updateQuery,
                post.getTitle(),
                post.getBody(),
                post.isApproved(),
                post.isStaticPage(),
                post.getCreatedOn(),
                post.getPostOn(),
                post.getExpireOn(),
                post.getPhotoFilename(),
                post.getUser().getId(),
                post.getId());

        if (updated == 1) {
            //delete and reinsert to bridge
            String delBridgeQuery = "DELETE FROM postCategory "
                    + "WHERE postId = ?;";
            jdbc.update(delBridgeQuery, post.getId());
            insertPostCategory(post);

            return post;
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public boolean deletePostById(int id) {
        //delete from bridge
        String delPC = "DELETE FROM postCategory "
                + "WHERE postId = ?;";
        jdbc.update(delPC, id);

        //delete post
        String deletePostQuery = "DELETE FROM post "
                + "WHERE postId = ?;";
        return jdbc.update(deletePostQuery, id) == 1;
    }

    /*helpers*/
    /**
     * Retrieve the User for a Post and associate its roles from db
     *
     * @param id {int} a valid id
     * @return {User} obj for a Post from db
     */
    private User readUserForPost(int id) throws DataAccessException {
        //read user
        String readQuery = "SELECT u.* FROM user u "
                + "JOIN post p ON p.userId = u.userId "
                + "WHERE p.postId = ?;";
        User u = jdbc.queryForObject(readQuery, new UserMapper(), id);

        //read and associate roles
        String selectRolesQuery = "SELECT r.* FROM userRole ur "
                + "JOIN role r ON r.roleId = ur.roleId "
                + "WHERE ur.userId = ?;";
        Set<Role> userRoles = new HashSet<>(jdbc.query(selectRolesQuery, new RoleMapper(), u.getId()));
        u.setRoles(userRoles);

        return u;
    }

    /**
     * Retrieve Categories from db for a Post
     *
     * @param id {int} id for an existing Post
     * @return {List} all categories
     */
    private List<Category> readCategoriesForPost(int id) {
        String readCQuery = "SELECT c.* FROM category c "
                + "JOIN postCategory pc ON pc.categoryId = c.categoryId "
                + "JOIN post p ON p.postId = pc.postId "
                + "WHERE p.postId = ?;";
        return jdbc.query(readCQuery, new CategoryMapper(), id);
    }

    /**
     * Update the postCategory bridge table
     *
     * @param post {Post} a well formed obj
     */
    private void insertPostCategory(Post post) {
        String insertBridgeQuery = "INSERT INTO postCategory(postId, categoryId) "
                + "VALUES(?,?);";

        for (Category c : post.getCategories()) {
            jdbc.update(insertBridgeQuery, post.getId(), c.getId());
        }
    }

    /**
     * RowMapper impl
     */
    public static final class PostMapper implements RowMapper<Post> {

        @Override
        public Post mapRow(ResultSet rs, int i) throws SQLException {
            Post p = new Post();
            p.setId(rs.getInt("postId"));
            p.setTitle(rs.getString("title"));
            p.setBody(rs.getString("body"));
            p.setApproved(rs.getBoolean("isApproved"));
            p.setStaticPage(rs.getBoolean("isStaticPage"));
            p.setCreatedOn(rs.getTimestamp("createdOn").toLocalDateTime());
            p.setPostOn(rs.getTimestamp("postOn").toLocalDateTime());
            p.setExpireOn(rs.getTimestamp("expireOn").toLocalDateTime());
            p.setPhotoFilename(rs.getString("photoFilename"));
            //user and category will be associated in methods

            return p;
        }
    }

}
