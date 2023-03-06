package ru.javaops.masterjava.matrix;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    // TODO implement parallel multiplication matrixA*matrixB
    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];
        int[] columnB = new int[matrixSize];

        Callable<int[][]> task = () -> getMultipliedMatrix(matrixA, matrixB, matrixSize, matrixC, columnB);

        return executor.submit(task).get();
    }

    // TODO optimize by https://habrahabr.ru/post/114797/
    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];
        int[] columnB = new int[matrixSize];

        return getMultipliedMatrix(matrixA, matrixB, matrixSize, matrixC, columnB);
    }

    private static int[][] getMultipliedMatrix(int[][] matrixA, int[][] matrixB, int matrixSize, int[][] matrixC, int[] columnB) {
        try {
            for (int i = 0; i < matrixSize; i++) {
                for (int j = 0; j < matrixSize; j++) {
                    System.arraycopy(matrixB[j], 0, columnB, 0, matrixSize);
                }

                for (int k = 0; k < matrixSize; k++) {
                    int[] rowA = matrixA[k];
                    int sum = 0;
                    for (int m = 0; m < matrixSize; m++) {
                        sum += rowA[m] * columnB[m];
                    }
                    matrixC[k][i] = sum;
                }
            }
        } catch (IndexOutOfBoundsException ignored) { }
        return matrixC;
    }


    public static int[][] create(int size) {
        int[][] matrix = new int[size][size];
        Random rn = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rn.nextInt(10);
            }
        }
        return matrix;
    }

    public static boolean compare(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
