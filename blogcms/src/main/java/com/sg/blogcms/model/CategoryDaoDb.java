package com.sg.blogcms.model;

import com.sg.blogcms.entity.Category;
import com.sg.blogcms.entity.Post;
import com.sg.blogcms.model.PostDaoDb.PostMapper;
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
public class CategoryDaoDb implements CategoryDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    @Transactional
    public Category createCategory(Category category) {
        String insertQuery = "INSERT INTO category(category) "
                + "VALUES(?)";
        jdbc.update(insertQuery, category.getCategory());

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        category.setId(newId);

        return category;
    }

    @Override
    public Category readCategoryById(int id) {
        try {
            String readQuery = "SELECT * FROM category "
                    + "WHERE categoryId = ?;";
            return jdbc.queryForObject(readQuery, new CategoryMapper(), id);
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public Category readCategoryByName(String name) {
        try {
            String readQuery = "SELECT * FROM category "
                    + "WHERE category = ?;";
            return jdbc.queryForObject(readQuery, new CategoryMapper(), name);
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Category> readAllCategories() {
        String readAllQuery = "SELECT * from category;";
        return jdbc.query(readAllQuery, new CategoryMapper());
    }

    @Override
    public Category updateCategory(Category category) {
        String updateQuery = "UPDATE category "
                + "SET "
                + "category = ? "
                + "WHERE categoryId = ?;";

        if (jdbc.update(updateQuery, category.getCategory(), category.getId()) == 1) {
            return category;
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public boolean deleteCategoryById(int id) {
        //delete from bridge
        String delPC = "DELETE FROM postCategory "
                + "WHERE categoryId = ?;";
        jdbc.update(delPC, id);

        //delete category
        String delCategoryQuery = "DELETE FROM category "
                + "WHERE categoryId = ?;";
        return jdbc.update(delCategoryQuery, id) == 1;
    }

    /**
     * RowMapper impl
     */
    public static final class CategoryMapper implements RowMapper<Category> {

        @Override
        public Category mapRow(ResultSet rs, int i) throws SQLException {
            Category c = new Category();
            c.setId(rs.getInt("categoryId"));
            c.setCategory(rs.getString("category"));

            return c;
        }
    }

}
