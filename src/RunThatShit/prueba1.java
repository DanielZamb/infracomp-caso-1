package RunThatShit;

import java.util.LinkedList;

public class prueba1 {
    public static void main(String[] args) throws InterruptedException{
        final PC pmc = new PC();
        Thread p1 = new Thread(new Runnable() {
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
        Thread p2 = new Thread(new Runnable() {
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

        Thread pM = new Thread(new Runnable() {
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
        Thread cM = new Thread(new Runnable() {
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
        Thread c1 = new Thread(new Runnable() {
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
        Thread c2 = new Thread(new Runnable() {
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
        p1.start();
        p2.start();
        pM.start();
        cM.start();
        c1.start();
        c2.start();

        /*p1.setPriority(3);
        p2.setPriority(3);
        pM.setPriority(8);
        cM.setPriority(8);
        c1.setPriority(3);
        c2.setPriority(3);*/
        // t1 finishes before t2
        p1.join();
        p2.join();
        pM.join();
        cM.join();
        c1.join();
        c2.join();
    }
    public static class PC{
        static LinkedList<Integer> pBuffer = new LinkedList<>();
        static LinkedList<Integer> cBuffer = new LinkedList<>();
        static LinkedList<Integer> iBuffer = new LinkedList<>();
        static int pCap = 3;
        static int cCap = 3;
        static int iCap = 1;

        public void produce() throws InterruptedException{
            int producto = 0 ;
            while (true){
                synchronized (this){
                    while(pBuffer.size() == pCap){
                        //notifyAll();
                        //Thread.yield();
                        wait();
                    }
                    System.out.println("P"+Thread.currentThread().getId()+" produced - " + producto);
                    pBuffer.addFirst(producto++);
                    notifyAll();
                    //Thread.sleep(250);
                }
            }
        }
        public void pMiddleMan() throws InterruptedException{
            while (true){
                //this <-- objeto sobre el cual se obtiene el bloqueo.
                synchronized (this){
                    while (pBuffer.isEmpty()||iBuffer.size() == iCap) {
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
                    notifyAll();
                    //Thread.sleep(250);
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
                    System.out.println(s);
                    notifyAll();
                    //Thread.sleep(250);
                }
            }
        }
        public void consume() throws InterruptedException{
            while (true){
                synchronized (this){
                    while(cBuffer.isEmpty()){
                        notify();
                        wait();
                        //Thread.yield();
                    }
                    int product = cBuffer.removeLast();
                    System.out.println("C"+ Thread.currentThread().getId()+" consumed - " + product);
                    notifyAll();
                    //Thread.sleep(250);
                }
            }
        }

    }
    //DEAD FKN LOCKS WITH NOTIFY
}
