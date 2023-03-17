package Entities;

import Enums.EnumBookType;
import Utils.FileService;

import java.io.IOException;

public class Book {
    private Integer id;
    private String title;
    private Author author;
    private String synopsis;
    private EnumBookType bookType;

    public Book() throws IOException {
        this.id = FileService.getLastIndexOfFile("db/Book.csv");
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public EnumBookType getBookType() {
        return bookType;
    }

    public void setBookType(EnumBookType bookType) {
        this.bookType = bookType;
    }

}
