package com.sg.blogcms.model;

import com.sg.blogcms.entity.User;
import java.util.List;

public interface UserDao {

    /**
     * Create a new user account
     *
     * @param user {User} a well form obj
     * @return {User} a successfully added acc from db
     */
    User createUser(User user);

    /**
     * Retrieve a user from db by their id
     *
     * @param id {int} a valid id
     * @return {User} obj from db, null otherwise
     */
    User readUserById(int id);

    /**
     * Retrieve a user from db by their username
     *
     * @param username {String} a valid username
     * @return {User} obj from db, null otherwise
     */
    User readUserByUsername(String username);

    /**
     * Retrieve an active user from db by their username
     *
     * @param username {String} a valid username
     * @return {User} obj from db, null otherwise
     */
    User readEnabledUserByUsername(String username);

    /**
     * Retrieve all users from db, regardless of activation status
     *
     * @return {List} all user obj's from db
     */
    List<User> readAllUsers();

    /**
     * Retrieve all users with enabled accounts
     *
     * @return {List} all active users
     */
    List<User> readAllEnabledUsers();

    /**
     * Update a User account
     *
     * @param userEdit {User} a well formed obj with matching id for edit
     * @return {User} the successfully updated obj from db, null otherwise
     */
    User updateUser(User userEdit);

    /**
     * Delete a User account
     *
     * @param id {int} a valid id
     * @return {boolean} true if delete, false otherwise
     */
    boolean deleteUserById(int id);
}
