package com.google.appengine.demos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.appengine.api.utils.SystemProperty;

/**
 * Using google end points
 * Defines v1 of a helloworld API, which provides simple "greeting" methods.
 */
@Api(name = "helloworldsql", version = "v1")
public class GreetingsEndPoint {

    public List<Greeting> getAllGreating() {

        EntityManager em = createEM();
        em.getTransaction().begin();
        List<Greeting> result = em
                                  .createQuery("SELECT g FROM Greeting g")
                                  .getResultList();
        em.getTransaction().commit();
        em.close();

        return result;
    }


    protected EntityManager createEM() {
        Map<String, String> properties = new HashMap();
        if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
            properties.put("javax.persistence.jdbc.driver",
                           "com.mysql.jdbc.GoogleDriver");
            properties.put("javax.persistence.jdbc.url",
                           System.getProperty("cloudsql.url"));
        } else {
            properties.put("javax.persistence.jdbc.driver",
                           "com.mysql.jdbc.Driver");
            properties.put("javax.persistence.jdbc.url",
                           System.getProperty("cloudsql.url.dev"));
        }

        EntityManagerFactory emf = Persistence.createEntityManagerFactory(
                                                                          "Demo", properties);

        // Insert a few rows.
        EntityManager em = emf.createEntityManager();
        // List all the rows.
        em = emf.createEntityManager();
        return em;
    }


    @ApiMethod(name = "insert", httpMethod = "post")
    public void insertGreeting(Greeting greeting) {
        EntityManager em = createEM();
        em.getTransaction().begin();
        em.persist(greeting);
        em.getTransaction().commit();
        em.close();
    }


}
