package Entities;

import Utils.FileService;

import java.io.IOException;

public class Loan {

    private Integer id;
    private User borrower;
    private Book borrowedBook;
    private int status;

    public Loan() throws IOException {
        this.id = FileService.getLastIndexOfFile("db/Loan.csv");
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getBorrower() {
        return borrower;
    }

    public void setBorrower(User borrower) {
        this.borrower = borrower;
    }

    public Book getBorrowedBook() {
        return borrowedBook;
    }

    public void setBorrowedBook(Book borrowedBook) {
        this.borrowedBook = borrowedBook;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
