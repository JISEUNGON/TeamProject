package com.example.busapp;


public class QuickSort {

    public static String[] sort(String[] x, Compare method) {
        quickSort(0, x.length - 1, x, method);
        return x;
    }

    public static String[] sort(String[] x) {
        quickSort(0, x.length - 1, x, String::compareTo);
        return x;
    }

    private static void quickSort(int low, int high, String[] x, Compare method) {
        if (low < high) {
            int s = partition(low, high, x, method);
            quickSort(low, s - 1, x, method);
            quickSort(s + 1, high, x, method);
        }
    }

    private static void swap(int i, int j, String[] x) {
        String temp = x[i];
        x[i] = x[j];
        x[j] = temp;
    }
    private static int partition(int low, int high, String[] x, Compare method) {
        int i = low + 1;
        int j = high;

        while (i <= j) {
            if (method.compareTo(x[i], x[low]) <= 0) {
                i++;
            } else if(method.compareTo(x[j], x[low]) > 0) {
                j--;
            } else {
                swap(i, j, x);
                i++; j--;
            }
        }
        swap(low, j, x);
        return j;
    }
}
