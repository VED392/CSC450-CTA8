import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public final class CountingApp {
    //Latch used to signal when couting up is done
    private static final CountDownLatch latch = new CountDownLatch(1);
    //Atomic counter to ensure thread safe acccess
    private static final AtomicInteger counter = new AtomicInteger(0);
    //Thread safe ouput collection
    private static final List<String> output = new CopyOnWriteArrayList<>();

    private static String formatCountingUp (int i){
        return String.format("Counting up: %d", i);
    }

    //Secure formatter for counting down
    private static String formatCountingDown (int i){
        return String.format("Counting down: %d", i);}

    

    //Runnable that counts up to 1 to 20
    private static class CountUp implements Runnable {
        @Override
        public void run() {
            try{
                for (int i=1; i<=20; i++){
                    Thread.sleep(100); //Simulate work
                    counter.set(i);
                    output.add(formatCountingUp(i));
                    System.out.println("CountUp updated counter: " + i);
                }//signal completion
                latch.countDown();
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
                System.err.println("CountUp interrupted");
            }catch(Throwable t){
                System.err.println("CountUp error: " + t.getMessage());
            }
        }
    }

    //Runnable that counts down from 20 to 1 after CountUp is done
    private static class CountDown implements Runnable{
        @Override
        public void run() {
            try {
                latch.await(); //wait for count up to finish
                int start = counter.get();
                System.out.println("CountDown starts. Counter value: " + start);
                for (int i = start; i >= 0; i--){
                    Thread.sleep(100); //Simulate work
                    output.add(formatCountingDown(i));
                    System.out.println("CountDown updated counter: " + i);
                    counter.set(i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("CountDown interrupted");
            } catch (Throwable t){
                System.err.println("CountDown error: " + t.getMessage());
            }
        }
    }

    //Main method that initiates threads and prints ouput
    public static void main(String[] args) {
        Thread t1 = new Thread(new CountUp());
        Thread t2 = new Thread(new CountDown());

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Main  interrupted during join");
        }

        //securely print final output
        List<String> safeOutput = Collections.unmodifiableList(List.copyOf(output));
        System.out.println("Final Output:");
        for (String line: safeOutput){
            System.out.println(line);
        }
    }
}
