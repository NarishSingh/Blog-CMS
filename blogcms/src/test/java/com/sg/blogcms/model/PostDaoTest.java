package com.sg.blogcms.model;

import com.sg.blogcms.entity.Category;
import com.sg.blogcms.entity.Post;
import com.sg.blogcms.entity.Role;
import com.sg.blogcms.entity.User;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
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
public class PostDaoTest {

    @Autowired
    RoleDao rDao;
    @Autowired
    UserDao uDao;
    @Autowired
    CategoryDao cDao;
    @Autowired
    PostDao pDao;

    Role role1;
    Role role2;
    Role role3;

    User creator;
    User admin;

    Category category1;
    Category category2;
    Category category3;

    Post p1;
    Post p2;
    Post p3;
    Post p4;

    public PostDaoTest() {
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
        //content creator
        User cc = new User();
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
        creator = uDao.createUser(cc);

        //admin
        User adm = new User();
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
        admin = uDao.createUser(adm);

        /*Create categories*/
        Category c1 = new Category();
        c1.setCategory("Upcoming");
        category1 = cDao.createCategory(c1);

        Category c2 = new Category();
        c2.setCategory("Release");
        category2 = cDao.createCategory(c2);

        Category c3 = new Category();
        c3.setCategory("Update");
        category3 = cDao.createCategory(c3);

        /*Create posts*/
        //FOR NO EXPIRE -> set to 12-31-9999, MySQL cannot take java's .MAX
        //by admin, approved, static, post now, never expires -> no expiration
        p1 = new Post();
        p1.setTitle("title1");
        p1.setBody("body1");
        p1.setApproved(true);
        p1.setStaticPage(true);
        p1.setCreatedOn(LocalDateTime.now().withNano(0));
        p1.setPostOn(LocalDateTime.now().withNano(0));
        p1.setExpireOn(LocalDateTime.of(9999, Month.DECEMBER, 31, 0, 0));
        p1.setPhotoFilename(null);
        p1.setUser(admin);
        List<Category> post1cat = new ArrayList<>();
        post1cat.add(category1);
        post1cat.add(category2);
        post1cat.add(category3);
        p1.setCategories(post1cat); //3 #'s

        //by creator, not approved, not static, post now -> expires in one month
        p2 = new Post();
        p2.setTitle("title2");
        p2.setBody("body2");
        p2.setApproved(false);
        p2.setStaticPage(false);
        p2.setCreatedOn(LocalDateTime.now().withNano(0));
        p2.setPostOn(LocalDateTime.now().withNano(0));
        p2.setExpireOn(LocalDateTime.now().plusMonths(1).withNano(0));
        p2.setPhotoFilename(null);
        p2.setUser(creator);
        List<Category> post2cat = new ArrayList<>();
        post2cat.add(category1);
        p2.setCategories(post2cat); //1 #

        //by admin, not approved, not static, post in one month -> no expire
        p3 = new Post();
        p3.setTitle("title3");
        p3.setBody("body3");
        p3.setApproved(false);
        p3.setStaticPage(false);
        p3.setCreatedOn(LocalDateTime.now().withNano(0));
        p3.setPostOn(LocalDateTime.now().plusMonths(1).withNano(0));
        p3.setExpireOn(LocalDateTime.of(9999, Month.DECEMBER, 31, 0, 0));
        p3.setPhotoFilename(null);
        p3.setUser(admin);
        List<Category> post3cat = new ArrayList<>();
        post3cat.add(category1);
        post3cat.add(category2);
        p3.setCategories(post3cat); //2#'s

        //by creator, approved, static, post in one month -> expires month after
        p4 = new Post();
        p4.setTitle("title4");
        p4.setBody("body4");
        p4.setApproved(true);
        p4.setStaticPage(true);
        p4.setCreatedOn(LocalDateTime.now().withNano(0));
        p4.setPostOn(LocalDateTime.now().plusMonths(1).withNano(0));
        p4.setExpireOn(LocalDateTime.now().plusMonths(2).withNano(0));
        p4.setPhotoFilename(null);
        p4.setUser(creator);
        List<Category> post4cat = new ArrayList<>();
        post4cat.add(category1);
        post4cat.add(category2);
        post4cat.add(category3);
        p4.setCategories(post4cat); //3 #'s
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of createPost and readPostById method, of class PostDao.
     */
    @Test
    public void testCreateReadPostByIdPost() {
        Post post1 = pDao.createPost(p1);

        Post fromDao = pDao.readPostById(post1.getId());

        assertNotNull(post1);
        assertNotNull(fromDao);
        assertEquals(post1, fromDao);
    }

    /**
     * Test of readAllPosts method, of class PostDao.
     */
    @Test
    public void testReadAllPosts() {
        Post post1 = pDao.createPost(p1);
        Post post2 = pDao.createPost(p2);
        Post post3 = pDao.createPost(p3);
        Post post4 = pDao.createPost(p4);

        List<Post> posts = pDao.readAllPosts();

        assertNotNull(posts);
        assertEquals(4, posts.size());
        assertTrue(posts.contains(post1));
        assertTrue(posts.contains(post2));
        assertTrue(posts.contains(post3));
        assertTrue(posts.contains(post4));
    }

    /**
     * Test of readPostsByDate method, of class PostDao.
     */
    @Test
    public void testReadPostsByDate() {
        Post post1 = pDao.createPost(p1);
        Post post2 = pDao.createPost(p2);
        Post post3 = pDao.createPost(p3);
        Post post4 = pDao.createPost(p4);

        List<Post> currentlyActive = pDao.readPostsByDate();

        assertNotNull(currentlyActive);
        assertEquals(2, currentlyActive.size());
        assertTrue(currentlyActive.contains(post1));
        assertTrue(currentlyActive.contains(post2));
        assertFalse(currentlyActive.contains(post3));
        assertFalse(currentlyActive.contains(post4));
    }

    /**
     * Test of readPostsByApproval method, of class PostDao.
     */
    @Test
    public void testReadPostsByApproval() {
        Post post1 = pDao.createPost(p1);
        Post post2 = pDao.createPost(p2);
        Post post3 = pDao.createPost(p3);
        Post post4 = pDao.createPost(p4);

        List<Post> approved = pDao.readPostsByApproval();

        assertNotNull(approved);
        assertEquals(2, approved.size());
        assertTrue(approved.contains(post1));
        assertFalse(approved.contains(post2));
        assertFalse(approved.contains(post3));
        assertTrue(approved.contains(post4));
    }

    /**
     * Test of readAllForPublication method, of class PostDao.
     */
    @Test
    public void testReadAllForPublication() {
        Post post1 = pDao.createPost(p1);
        Post post2 = pDao.createPost(p2);
        Post post3 = pDao.createPost(p3);
        Post post4 = pDao.createPost(p4);

        List<Post> publishable = pDao.readAllForPublication();

        assertNotNull(publishable);
        assertEquals(0, publishable.size());
        assertFalse(publishable.contains(post1));
        assertFalse(publishable.contains(post2));
        assertFalse(publishable.contains(post3));
        assertFalse(publishable.contains(post4));
    }
    
    /**
     * Test of readAllStatic method, of class PostDao.
     */
    @Test
    public void testReadAllStatic(){
        Post post1 = pDao.createPost(p1);
        Post post2 = pDao.createPost(p2);
        Post post3 = pDao.createPost(p3);
        Post post4 = pDao.createPost(p4);
        
        List<Post> statics = pDao.readAllStatic();
        assertEquals(2, statics.size());
        assertTrue(statics.contains(post1));
        assertFalse(statics.contains(post2));
        assertFalse(statics.contains(post3));
        assertTrue(statics.contains(post4));
    }

    /**
     * Test of readPostsByCategory method, of class PostDao.
     */
    @Test
    public void testReadPostsByCategory() {
        Post post1 = pDao.createPost(p1);
        Post post2 = pDao.createPost(p2);
        Post post3 = pDao.createPost(p3);
        Post post4 = pDao.createPost(p4);

        List<Post> cat1posts = pDao.readPostsByCategory(category1.getId());
        List<Post> cat2posts = pDao.readPostsByCategory(category2.getId());
        List<Post> cat3posts = pDao.readPostsByCategory(category3.getId());

        assertNotNull(cat1posts);
        assertNotNull(cat2posts);
        assertNotNull(cat3posts);
        assertEquals(4, cat1posts.size());
        assertTrue(cat1posts.contains(post1));
        assertTrue(cat1posts.contains(post2));
        assertTrue(cat1posts.contains(post3));
        assertTrue(cat1posts.contains(post4));
        assertEquals(3, cat2posts.size());
        assertTrue(cat2posts.contains(post1));
        assertFalse(cat2posts.contains(post2));
        assertTrue(cat2posts.contains(post3));
        assertTrue(cat2posts.contains(post4));
        assertEquals(2, cat3posts.size());
        assertTrue(cat3posts.contains(post1));
        assertFalse(cat3posts.contains(post2));
        assertFalse(cat3posts.contains(post3));
        assertTrue(cat3posts.contains(post4));
    }

    /**
     * Test of readPublishedPostsByCategory method, of class PostDao.
     */
    @Test
    public void testReadPublishedPostsByCategory() {
        Post post1 = pDao.createPost(p1); //only one that will be published
        Post post2 = pDao.createPost(p2);
        Post post3 = pDao.createPost(p3);
        Post post4 = pDao.createPost(p4);

        List<Post> cat1posts = pDao.readPublishedPostsByCategory(category1.getId());
        List<Post> cat2posts = pDao.readPublishedPostsByCategory(category2.getId());
        List<Post> cat3posts = pDao.readPublishedPostsByCategory(category3.getId());

        assertNotNull(cat1posts);
        assertNotNull(cat2posts);
        assertNotNull(cat3posts);
        assertEquals(1, cat1posts.size());
        assertTrue(cat1posts.contains(post1));
        assertFalse(cat1posts.contains(post2));
        assertFalse(cat1posts.contains(post3));
        assertFalse(cat1posts.contains(post4));
        assertEquals(1, cat2posts.size());
        assertTrue(cat2posts.contains(post1));
        assertFalse(cat2posts.contains(post2));
        assertFalse(cat2posts.contains(post3));
        assertFalse(cat2posts.contains(post4));
        assertEquals(1, cat3posts.size());
        assertTrue(cat3posts.contains(post1));
        assertFalse(cat3posts.contains(post2));
        assertFalse(cat3posts.contains(post3));
        assertFalse(cat3posts.contains(post4));
    }

    /**
     * Test of updatePost method, of class PostDao.
     */
    @Test
    public void testUpdatePost() {
        Post post1 = pDao.createPost(p1);
        Post original = pDao.readPostById(post1.getId());

        post1.setBody("This is the edited body to the post i hope this test passes");
        post1.setApproved(false);
        post1.setExpireOn(LocalDateTime.now().plusDays(5).withNano(0));

        Post post1update = pDao.updatePost(post1);
        Post edit = pDao.readPostById(post1.getId());

        assertNotNull(original);
        assertNotNull(post1update);
        assertNotNull(edit);
        assertNotEquals(original, edit);
        assertEquals(post1update, edit);
    }

    /**
     * Test of deletePostById method, of class PostDao.
     */
    @Test
    public void testDeletePostById() {
        Post post1 = pDao.createPost(p1);
        Post post2 = pDao.createPost(p2);
        Post post3 = pDao.createPost(p3);
        Post post4 = pDao.createPost(p4);
        List<Post> original = pDao.readAllPosts();

        boolean deleted = pDao.deletePostById(post1.getId());
        List<Post> afterDel = pDao.readAllPosts();

        assertEquals(4, original.size());
        assertEquals(3, afterDel.size());
        assertTrue(deleted);
        assertFalse(afterDel.contains(post1));
        assertTrue(afterDel.contains(post2));
        assertTrue(afterDel.contains(post3));
        assertTrue(afterDel.contains(post4));
    }

}
