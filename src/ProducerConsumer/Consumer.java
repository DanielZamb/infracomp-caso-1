package ProducerConsumer;

public class Consumer {
    private int numProductos;
    private Buffer cBuffer;
    private String tipoProd;
    public Consumer(int N, Buffer cBuffer, String tipoProd){
        numProductos = N;
        this.cBuffer = cBuffer;
        this.tipoProd = tipoProd;
    }
}
