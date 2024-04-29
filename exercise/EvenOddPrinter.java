public class EvenOddPrinter {
    private static volatile boolean flag = true; // Use volatile to ensure visibility across threads

    public static void main(String[] args) {
        Runnable odd = () -> {
            for (int i = 1; i <= 10; i += 2) {
                synchronized (EvenOddPrinter.class) {
                    while (!flag) {
                        try {
                            EvenOddPrinter.class.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println(Thread.currentThread().getName() + " " + i);
                    flag = false;
                    EvenOddPrinter.class.notify(); // Notify waiting thread (even thread)
                }
            }
        };

        Runnable even = () -> {
            for (int i = 2; i <= 10; i += 2) {
                synchronized (EvenOddPrinter.class) {
                    while (flag) {
                        try {
                            EvenOddPrinter.class.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println(Thread.currentThread().getName() + " " + i);
                    flag = true;
                    EvenOddPrinter.class.notify(); // Notify waiting thread (odd thread)
                }
            }
        };

        Thread t1 = new Thread(odd, "OddThread");
        Thread t2 = new Thread(even, "EvenThread");

        t1.start();
        t2.start();
    }
}
