package com.google.appengine.demos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.utils.SystemProperty;

/**
 * Using google end points Defines v1 of a helloworld API, which provides simple "greeting" methods.
 */
@Api(name = "helloworldsql", version = "v1")
public class GreetingsEndPoint implements GreetingsService {

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


    @ApiMethod(name = "add")
    public Greeting addGreeting(@Nullable @Named("id") Long id, @Named("author") String author, @Named("content") String content) throws NotFoundException {
        // Check for already exists
        if (id == null) {
            id = new Random().nextLong();
        }

        Greeting g = new Greeting(id, author, content);
        EntityManager em = createEM();
        em.getTransaction().begin();
        em.persist(g);
        em.getTransaction().commit();
        em.close();
        return g;
    }


    @ApiMethod(name = "update")
    public Greeting updateGreeting(Greeting g) throws NotFoundException {

        EntityManager em = createEM();
        em.getTransaction().begin();
        Greeting currentGreeting = em.find(Greeting.class, g.id);

        currentGreeting.setAuthor(g.getAuthor());
        currentGreeting.setContent(g.getContent());
        currentGreeting.setDate(g.getDate());

        em.persist(currentGreeting);
        em.getTransaction().commit();
        em.close();

        return g;
    }


    @ApiMethod(name = "remove")
    public void removeGreeting(@Named("id") Long id) throws NotFoundException {
        EntityManager em = createEM();
        em.getTransaction().begin();
        Greeting currentGreeting = em.find(Greeting.class, id);
        em.remove(currentGreeting);
        em.getTransaction().commit();
        em.close();
    }

    @ApiMethod(name = "list")
    public List<Greeting> getGreetings() {
        EntityManager em = createEM();
        em.getTransaction().begin();
        List<Greeting> result = em
                                  .createQuery("SELECT g FROM Greeting g")
                                  .getResultList();
        em.getTransaction().commit();
        em.close();

        return result;
    }

    @ApiMethod(name = "listByAuthor")
    public List<Greeting> getGreetingsByAuthor(@Named("author") String author) {
        EntityManager em = createEM();
        em.getTransaction().begin();
        // be careful sql injection if real app
        List<Greeting> result = em
                                  .createQuery("SELECT g FROM Greeting g where author like '%" + author + "%'")
                                  .getResultList();
        em.getTransaction().commit();
        em.close();
        return result;
    }

    @ApiMethod(name = "getGreeting")
    public Greeting getGreeting(@Named("id") Long id) throws NotFoundException {
        EntityManager em = createEM();
        Greeting currentGreeting = em.find(Greeting.class, id);
        em.close();
        return currentGreeting;
    }

}
