package com.example.busapp;

public class QuickSort {
    public static String[] sort(String[] x) {
        quickSort(0, x.length - 1, x);
        return x;
    }

    private static void quickSort(int low, int high, String[] x) {
        if (low < high) {
            int s = partition(low, high, x);
            quickSort(low, s - 1, x);
            quickSort(s + 1, high, x);
        }
    }

    private static int parseInt(String time) {
        return Integer.parseInt(time.replace(":", ""));
    }

    private static void swap(int i, int j, String[] x) {
        String temp = x[i];
        x[i] = x[j];
        x[j] = temp;
    }
    private static int partition(int low, int high, String[] x) {
        int i = low + 1;
        int j = high;

        while (i <= j) {
            if (DateFormat.compare(x[i], x[low]) <= 0) {
                i++;
            } else if(DateFormat.compare(x[j], x[low]) > 0) {
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
