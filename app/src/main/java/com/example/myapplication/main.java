package com.example.myapplication;

public class main {
    public static void main(String[] args) {
        CanonAPI canon = new CanonAPI();
        if(canon.takePhoto()) {
            System.out.println("yes photo taken");
        } else {
            System.out.println("no photo taken");
        }
    }

}
