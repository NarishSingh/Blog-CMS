package com.sg.blogcms.model;

import com.sg.blogcms.entity.Role;
import java.util.List;

public interface RoleDao {

    /**
     * Create new role/account type
     *
     * @param role {Role} well form obj
     * @return {Role} the successfully create obj from db
     */
    Role createRole(Role role);

    /**
     * Retrieve a role by id
     *
     * @param id {int} for an existing Rol in db
     * @return {Role} the obj from db, null for invalid id
     */
    Role readRoleById(int id);

    /**
     * Retrieve a role by its role title
     *
     * @param roleString {String} for an existing Rol in db
     * @return {Role} the obj from db, null for invalid title
     */
    Role readRoleByRole(String roleString);

    /**
     * Retrieve all roles
     *
     * @return {List} all obj's from db
     */
    List<Role> readAllRoles();

    /**
     * Update a Role in db
     *
     * @param roleEdit {Role} a well formed obj with the corresponding id for
     *                 the edit
     * @return {Role} the successfully edited obj from db, null otherwise
     */
    Role updateRole(Role roleEdit);

    /**
     * Delete a Role from db
     *
     * @param id {int} a valid id
     * @return {boolean} true if delete succeeds, false otherwise
     */
    boolean deleteRoleById(int id);
}
