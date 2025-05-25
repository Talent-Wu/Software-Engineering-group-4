package utils;

import model.Wallet;
import model.Wish;

import java.util.Comparator;
import java.util.List;

public class WishCalculater {
    public static List<Wish> calculateComplete(List<Wallet> incomes, List<Wallet> expenses, List<Wish> wishList) {
        // 按时间排序
        incomes.sort(Comparator.comparing(Wallet::getTime));
        expenses.sort(Comparator.comparing(Wallet::getTime));

        // 初始化净收入
        double netIncome = 0.0;

        // 计算总收入和总支出
        for (Wallet income : incomes) {
            netIncome += income.getAmount();
        }
        for (Wallet expense : expenses) {
            netIncome += expense.getAmount(); // 支出是负数，所以直接加
        }

        // 遍历愿望列表，计算每个愿望的完成比例
        for (int i = 0; i < wishList.size(); i++) {
            Wish wish = wishList.get(i);
            if (netIncome >= wish.getAmount()) {
                wish.setComplete("100%");
                netIncome -= wish.getAmount();
            } else {
                // 添加净收入小于0的判断
                if (netIncome < 0) {
                    wish.setComplete("0%");
                } else {
                    double percent = (netIncome / wish.getAmount()) * 100;
                    wish.setComplete(String.format("%.2f", percent) + "%");
                }
                // 将后续愿望的完成度清零
                for (int j = i + 1; j < wishList.size(); j++) {
                    wishList.get(j).setComplete("0%");
                }
                break; // 如果当前愿望未完成，后续愿望也不再计算
            }
        }

        return wishList;
    }
}