package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import model.Wallet;
import utils.DataUtil;

import java.util.List;

public class WalletController {

    @FXML
    public Label income;
    public Label expenses;
    public Label income1;
    public Label income2;
    public Label income3;
    public Label income4;
    public Label income5;
    public Label income6;
    public Label expenses1;
    public Label expenses2;
    public Label expenses3;

    private String moneyFlag = "￥";

    @FXML
    public void initialize() {
        List<Wallet> list = DataUtil.readIncome();
        double totalIncome = 0.0;
        double a1 = 0.0;
        double a2 = 0.0;
        double a3 = 0.0;
        double a4 = 0.0;
        double a5 = 0.0;
        double a6 = 0.0;
        for (Wallet wallet : list) {
            totalIncome += wallet.getAmount();
            switch (wallet.getCompany()) {
                case "Industrial and Commercial Bank of China":
                    a1 += wallet.getAmount();
                    break;
                case "Bank of China":
                    a2 += wallet.getAmount();
                    break;
                case "China Construction Bank":
                    a3 += wallet.getAmount();
                    break;
                case "WeChat Pay":
                    a4 += wallet.getAmount();
                    break;
                case "Alipay":
                    a5 += wallet.getAmount();
                    break;
                case "Apple Pay":
                    a6 += wallet.getAmount();
                    break;
            }
        }
        income.setText(moneyFlag + String.valueOf((int)totalIncome));
        income1.setText(moneyFlag + String.valueOf((int)a1));
        income2.setText(moneyFlag + String.valueOf((int)a2));
        income3.setText(moneyFlag + String.valueOf((int)a3));
        income4.setText(moneyFlag + String.valueOf((int)a4));
        income5.setText(moneyFlag + String.valueOf((int)a5));
        income6.setText(moneyFlag + String.valueOf((int)a6));


        List<Wallet> list1 = DataUtil.readExpense();
        double totalExpenses = 0.0;
        double e1 = 0.0;
        double e2 = 0.0;
        double e3 = 0.0;
        for (Wallet wallet : list1) {
            totalExpenses += wallet.getAmount();
            switch(wallet.getCompany()) {
                case "JD White Stripe":
                    e1 += wallet.getAmount();
                    break;
                case "Meituan":
                    e2 += wallet.getAmount();
                    break;
                case "McDonald's":
                    e3 += wallet.getAmount();
                    break;
            }
        }
        expenses.setText(moneyFlag + String.valueOf((int)totalExpenses));
        expenses1.setText(moneyFlag + String.valueOf((int)e1));
        expenses2.setText(moneyFlag + String.valueOf((int)e2));
        expenses3.setText(moneyFlag + String.valueOf((int)e3));
    }

    public void clear(MouseEvent mouseEvent) {
        DataUtil.clearWallet();
        initialize();
    }
}
