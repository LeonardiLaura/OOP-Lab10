package it.unibo.oop.lab.reactivegui03;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class AnotherConcurrentGUI {
    private static final long serialVersionUID = 1L;
    private static final double WIDTH_PERC = 0.2;
    private static final double HEIGHT_PERC = 0.1;
    private final JFrame frame = new JFrame();
    private final JLabel label = new JLabel("0");
    private final JPanel panel = new JPanel();
    private final JButton bUp = new JButton("up");
    private final JButton bDown = new JButton("down");
    private final JButton bStop = new JButton("stop");
    private final Agent agent = new Agent();

    public AnotherConcurrentGUI() {
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize((int) (screenSize.getWidth() * WIDTH_PERC), (int) (screenSize.getHeight() * HEIGHT_PERC));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        panel.add(label);
        panel.add(bUp);
        panel.add(bDown);
        panel.add(bStop);
        frame.getContentPane().add(panel);
        frame.setVisible(true);

        new Thread(agent).start();

        final TimeOutAgent agent2 = new TimeOutAgent();
        new Thread(agent2).start();

        bUp.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                agent.changeToUp();
            }
        });

        bDown.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                agent.changeToDown();
            }
        });

        bStop.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                agent.stopCounting();
            }
        });
    }

    private class Agent implements Runnable {
        private boolean stop;
        private int counter;
        private boolean dirUp = true;

        @Override
        public void run() {
            while (!this.stop) {
                try {
                    if (this.dirUp) {
                        this.counter++;
                    } else {
                        this.counter--;
                    }
                    SwingUtilities.invokeAndWait(() -> AnotherConcurrentGUI.this.label.setText(Integer.toString(this.counter)));
                    Thread.sleep(100);
                } catch (InvocationTargetException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }

        public void stopCounting() {
            this.stop = true;
        }
        public void changeToUp() {
            this.dirUp = true;
        }
        public void changeToDown() {
            this.dirUp = false;
        }
    }
    private class TimeOutAgent implements Runnable {
        private static final int TIME = 10_000;
        @Override
        public void run() {
           try {
               Thread.sleep(TIME);
               AnotherConcurrentGUI.this.agent.stopCounting();
               AnotherConcurrentGUI.this.bDown.setEnabled(false);
               AnotherConcurrentGUI.this.bUp.setEnabled(false);
               AnotherConcurrentGUI.this.bStop.setEnabled(false);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
        }
    }
}
