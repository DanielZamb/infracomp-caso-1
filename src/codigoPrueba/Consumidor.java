import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Consumidor extends Thread{
    
    private int id;
    private int maxProductos;
    private Buzon buzon;
    private boolean tipo;
    private int productosActuales;

    public Consumidor(int id, int maxProductos, Buzon buzon, Boolean tipo){
        this.id = id;
        this.maxProductos = maxProductos;
        this.buzon = buzon;
        this.tipo = tipo;
        this.productosActuales = 0;

    }

    @Override
    public void run(){
        while(maxProductos != productosActuales){
            synchronized(this){
                while(buzon.estaVacio()){
                    Consumidor.yield();
                }
            }
            synchronized(buzon){
                if(buzon.estaLleno()){

                    
                    Producto nuevoProducto = new Producto(tipoProductor);
                    productosActuales--;
                    String tipoP = tipo? "A" : "B";
                    System.out.println("El consumidor" + id + "ha consumido su producto no."+productosActuales+"de tipo"+tipoP+ "al buzon"+ buzon)
                    buzon.sacarProducto(tipo, producto);
                }
            }
        }
        System.out.println("El thread del consumidor"+ id + "ha finalizado");
    }

}

