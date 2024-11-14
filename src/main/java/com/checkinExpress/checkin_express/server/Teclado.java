package com.checkinExpress.checkin_express.server;

import java.util.Scanner;

public class Teclado {
    private static Scanner scanner = new Scanner(System.in);

    public static String getUmString() {
        return scanner.nextLine();
    }
}