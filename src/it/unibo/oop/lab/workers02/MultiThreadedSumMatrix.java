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
        private final List<Double> lis;
        private final int startpos;
        private final int nelem;
        private double res;

        Worker(final List<Double> l, final int startpos, final int nelem) {
            super();
            this.lis = l;
            this.startpos = startpos;
            this.nelem = nelem;
        }


        @Override
        public void run() {
            System.out.println("Working from position " + startpos + " to position " + (startpos + nelem - 1));
            for (int i = startpos; i < lis.size() && i < startpos + nelem; i++) {
                this.res += this.lis.get(i);
            }
        }

        public double getResult() {
            return this.res;
        }
    }
     @Override
    public double sum(final double[][] matrix) {
         final List<Double> l = new ArrayList<>();

         for (final double[] riga:matrix) {
             for (final double elem:riga) {
                 l.add(elem);
             }
         }

        final int size = l.size() % this.numThreads + l.size() / this.numThreads;

        final List<Worker> workers = new ArrayList<>(numThreads);
        for (int st = 0; st < l.size(); st += size) {
            workers.add(new Worker(l, st, size));
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
