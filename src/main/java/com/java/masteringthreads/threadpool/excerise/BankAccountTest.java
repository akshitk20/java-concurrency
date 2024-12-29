package com.java.masteringthreads.threadpool.excerise;

import java.util.Timer;
import java.util.TimerTask;

/*
* Create a bank account with balance 1000
* Create a runnable lambda
* in the run() method call repeatedly :
* account.deposit(100)
* account.withdraw(100)
* create two thread instances running both pointing at your runnable
* create timer task to once a second prints out the balance
* balance should be 1000, 1100, 1200 nothing else
* */
public class BankAccountTest {

    public static void main(String[] args) {
        BankAccount account = new BankAccount(1000);
        Runnable task = () -> {
            while(true) {
                // below are not atomic so needs synchronization
                //account.deposit(100); // account.deposit = account.deposit+100
                //account.withdraw(100); // account.balance = account.balance-100
                synchronized (account) {
                    account.deposit(100);
                }
                Thread.yield();
                synchronized (account) {
                    account.withdraw(100);
                }
            }
        };

        Thread.ofPlatform().start(task);
        Thread.ofPlatform().start(task);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (account) {
                    System.out.println(account.getBalance());
                }
            }
        }, 1000, 1000);

    }


}
