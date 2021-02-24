package ProducerConsumer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class main extends Thread{
    Producer[] producers;
    Consumer[] consumers;
    MiddleMan producerMM;
    MiddleMan consumerMM;
    Buffer<String> pBuffer;
    Buffer<String> commonBuffer;
    Buffer<String> cBuffer;
    public void run(){
        try{
            InputStream input = new FileInputStream("data/params.properties");
            Properties prop = new Properties();

            prop.load(input);

            int numProdCons = Integer.parseInt(prop.getProperty("concurrencia.numProdCons"));
            int numProductos = Integer.parseInt(prop.getProperty("concurrencia.numProductos"));
            int buzonesProd = Integer.parseInt(prop.getProperty("concurrencia.buzonesProd"));
            int buzonesCons = Integer.parseInt(prop.getProperty("concurrencia.buzonesCons"));
            int totalProducts = numProdCons*numProductos;

            commonBuffer = new Buffer<>(1);
            pBuffer = new Buffer<>(buzonesProd);
            cBuffer = new Buffer<>(buzonesCons);
            producerMM = new MiddleMan(pBuffer,commonBuffer,true,totalProducts);
            consumerMM = new MiddleMan(cBuffer,commonBuffer,false,totalProducts);
            commonBuffer.setMiddleMan(consumerMM);
            pBuffer.setMiddleMan(producerMM); //este man solo puede despertar a pMM
            cBuffer.setMiddleMan(producerMM);



            producers = new Producer[numProdCons];
            consumers = new Consumer[numProdCons];

            producerMM.start();
            consumerMM.start();

            for(int i = 0; i<numProdCons/2; i++){
                producers[i] = new Producer (i,numProductos,pBuffer,"A");
                consumers[i]= new Consumer (i, numProductos, cBuffer, "A");
            }
            for(int i = numProdCons/2; i<numProdCons; i++){
                producers[i] = new Producer (i,numProductos,pBuffer,"B");
                consumers[i]= new Consumer (i, numProductos, cBuffer, "B");
            }
            for(int i= 0;i<numProdCons; i++){
                producers[i].start();
            }
            for(int i=0; i<numProdCons; i++){
                consumers[i].start();
            }


            /*for (int i=0; i<numProdCons;i++){
                producers[i].join();
                consumers[i].join();
            }
            producerMM.join();
            consumerMM.join();*/
        } catch (IOException e){//| InterruptedException ex
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        main m = new main();
        m.start();
    }
}
