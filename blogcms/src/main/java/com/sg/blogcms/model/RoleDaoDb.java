package com.sg.blogcms.model;

import com.sg.blogcms.entity.Role;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class RoleDaoDb implements RoleDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    @Transactional
    public Role createRole(Role role) {
        String insertQuery = "INSERT INTO role(role) "
                + "VALUES(?);";
        jdbc.update(insertQuery, role.getRole());

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID();", Integer.class);
        role.setId(newId);

        return role;
    }

    @Override
    public Role readRoleById(int id) {
        try {
            String selectQuery = "SELECT * FROM role "
                    + "WHERE roleId = ?;";
            return jdbc.queryForObject(selectQuery, new RoleMapper(), id);
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public Role readRoleByRole(String roleString) {
        try {
            String selectQuery = "SELECT * FROM role "
                    + "WHERE role = ?;";
            return jdbc.queryForObject(selectQuery, new RoleMapper(), roleString);
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Role> readAllRoles() {
        String selectAllQuery = "SELECT * FROM role;";
        return jdbc.query(selectAllQuery, new RoleMapper());
    }

    @Override
    public Role updateRole(Role roleEdit) {
        String updateQuery = "UPDATE role "
                + "SET role = ? "
                + "WHERE roleId = ?;";

        if (jdbc.update(updateQuery, roleEdit.getRole(), roleEdit.getId()) == 1) {
            return roleEdit;
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public boolean deleteRoleById(int id) {
        //delete from user role
        String delUR = "DELETE FROM userRole "
                + "WHERE roleId = ?;";
        jdbc.update(delUR, id);

        //delete role
        String delRQuery = "DELETE FROM role "
                + "WHERE roleId = ?;";
        return jdbc.update(delRQuery, id) == 1;
    }

    /**
     * RowMapper impl
     */
    public static final class RoleMapper implements RowMapper<Role> {

        @Override
        public Role mapRow(ResultSet rs, int i) throws SQLException {
            Role r = new Role();
            r.setId(rs.getInt("roleId"));
            r.setRole(rs.getString("role"));

            return r;
        }
    }

}
