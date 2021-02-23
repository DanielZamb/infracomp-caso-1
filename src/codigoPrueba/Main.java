import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class Main extends Thread{

    @Override
    public void run(){
        Productor[] productores;

        Consumidor[] consumidores;

        Buzon buzonProductores;

        Buzon buzonConsumidores;

        Buzon buzonIntermediarios;

        Intermediario inter1;

        Intermediario inter2;

        try(InputStream input = new FileInputStream("data/infor.properties")){

            Properties prop = new Properties();

            prop.load(input);

            int numProdCons = Integer.parseInt(prop.getProperty("concurrencia.numProdCons"));
            int numProductos = Integer.parseInt(prop.getProperty("concurrencia.numProductos"));
            int buzonesProd = Integer.parseInt(prop.getProperty("concurrencia.buzonesProd"));
            int buzonesCons = Integer.parseInt(prop.getProperty("concurrencia.buzonesCons"));

            buzonProductores = new Buzon(1,buzonesProd, null, false);
            buzonIntermediarios = new Buzon(2,1,null, false);
            buzonConsumidores= new Buzon(3, buzonesCons, null, true);
            inter1 = new Intermediario(1,buzonProductores, buzonIntermediarios, (numProductos*numProdCons));
            inter2 = new Intermediario(2,buzonIntermediarios, buzonConsumidores, (numProductos*numProdCons));
            buzonProductores.setIntermediario(inter1);
            buzonIntermediarios.setIntermediario(inter2);
            productores = new Productor[numProdCons];
            consumidores = new Consumidor[numProdCons];
            for(int i = 0; i<numProdCons/2; i++){
                productores[i] = new Productor (i, numProductos, buzonProductores, true);
                consumidores[i]= new Consumidor (i, numProductos, true, buzonConsumidores);
            }
            for(int i = numProdCons/2; i<numProdCons; i++){
                productores[i]= new Productor(i, numProductos, buzonProductores, false);
                consumidores[i]= new Consumidor(i, numProductos, false, buzonConsumidores);
            }
            for(int i= 0;i<numProdCons; i++){
                productores[i].start();
            }
            for(int i=0; i<numProdCons; i++){
                consumidores[i].start();
            }
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public static void main(String[] args){
        Main m = new Main();
        m.start();
    }
    
}
