package test;

import org.junit.jupiter.api.Test;
import model.Wallet;
import model.Wish;
import utils.WishCalculater;

import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class WishCalculaterTest {

    @Test
    void testCalculateComplete() {
        List<Wallet> incomes = Arrays.asList(new Wallet("salary", 2000, "CompanyA", "2024-01-01 12:00:00"));
        List<Wallet> expenses = Arrays.asList(new Wallet("rent", -1000, "Landlord", "2024-01-01 12:00:00"));
        List<Wish> wishList = Arrays.asList(new Wish("New Phone", 500, "2024-01-01 12:00:00"));

        List<Wish> result = WishCalculater.calculateComplete(incomes, expenses, wishList);

        assertNotNull(result);
        assertEquals("100%", result.get(0).getComplete());
    }
}