package Entities;

import Utils.FileService;

import java.io.IOException;
import java.util.ArrayList;

public class Author {
    private Integer id;
    private String firstName;
    private String lastName;

    public Author() throws IOException {
        this.id = FileService.getLastIndexOfFile("db/Author.csv");
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
