import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class ProducerConsumerExample {
    private static final int BUFFER_CAPACITY = 5;
    private static final Queue<Integer> buffer = new LinkedList<>();
    private static final Random random = new Random();

    public static void main(String[] args) {
        Thread producerThread = new Thread(new Producer(), "ProducerThread");
        Thread consumerThread = new Thread(new Consumer(), "ConsumerThread");

        producerThread.start();
        consumerThread.start();
    }

    static class Producer implements Runnable {
        @Override
        public void run() {
            while (true) {
                synchronized (buffer) {
                    try {
                        while (buffer.size() == BUFFER_CAPACITY) {
                            System.out.println("Buffer is full. Producer is waiting...");
                            buffer.wait();
                        }

                        int newItem = random.nextInt(100) + 1;
                        System.out.println("Produced: " + newItem);
                        buffer.offer(newItem);

                        buffer.notify(); // Notify consumer thread that item is available
                        Thread.sleep(1000); // Simulate some delay
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }

    static class Consumer implements Runnable {
        @Override
        public void run() {
            while (true) {
                synchronized (buffer) {
                    try {
                        while (buffer.isEmpty()) {
                            System.out.println("Buffer is empty. Consumer is waiting...");
                            buffer.wait();
                        }

                        int item = buffer.poll();
                        System.out.println("Consumed: " + item);

                        buffer.notify(); // Notify producer thread that space is available
                        Thread.sleep(1500); // Simulate some delay
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }
}
