package com.example.springBoot.controller;

/**
 * Created by Administrator on 2019/7/24.
 */
public class SellTicket {


        /**
         * @param args
         */
        public static void main(String[] args) {
            Ticket t = new Ticket();
            new Thread(t).start();
            new Thread(t).start();
            new Thread(t).start();
            new Thread(t).start();
        }
    }
    class Ticket implements Runnable{

        private int ticket = 10;
        public void run() {
                while(ticket>0){
                    synchronized(this){
                        if(ticket>0){
                            ticket--;
                        }
                        System.out.println("线程："+Thread.currentThread().getName()+"当前票数为："+ticket);
                    }
              }

        }

}


