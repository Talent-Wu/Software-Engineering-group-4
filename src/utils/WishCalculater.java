package utils;

import model.Wallet;
import model.Wish;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class WishCalculater {
    public static List<Wish> calculateComplete() {
        List<Wallet> incomes = DataUtil.readIncome();
        incomes.sort(Comparator.comparing(Wallet::getTime));

        List<Wish> wishList = DataUtil.readWishList();
        double total = 0.0;

        for (Wish wish : wishList) {

            boolean last100 = false;

            Iterator<Wallet> it = incomes.iterator();
            while (it.hasNext()) {
                Wallet income = it.next();
                if (income.getTime().compareTo(wish.getTime()) > 0) {
                    total += income.getAmount();
                    if (total >= wish.getAmount()) {
                        wish.setComplete("100%");

                        total = total - wish.getAmount();

                        it.remove();
                        last100 = true;
                        break;
                    }
                }
            }

            if(last100) {
                continue;
            }

            double percent = total / wish.getAmount();
            wish.setComplete(percent * 100.0 + "%");
            break;
        }

        return wishList;
    }

}
