package com.sg.blogcms.model;

import com.sg.blogcms.entity.Role;
import com.sg.blogcms.entity.User;
import com.sg.blogcms.model.RoleDaoDb.RoleMapper;
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
public class UserDaoDb implements UserDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    @Transactional
    public User createUser(User user) {
        //insert to user
        String insertUserQuery = "INSERT INTO user(username, password, isEnabled, firstName, lastName, email, photoFilename) "
                + "VALUES(?,?,?,?,?,?,?);";
        jdbc.update(insertUserQuery,
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhotoFilename());

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        user.setId(newId);

        //insert to bridge
        for (Role role : user.getRoles()) {
            String insertURQuery = "INSERT INTO userRole(userId, roleId) "
                    + "VALUES(?,?);";
            jdbc.update(insertURQuery, user.getId(), role.getId());
        }

        return user;
    }

    @Override
    public User readUserById(int id) {
        try {
            String readQuery = "SELECT * FROM user "
                    + "WHERE userId = ?;";
            User user = jdbc.queryForObject(readQuery, new UserMapper(), id);
            user.setRoles(readRolesForUser(user.getId()));

            return user;
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public User readUserByUsername(String username) {
        try {
            String readQuery = "SELECT * FROM user "
                    + "WHERE username = ?;";
            User user = jdbc.queryForObject(readQuery, new UserMapper(), username);
            user.setRoles(readRolesForUser(user.getId()));

            return user;
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public User readEnabledUserByUsername(String username) {
        try {
            String readQuery = "SELECT * FROM user "
                    + "WHERE username = ? "
                    + "AND isEnabled != 0;";
            User user = jdbc.queryForObject(readQuery, new UserMapper(), username);
            user.setRoles(readRolesForUser(user.getId()));

            return user;
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public List<User> readAllUsers() {
        String readAllQuery = "SELECT * FROM user;";
        List<User> users = jdbc.query(readAllQuery, new UserMapper());
        for (User user : users) {
            user.setRoles(readRolesForUser(user.getId()));
        }

        return users;
    }

    @Override
    public List<User> readAllEnabledUsers() {
        String readAllQuery = "SELECT * FROM user "
                + "WHERE isEnabled != 0;";
        List<User> activeUsers = jdbc.query(readAllQuery, new UserMapper());
        for (User user : activeUsers) {
            user.setRoles(readRolesForUser(user.getId()));
        }

        return activeUsers;
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        String updateQuery = "UPDATE user "
                + "SET "
                + "username = ?, "
                + "password = ?, "
                + "isEnabled = ?, "
                + "firstName = ?, "
                + "lastName = ?, "
                + "email = ?, "
                + "photoFilename = ? "
                + "WHERE userId = ?;";
        int updated = jdbc.update(updateQuery,
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhotoFilename(),
                user.getId());

        if (updated == 1) {
            //delete from bridge
            String delUR = "DELETE FROM userRole "
                    + "WHERE userId = ?;";
            jdbc.update(delUR, user.getId());

            //reinsert to bridge
            for (Role role : user.getRoles()) {
                String insertURQuery = "INSERT INTO userRole (userId, roleId) "
                        + "VALUES(?,?);";
                jdbc.update(insertURQuery, user.getId(), role.getId());
            }

            return user;
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public boolean deleteUserById(int id) {
        /*delete from bridge*/
        String delUR = "DELETE FROM userRole "
                + "WHERE userId = ?;";
        jdbc.update(delUR, id);

        /*delete post*/
        //bridge
        String delPC = "DELETE FROM postCategory "
                + "WHERE postId IN "
                + "(SELECT postId FROM post "
                + "WHERE userId = ?);";
        jdbc.update(delPC, id);

        //actual posts
        String delP = "DELETE FROM post "
                + "WHERE userId = ?;";
        jdbc.update(delP, id);

        /*delete user*/
        String deleteQuery = "DELETE FROM user "
                + "WHERE userId = ?;";
        return jdbc.update(deleteQuery, id) == 1;
    }

    /*helper*/
    /**
     * Retrieve Roles for a User
     *
     * @param id {int} an existing User obj's id
     * @return {Set} Role obj's for a user
     * @throws DataAccessException if cannot read roles
     */
    private Set<Role> readRolesForUser(int id) throws DataAccessException {
        String selectRolesQuery = "SELECT r.* FROM userRole ur "
                + "JOIN role r ON r.roleId = ur.roleId "
                + "WHERE ur.userId = ?;";
        return new HashSet<>(jdbc.query(selectRolesQuery, new RoleMapper(), id));
    }

    /**
     * RowMapper impl
     */
    public static final class UserMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int i) throws SQLException {
            User u = new User();
            u.setId(rs.getInt("userId"));
            u.setUsername(rs.getString("username"));
            u.setPassword(rs.getString("password"));
            u.setEnabled(rs.getBoolean("isEnabled"));
            u.setFirstName(rs.getString("firstName"));
            u.setLastName(rs.getString("lastName"));
            u.setEmail(rs.getString("email"));
            u.setPhotoFilename(rs.getString("photoFilename"));
            //roles will be associated in implementations

            return u;
        }
    }

}
