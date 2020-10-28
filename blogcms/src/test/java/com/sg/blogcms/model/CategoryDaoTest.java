package com.sg.blogcms.model;

import com.sg.blogcms.entity.Category;
import com.sg.blogcms.entity.Post;
import com.sg.blogcms.entity.Role;
import com.sg.blogcms.entity.User;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryDaoTest {

    @Autowired
    RoleDao rDao;
    @Autowired
    UserDao uDao;
    @Autowired
    CategoryDao cDao;
    @Autowired
    PostDao pDao;

    static Category c1;
    static Category c2;
    static Category c3;

    public CategoryDaoTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        /*clean db*/
        for (Role r : rDao.readAllRoles()) {
            rDao.deleteRoleById(r.getId());
        }

        for (User u : uDao.readAllUsers()) {
            uDao.deleteUserById(u.getId());
        }

        for (Category c : cDao.readAllCategories()) {
            cDao.deleteCategoryById(c.getId());
        }

        for (Post p : pDao.readAllPosts()) {
            pDao.deletePostById(p.getId());
        }

        /*Create categories*/
        c1 = new Category();
        c1.setCategory("Upcoming");

        c2 = new Category();
        c2.setCategory("Release");

        c3 = new Category();
        c3.setCategory("Update");
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of createCategory and readCategoryById method, of class CategoryDao.
     */
    @Test
    public void testCreateReadCategoryByIdCategory() {
        Category cat1 = cDao.createCategory(c1);

        Category fromDao = cDao.readCategoryById(cat1.getId());

        assertNotNull(cat1);
        assertNotNull(fromDao);
        assertEquals(cat1, fromDao);
    }

    /**
     * Test of readCategoryByName method, of class CategoryDao.
     */
    @Test
    public void testReadCategoryByName() {
        Category cat1 = cDao.createCategory(c1);

        Category fromDao = cDao.readCategoryByName(cat1.getCategory());

        assertNotNull(cat1);
        assertNotNull(fromDao);
        assertEquals(cat1, fromDao);
    }

    /**
     * Test of readAllCategories method, of class CategoryDao.
     */
    @Test
    public void testReadAllCategories() {
        Category cat1 = cDao.createCategory(c1);
        Category cat2 = cDao.createCategory(c2);
        Category cat3 = cDao.createCategory(c3);

        List<Category> categories = cDao.readAllCategories();

        assertNotNull(categories);
        assertEquals(3, categories.size());
        assertTrue(categories.contains(cat1));
        assertTrue(categories.contains(cat2));
        assertTrue(categories.contains(cat3));
    }

    /**
     * Test of updateCategory method, of class CategoryDao.
     */
    @Test
    public void testUpdateCategory() {
        Category cat1 = cDao.createCategory(c1);
        Category original = cDao.readCategoryById(cat1.getId());

        cat1.setCategory("testEdit");
        Category cat1update = cDao.updateCategory(cat1);
        Category edit = cDao.readCategoryById(cat1.getId());

        assertNotNull(edit);
        assertEquals(cat1update, edit);
        assertNotEquals(original, edit);
    }

    /**
     * Test of deleteCategoryById method, of class CategoryDao.
     */
    @Test
    public void testDeleteCategoryById() {
        Category cat1 = cDao.createCategory(c1);
        Category cat2 = cDao.createCategory(c2);
        Category cat3 = cDao.createCategory(c3);
        List<Category> original = cDao.readAllCategories();

        boolean deleted = cDao.deleteCategoryById(cat2.getId());

        List<Category> afterDel = cDao.readAllCategories();

        assertNotNull(original);
        assertEquals(3, original.size());
        assertNotNull(afterDel);
        assertEquals(2, afterDel.size());
        assertNotEquals(original, afterDel);
        assertTrue(deleted);
        assertTrue(afterDel.contains(cat1));
        assertFalse(afterDel.contains(cat2));
        assertTrue(afterDel.contains(cat3));
    }

}
