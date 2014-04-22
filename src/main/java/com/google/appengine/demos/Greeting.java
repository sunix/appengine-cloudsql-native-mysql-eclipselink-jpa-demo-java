/**
 * Copyright 2012 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.appengine.demos;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "Greeting")
public class Greeting {
    protected Long   id;

    protected String author;
    protected Date   date;
    protected String content;


    public Greeting() {
    }

    public Greeting(String author, Date date, String content) {
        this.author = author;
        this.date = date;
        this.content = content;
    }

    public Greeting(Long id) {
        this.id = id;
    }


    public Greeting(Long id, String author, String content) {
        this.id = id;
        this.author = author;
        this.content = content;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Long getId() {
        return id;
    }

    protected void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Greeting) {
            return id != null && id.equals(((Greeting)obj).getId());
        }
        return super.equals(obj);
    }
}
