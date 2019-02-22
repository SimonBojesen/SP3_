package dbfacades;

import dbfacades.DemoFacade;
import entity.Car;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * UNIT TEST example that mocks away the database with an in-memory database See
 * Persistence unit in persistence.xml in the test packages
 *
 * Use this in your own project by: - Delete everything inside the setUp method,
 * but first, observe how test data is created - Delete the single test method,
 * and replace with your own tests
 *
 */
public class FacadeTest {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu-test", null);

    DemoFacade facade = new DemoFacade(emf);

    /**
     * Setup test data in the database to a known state BEFORE Each test
     */
    @Before
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            //Delete all, since some future test cases might add/change data
            em.createNativeQuery("TRUNCATE TABLE car").executeUpdate();
            //Add our test data
            Car e1 = new Car("Volve");
            Car e2 = new Car("WW");
            em.persist(e1);
            em.persist(e2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    // Test the single method in the Facade
    @Test
    public void countEntities() {
        long count = facade.countCars();
        Assert.assertEquals(2, count);
    }

    @Test
    public void getCarByMake() {
        Car c = facade.getCarByMake("WW");
        Assert.assertEquals("WW", c.getMake());
    }
    
    @Test
    public void getCarById() {
        Car c = facade.getCarById( 2);
        Assert.assertEquals(2, (int) c.getId());
    }
    
    @Test
    public void deleteCarById() {
        facade.deleteCarById(1);
        Car cc = facade.getCarById(1);
        Assert.assertNull(cc);
    }

    @Test
    public void getAllCars() {
        EntityManager em = emf.createEntityManager();
        try {
            List<Car> expectedCars = facade.getAllCars();
            Assert.assertEquals(2, expectedCars.size());
        } finally {
            em.close();
        }
    }
//    @Test
//    public void failtest(){
//        Assert.assertTrue(false);
//    }
//    
    
}
