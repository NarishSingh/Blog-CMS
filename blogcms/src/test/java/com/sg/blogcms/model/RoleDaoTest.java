package com.sg.blogcms.model;

import com.sg.blogcms.entity.Category;
import com.sg.blogcms.entity.Post;
import com.sg.blogcms.entity.Role;
import com.sg.blogcms.entity.User;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleDaoTest {

    @Autowired
    RoleDao rDao;
    @Autowired
    UserDao uDao;
    @Autowired
    CategoryDao cDao;
    @Autowired
    PostDao pDao;

    static Role r1;
    static Role r2;
    static Role r3;

    public RoleDaoTest() {
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

        /*Create roles*/
        r1 = new Role();
        r1.setRole("User");

        r2 = new Role();
        r2.setRole("Content Creator");

        r3 = new Role();
        r3.setRole("Admin");

    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of createRole and readRoleById method, of class RoleDao.
     */
    @Test
    public void testCreateReadRoleByIdRole() {
        Role role1 = rDao.createRole(r1);

        Role fromDao = rDao.readRoleById(role1.getId());

        assertNotNull(role1);
        assertNotNull(fromDao);
        assertEquals(role1, fromDao);
    }

    /**
     * Test of readRoleByRole method, of class RoleDao.
     */
    @Test
    public void testReadRoleByRole() {
        Role role1 = rDao.createRole(r1);

        Role fromDao = rDao.readRoleByRole(role1.getRole());

        assertNotNull(role1);
        assertNotNull(fromDao);
        assertEquals(role1, fromDao);
    }

    /**
     * Test of readAllRoles method, of class RoleDao.
     */
    @Test
    public void testReadAllRoles() {
        Role role1 = rDao.createRole(r1);
        Role role2 = rDao.createRole(r2);
        Role role3 = rDao.createRole(r3);

        List<Role> roles = rDao.readAllRoles();

        assertNotNull(roles);
        assertEquals(3, roles.size());
        assertTrue(roles.contains(role1));
        assertTrue(roles.contains(role2));
        assertTrue(roles.contains(role3));
    }

    /**
     * Test of updateRole method, of class RoleDao.
     */
    @Test
    public void testUpdateRole() {
        Role role1 = rDao.createRole(r1);
        Role original = rDao.readRoleById(role1.getId());

        role1.setRole("Paid User");
        Role role1update = rDao.updateRole(role1);
        Role edit = rDao.readRoleById(role1.getId());

        assertNotNull(original);
        assertNotNull(edit);
        assertEquals(role1update, edit);
        assertNotEquals(original, edit);
    }

    /**
     * Test of deleteRoleById method, of class RoleDao.
     */
    @Test
    public void testDeleteRoleById() {
        Role role1 = rDao.createRole(r1);
        Role role2 = rDao.createRole(r2);
        Role role3 = rDao.createRole(r3);
        List<Role> original = rDao.readAllRoles();

        boolean deleted = rDao.deleteRoleById(role2.getId());
        List<Role> afterDel = rDao.readAllRoles();

        assertNotNull(original);
        assertEquals(3, original.size());
        assertNotNull(afterDel);
        assertEquals(2, afterDel.size());
        assertNotEquals(original, afterDel);
        assertTrue(deleted);
        assertTrue(afterDel.contains(role1));
        assertFalse(afterDel.contains(role2));
        assertTrue(afterDel.contains(role3));
    }

}
