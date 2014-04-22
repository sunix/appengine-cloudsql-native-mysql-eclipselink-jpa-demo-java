package com.google.appengine.demos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.google.api.server.spi.response.NotFoundException;

/**
 * Defines v1 of a helloworld API, which provides simple "greeting" methods.
 */
@Api(name = "helloworld", version = "v1", description = "Without Google Cloud SQL to save money")
public class GreetingsEndPointInMemory implements GreetingsService {

    public static List<Greeting> greetings = new ArrayList<Greeting>();

    @ApiMethod(name = "add")
    public Greeting addGreeting(@Nullable @Named("id") Long id, @Named("author") String author, @Named("content") String content) throws NotFoundException {
        // Check for already exists
        if (id == null) {
            id = new Random().nextLong();
        }
        int index = greetings.indexOf(new Greeting(id));
        if (index != -1)
            throw new NotFoundException("Greeting Record already exists");

        Greeting g = new Greeting(id, author, content);
        greetings.add(g);
        return g;
    }

    @ApiMethod(name = "update")
    public Greeting updateGreeting(Greeting g) throws NotFoundException {
        int index = greetings.indexOf(g);
        if (index == -1)
            throw new NotFoundException("Greeting Record does not exist");
        Greeting currentGreeting = greetings.get(index);
        currentGreeting.setAuthor(g.getAuthor());
        currentGreeting.setContent(g.getContent());
        return g;
    }

    @ApiMethod(name = "remove")
    public void removeGreeting(@Named("id") Long id) throws NotFoundException {
        int index = greetings.indexOf(new Greeting(id));
        if (index == -1)
            throw new NotFoundException("Greeting Record does not exist");
        greetings.remove(index);
    }

    @ApiMethod(name = "list")
    public List<Greeting> getGreetings() {
        return greetings;
    }

    @ApiMethod(name = "listByAuthor")
    public List<Greeting> getGreetingsByAuthor(@Named("author") String author) {
        List<Greeting> results = new ArrayList<Greeting>();
        for (Greeting greeting : greetings) {
            if (greeting.getAuthor().indexOf(author) != -1) {
                results.add(greeting);
            }
        }
        return results;
    }

    @ApiMethod(name = "getGreeting")
    public Greeting getGreeting(@Named("id") Long id) throws NotFoundException {
        int index = greetings.indexOf(new Greeting(id));
        if (index == -1)
            throw new NotFoundException("Greeting Record does not exist");
        return greetings.get(index);
    }

}
