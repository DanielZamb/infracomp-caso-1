package ProducerConsumer;

public class Producer extends Thread{
    private int id;
    private int numProductos;
    private Buffer pBuffer;
    private String tipoProd;
    private int actuales;

    public Producer(int id, int N, Buffer pBuffer,String tipoProd){
        this.id = id;
        numProductos = N;
        this.pBuffer = pBuffer;
        this.tipoProd = tipoProd;
        this.actuales = 0;
    }
    public void run(){
        int prodIndex = 0;
        while(numProductos != actuales){
            synchronized (this){
                while(pBuffer.isFull()){
                    Producer.yield();
                }
            }
            synchronized (pBuffer){
                String prod = tipoProd + prodIndex;
                prodIndex++;
                actuales++;
                System.out.println("P"+this.id+" has produced <"+prod+">");
                pBuffer.insert(prod);
            }
        }
        System.out.println("The thread managing P"+id+" has successfully terminated");
    }


}
