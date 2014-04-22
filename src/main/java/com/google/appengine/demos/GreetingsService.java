package com.google.appengine.demos;

import java.util.List;

import com.google.api.server.spi.response.NotFoundException;

public interface GreetingsService {

    public Greeting addGreeting(Long id, String author, String content) throws NotFoundException;

    public Greeting updateGreeting(Greeting g) throws NotFoundException;

    public void removeGreeting(Long id) throws NotFoundException;

    public List<Greeting> getGreetings();

    public List<Greeting> getGreetingsByAuthor(String author);

    public Greeting getGreeting(Long id) throws NotFoundException;

}
