package it.unibo.oop.lab.workers02;

import java.util.ArrayList;
import java.util.List;

//import it.unibo.oop.lab.workers01.MultiThreadedListSumClassic.Worker;

public class MultiThreadedSumMatrix implements SumMatrix {
    private final int numThreads;

    public MultiThreadedSumMatrix(final int num) {
        this.numThreads = num;
    }

    private static class Worker extends Thread {
        private final double[][] mat;
        private final int startpos;
        private final int nelem;
        private double res;

        Worker(final double[][] m, final int startpos, final int nelem) {
            super();
            this.mat = m;
            this.startpos = startpos;
            this.nelem = nelem;
        }


        @Override
        public void run() {
            int r = this.startpos / mat[0].length;
            int c = this.startpos % mat[0].length;
            System.out.println("Working from position " + startpos + " to position " + (startpos + nelem - 1));
            for (int i = startpos; i < (mat.length * mat[0].length) && i < startpos + nelem; i++) {
                this.res += this.mat[r][c];
                c++;
                if (c >= mat[0].length) {
                    c = 0;
                    r++;
                }
            }
        }

        public double getResult() {
            return this.res;
        }
    }
     @Override
    public double sum(final double[][] matrix) {
         final int size = (matrix.length * matrix[0].length) % this.numThreads + (matrix.length * matrix[0].length) / this.numThreads;

         final List<Worker> workers = new ArrayList<>(numThreads);
         for (int st = 0; st < (matrix.length * matrix[0].length); st += size) {
             workers.add(new Worker(matrix, st, size));
         }


        for (final Worker w: workers) {
            w.start();
        }
        double sum = 0;
        for (final Worker w: workers) {
            try {
                w.join();
                sum += w.getResult();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
        return sum;
    }


}
