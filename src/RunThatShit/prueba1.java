package RunThatShit;

import java.util.LinkedList;

public class prueba1 {
    public static void main(String[] args) throws InterruptedException{
        final PC pmc = new PC();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try {
                    pmc.produce();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try {
                    pmc.pMiddleMan();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try {
                    pmc.cMiddleMan();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        // Create consumer thread
        Thread t4 = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try {
                    pmc.consume();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // Start both threads
        t1.start();
        t2.start();
        t3.start();
        t4.start();

        // t1 finishes before t2
        t1.join();
        t2.join();
        t3.join();
        t4.join();
    }
    public static class PC{
        LinkedList<Integer> pBuffer = new LinkedList<>();
        LinkedList<Integer> cBuffer = new LinkedList<>();
        LinkedList<Integer> iBuffer = new LinkedList<>();
        int pCap = 4;
        int cCap = 2;
        int iCap = 1;

        public void produce() throws InterruptedException{
            int producto = 0 ;
            while (true){
                synchronized (this){
                    while(pBuffer.size() == pCap){
                        wait();
                    }
                    System.out.println("P1 produced - " + producto);
                    pBuffer.addFirst(producto++);
                    notify();
                    Thread.sleep(250);
                }
            }
        }
        public void pMiddleMan() throws InterruptedException{
            while (true){
                //this <-- objeto sobre el cual se obtiene el bloqueo.
                synchronized (this){
                    while (pBuffer.isEmpty()||iBuffer.size() == 1) {
                        wait();
                    }
                    int product = pBuffer.removeLast();
                    String s = "P1 = [";
                    System.out.println("P_MiddleMan has received <"+ product+">");
                    for (Integer n:
                         pBuffer) {
                        s+= n+",";
                    }
                    s += "]";
                    System.out.println(s);
                    iBuffer.addFirst(product);
                    System.out.println("P_MiddleMan has delivered <"+ product+">");
                    notify();
                    Thread.sleep(250);
                }
            }
        }
        public void cMiddleMan() throws InterruptedException{
            while (true){
                //this <-- objeto sobre el cual se obtiene el bloqueo.
                synchronized (this){
                    while (cBuffer.size() == cCap || iBuffer.size() == 0){
                        wait();
                    }
                    int product = iBuffer.removeLast();
                    System.out.println("C_MiddleMan has received <"+ product+">");
                    cBuffer.addFirst(product);
                    System.out.println("C_MiddleMan has delivered <"+ product+">");
                    String s = "C1 = [";
                    for (Integer n:
                            cBuffer) {
                        s+= n+",";
                    }
                    s += "]";
                    notify();
                    Thread.sleep(250);
                }
            }
        }
        public void consume() throws InterruptedException{
            while (true){
                synchronized (this){
                    while(cBuffer.isEmpty()){
                        wait();
                    }
                    int product = cBuffer.removeLast();
                    System.out.println("C1 consumed - " + product);
                    notify();
                    Thread.sleep(250);
                }
            }
        }

    }
}
