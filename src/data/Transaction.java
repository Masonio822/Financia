package data;

import java.io.Serializable;
import java.time.LocalDate;

public record Transaction(String company, double amount, LocalDate date, boolean reoccurring) implements Serializable {
    public Object[] toArray() {
        return new Object[] {company, amount, date, reoccurring};
    }
}
