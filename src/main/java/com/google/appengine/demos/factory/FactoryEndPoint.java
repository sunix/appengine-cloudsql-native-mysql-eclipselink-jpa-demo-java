package com.google.appengine.demos.factory;

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
 * Using google end points Defines v1 of a factory API
 */
@Api(name = "factory", version = "v1")
public class FactoryEndPoint {

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
    public Factory addFactory(@Nullable @Named("id") Long id, @Named("owner") String owner, @Named("url") String url) throws NotFoundException {
        // Check for already exists
        if (id == null) {
            id = new Random().nextLong();
        }

        Factory g = new Factory(id, owner, url);
        EntityManager em = createEM();
        em.getTransaction().begin();
        em.persist(g);
        em.getTransaction().commit();
        em.close();
        return g;
    }


    @ApiMethod(name = "update")
    public Factory updateFactory(Factory g) throws NotFoundException {

        EntityManager em = createEM();
        em.getTransaction().begin();
        Factory currentFactory = em.find(Factory.class, g.id);

        currentFactory.setOwner(g.getOwner());
        currentFactory.setUrl(g.getUrl());

        em.persist(currentFactory);
        em.getTransaction().commit();
        em.close();

        return g;
    }


    @ApiMethod(name = "remove")
    public void removeFactory(@Named("id") Long id) throws NotFoundException {
        EntityManager em = createEM();
        em.getTransaction().begin();
        Factory currentFactory = em.find(Factory.class, id);
        em.remove(currentFactory);
        em.getTransaction().commit();
        em.close();
    }

    @ApiMethod(name = "list")
    public List<Factory> getFactories() {
        EntityManager em = createEM();
        em.getTransaction().begin();
        List<Factory> result = em
                                 .createQuery("SELECT g FROM Factory g")
                                 .getResultList();
        em.getTransaction().commit();
        em.close();

        return result;
    }


    @ApiMethod(name = "getFactory")
    public Factory getFactory(@Named("id") Long id) throws NotFoundException {
        EntityManager em = createEM();
        Factory currentFactory = em.find(Factory.class, id);
        em.close();
        return currentFactory;
    }

}
