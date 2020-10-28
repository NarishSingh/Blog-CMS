package com.sg.blogcms.model;

import com.sg.blogcms.entity.Category;
import com.sg.blogcms.entity.Post;
import com.sg.blogcms.entity.Role;
import com.sg.blogcms.entity.User;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
public class UserDaoTest {
    
    @Autowired
    RoleDao rDao;
    @Autowired
    UserDao uDao;
    @Autowired
    CategoryDao cDao;
    @Autowired
    PostDao pDao;
    
    static Role role1;
    static Role role2;
    static Role role3;
    
    static User u1;
    static User u2disabled;
    static User cc;
    static User adm;
    
    
    public UserDaoTest() {
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
        Role r1 = new Role();
        r1.setRole("User");
        role1 = rDao.createRole(r1);

        Role r2 = new Role();
        r2.setRole("Content Creator");
        role2 = rDao.createRole(r2);

        Role r3 = new Role();
        r3.setRole("Admin");
        role3 = rDao.createRole(r3);
        
        /*Create users*/
        //just a regular user
        u1 = new User();
        u1.setUsername("test1");
        u1.setPassword("password001");
        u1.setEnabled(true);
        u1.setFirstName("First");
        u1.setLastName("Tester");
        u1.setEmail("test1@mail.com");
        u1.setPhotoFilename(null);
        Set<Role> u1r = new HashSet<>();
        u1r.add(role1);
        u1.setRoles(u1r);
        
        //disabled account, some nulls
        u2disabled = new User();
        u2disabled.setUsername("test2disabled");
        u2disabled.setPassword("password002");
        u2disabled.setEnabled(false);
        u2disabled.setFirstName("Second");
        u2disabled.setLastName(null);
        u2disabled.setEmail(null);
        u2disabled.setPhotoFilename(null);
        Set<Role> u2disabledr = new HashSet<>();
        u2disabledr.add(role1);
        u2disabled.setRoles(u2disabledr);
        
        //content creator
        cc = new User();
        cc.setUsername("creator");
        cc.setPassword("password003");
        cc.setEnabled(true);
        cc.setFirstName("First");
        cc.setLastName("Creator");
        cc.setEmail("test3@mail.com");
        cc.setPhotoFilename(null);
        Set<Role> ccr = new HashSet<>();
        ccr.add(role1);
        ccr.add(role2);
        cc.setRoles(ccr);
        
        //admin
        adm = new User();
        adm.setUsername("admin");
        adm.setPassword("password003");
        adm.setEnabled(true);
        adm.setFirstName("The");
        adm.setLastName("Admin");
        adm.setEmail("admin@mail.com");
        adm.setPhotoFilename(null);
        Set<Role> ar = new HashSet<>();
        ar.add(role1);
        ar.add(role2);
        ar.add(role3);
        adm.setRoles(ar);
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of createUser and readUserById method, of class UserDao.
     */
    @Test
    public void testCreateReadUserByIdUser() {
        User user1 = uDao.createUser(u1);
        
        User fromDao = uDao.readUserById(user1.getId());
        
        assertNotNull(user1);
        assertNotNull(fromDao);
        assertEquals(user1, fromDao);
    }

    /**
     * Test of readUserByUsername method, of class UserDao.
     */
    @Test
    public void testReadUserByUsername() {
        User user1 = uDao.createUser(u1);
        
        User fromDao = uDao.readUserByUsername(user1.getUsername());
        
        assertNotNull(user1);
        assertNotNull(fromDao);
        assertEquals(user1, fromDao);
    }

    /**
     * Test of readAllUsers method, of class UserDao.
     */
    @Test
    public void testReadAllUsers() {
        User user1 = uDao.createUser(u1);
        User user2disabled = uDao.createUser(u2disabled);
        User creator = uDao.createUser(cc);
        User admin = uDao.createUser(adm);
        
        List<User> users = uDao.readAllUsers();
        
        assertNotNull(user1);
        assertNotNull(user2disabled);
        assertNotNull(creator);
        assertNotNull(admin);
        assertNotNull(users);
        assertEquals(4, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2disabled));
        assertTrue(users.contains(creator));
        assertTrue(users.contains(admin));
    }

    /**
     * Test of readAllEnabledUsers method, of class UserDao.
     */
    @Test
    public void testReadAllEnabledUsers() {
        User user1 = uDao.createUser(u1);
        User user2disabled = uDao.createUser(u2disabled);
        User creator = uDao.createUser(cc);
        User admin = uDao.createUser(adm);
        
        List<User> enabledUsers = uDao.readAllEnabledUsers();
        
        assertNotNull(enabledUsers);
        assertEquals(3, enabledUsers.size());
        assertTrue(enabledUsers.contains(user1));
        assertFalse(enabledUsers.contains(user2disabled));
        assertTrue(enabledUsers.contains(creator));
        assertTrue(enabledUsers.contains(admin));
    }

    /**
     * Test of updateUser method, of class UserDao.
     */
    @Test
    public void testUpdateUser() {
        User user1 = uDao.createUser(u1);
        User original = uDao.readUserById(user1.getId());
        
        user1.setPassword("newPassword");
        user1.setFirstName("editName");
        
        User user1edit = uDao.updateUser(user1);
        User edit = uDao.readUserById(user1.getId());
        
        assertNotNull(original);
        assertNotNull(user1edit);
        assertNotNull(edit);
        assertEquals(user1edit, edit);
        assertNotEquals(original, user1edit);
        assertNotEquals(original, edit);
    }

    /**
     * Test of deleteUserById method, of class UserDao.
     */
    @Test
    public void testDeleteUserById() {
        User user1 = uDao.createUser(u1);
        User user2disabled = uDao.createUser(u2disabled);
        User creator = uDao.createUser(cc);
        User admin = uDao.createUser(adm);
        
        List<User> original = uDao.readAllUsers();
        
        boolean deleted = uDao.deleteUserById(creator.getId());
        List<User> afterDel = uDao.readAllUsers();
        
        assertEquals(4, original.size());
        assertTrue(original.contains(user1));
        assertTrue(original.contains(user2disabled));
        assertTrue(original.contains(creator));
        assertTrue(original.contains(admin));
        assertTrue(deleted);
        assertEquals(3, afterDel.size());
        assertTrue(afterDel.contains(user1));
        assertTrue(afterDel.contains(user2disabled));
        assertFalse(afterDel.contains(creator));
        assertTrue(afterDel.contains(admin));
    }

}
