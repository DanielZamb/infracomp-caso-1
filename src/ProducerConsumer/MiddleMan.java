package ProducerConsumer;

public class MiddleMan extends Thread{
    private static final Boolean PROD = true;
    private static final Boolean CONS = false;
    private Buffer commonBuffer;
    private Buffer sBuffer;
    private Boolean bufferType;
    private int transferedAll;
    private int transfered;
    public MiddleMan(Buffer startBuffer, Buffer commonBuffer, Boolean buffType,int totalProducts){
        sBuffer = startBuffer;
        this.commonBuffer = commonBuffer;
        bufferType = buffType;
        transfered = 0;
        transferedAll = totalProducts;
    }
    public void run(){
        String prod = "";
        while (transferedAll!=transfered){ //OJO ACAAAAAAA
            synchronized (this){
                if (bufferType == PROD)
                    while (sBuffer.isEmpty() || commonBuffer.isFull()){
                       try{
                           wait();
                       }catch (Exception e){}
                    }
                else if (bufferType == CONS)
                    while (sBuffer.isFull()|| commonBuffer.isEmpty()){
                        try{
                            wait();
                        }catch (Exception e){}
                    }
            }
            if (bufferType == PROD)
            {
                synchronized (sBuffer){
                    prod = (String) sBuffer.remove();
                    System.out.println("PRODUCER_MiddleMan has received <"+prod+">");
                }
                synchronized (commonBuffer){
                    commonBuffer.insert(prod);
                    System.out.println("PRODUCER_MiddleMan has delivered <"+prod+">");
                }
                transfered++;
            }
            else if (bufferType == CONS)
            {
                synchronized (commonBuffer){
                    prod = (String) commonBuffer.remove();
                    System.out.println("CONSUMER_MiddleMan has received <"+prod+">");
                }
                synchronized (sBuffer){
                    sBuffer.insert(prod);
                    System.out.println("CONSUMER_MiddleMan has delivered <"+prod+">");
                }
                transfered++;
            }

        }
        if (bufferType == PROD)
            System.out.println("The thread managing the PRODUCER_MiddleMan has successfully terminated");
        else
            System.out.println("The thread managing the CONSUMER_MiddleMan has successfully terminated");
    }

}
