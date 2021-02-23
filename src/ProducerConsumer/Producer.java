package ProducerConsumer;

public class Producer {
    private int numProductos;
    private Buffer pBuffer;
    private String tipoProd;
    public Producer(int N, Buffer pBuffer,String tipoProd){
        numProductos = N;
        this.pBuffer = pBuffer;
        this.tipoProd = tipoProd;
    }

}
