package com.java.masteringthreads.threadpool.excerise;

public class BankAccount {
    private int balance;

    public BankAccount(int initial) {
        balance = initial;
    }

    public void deposit(int amount) {
        balance = balance + amount;
    }

    public void withdraw(int amount) {
        deposit(-amount);
    }

    public int getBalance() {
        return balance;
    }
}
