package com.sg.blogcms.model;

import com.sg.blogcms.entity.Category;
import com.sg.blogcms.entity.Post;
import java.util.List;

public interface CategoryDao {

    /**
     * Create a new post Category
     *
     * @param category {Category} well formed obj
     * @return {Category} the successfully created obj from db
     */
    Category createCategory(Category category);

    /**
     * Retrieve a Category from db by its id
     *
     * @param id {int} a valid id
     * @return {Category} the obj from db, null otherwise
     */
    Category readCategoryById(int id);

    /**
     * Retrieve a Category from db by its name
     *
     * @param name {String} the hashtag or name
     * @return {Category} the obj from db, null otherwise
     */
    Category readCategoryByName(String name);

    /**
     * Retrieve all Categories from db
     *
     * @return {List} all categories from db
     */
    List<Category> readAllCategories();

    /**
     * Edit a Category in db
     *
     * @param category {Category} well formed obj with the matching id for edit
     * @return {Category} the successfully edited obj, null if update fails
     */
    Category updateCategory(Category category);

    /**
     * Delete a Category from db
     *
     * @param id {int} a valid id
     * @return {boolean} true if deleted, false otherwise
     */
    boolean deleteCategoryById(int id);
}
