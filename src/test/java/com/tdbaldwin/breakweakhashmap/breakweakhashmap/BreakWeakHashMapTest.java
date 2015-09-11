package com.tdbaldwin.breakweakhashmap.breakweakhashmap;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.dmg.pmml.FieldName;
import org.junit.Test;

public class BreakWeakHashMapTest {
    private final int threadCount = 8;

    @Test
    public void canBombard() {
        final ThreadPoolExecutor tpe = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; ++i) {
            tpe.execute(new Bombard());
        }

        final Random guess = new Random();
        int count;
        try {
            while ((count = tpe.getActiveCount()) > 0) {
                Thread.sleep(500);
                if (guess.nextBoolean()) {
                    System.gc();
                }
                System.out.println("Checking if finished. Still have " + count + " left.");
            }
        } catch (final InterruptedException ie) {
            System.out.println("Thread interrupted. " + ie.getMessage());
        }
    }
}

class Bombard implements Runnable {
    private final Random fun;
    private final ArrayList<FieldName> fns;

    public Bombard() {
        fun = new Random();
        fns = new ArrayList<FieldName>();
    }

    public void run() {
        for (int i = 0; i < 2000; ++i) {
            addFieldname();
        }
    }

    private void addFieldname() {
        final int len = fun.nextInt(10) + 5;

        final char[] str = new char[len];

        for (int i = 0; i < len; ++i) {
            str[i] = (char) (fun.nextInt(93) + 33);
        }

        fns.add(FieldName.create(new String(str)));
    }
}
