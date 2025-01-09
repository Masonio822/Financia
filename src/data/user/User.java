package data.user;

import data.Transaction;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class User implements Serializable, Cloneable {
    private final String username;
    private final String password;
    private List<Transaction> transactions;
    private double balance;
    private final UUID uuid;

    //For the login user which should have a private constructor
    protected User() {
        this.username = "";
        this.password = "";
        this.uuid = null;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.transactions = new ArrayList<>();
        this.balance = 0;
        this.uuid = UUID.randomUUID();
    }

    public User(String username, String password, List<Transaction> transactions) {
        this(username, password);
        this.transactions = transactions;
        this.balance = transactions
                .stream()
                .mapToDouble(Transaction::amount)
                .sum();
    }

    public void save() {
        String path = System.getProperty("user.home") + "\\Documents\\Financia\\users";
        File file = new File(path);
        file.mkdirs();
        file = new File(path + "\\" + this.uuid + ".ser");
        try (
                FileOutputStream fileStream = new FileOutputStream(file);
                ObjectOutputStream objectStream = new ObjectOutputStream(fileStream)
        ) {
            //TODO encrypt with SHA256
            objectStream.writeObject(this);
        } catch (IOException e) {
            throw new RuntimeException("Could not save user!\nThis may be caused by invalid permissions");
        }
    }


    public boolean equalsIgnoreUuid(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username)
                && Objects.equals(password, user.password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username)
                && Objects.equals(password, user.password)
                && Objects.equals(uuid, user.uuid);
    }

    @Override
    public User clone() {
        try {
            return (User) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUsername() {
        return username;
    }

    public List<Transaction> getTransactions() {
        if (this.transactions == null) this.transactions = new ArrayList<>();
        return transactions;
    }

    public void addTransaction(Transaction t) {
        if (this.transactions == null) this.transactions = new ArrayList<>();
        this.transactions.add(t);
    }

    public String getPassword() {
        return password;
    }

    public double getBalance() {
        return balance;
    }

    public double updateBalance() {
        this.balance = this.transactions
                .stream()
                .mapToDouble(Transaction::amount)
                .sum();
        return balance;
    }

    public UUID getUuid() {
        return uuid;
    }
}
