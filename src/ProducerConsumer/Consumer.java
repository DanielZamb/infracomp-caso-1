package ProducerConsumer;

public class Consumer extends Thread{
    private int id;
    private int numProductos;
    private Buffer cBuffer;
    private String tipoProd;
    private int actuales;
    public Consumer(int id,int N, Buffer cBuffer, String tipoProd){
        this.id = id;
        numProductos = N;
        this.cBuffer = cBuffer;
        this.tipoProd = tipoProd;
        actuales=0;
    }
    public void run(){
        while (numProductos != actuales) {
            synchronized (this){
                while (cBuffer.isEmpty()){
                    Consumer.yield();
                }
            }
            synchronized (cBuffer){
                if (cBuffer.size()>0){
                    String prod = (String) cBuffer.remove();
                    actuales++;
                    System.out.println("C"+this.id+" has consumed <"+prod+">");
                }
            }
        }
        System.out.println("The thread managing C"+id+" has successfully terminated");
    }
}
