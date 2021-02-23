package ProducerConsumer;

import java.util.LinkedList;

public class Buffer<K> {
    private LinkedList<K> buffer;
    private int n;
    Object full,empty;
    public Buffer(int n){
        this.n = n;
        buffer = new LinkedList<>();
        full = new Object();
        empty = new Object();
    }

    public void insert(K k){
        boolean cont = true;
        while (cont){
            synchronized (this){
                if (buffer.size()<n){
                    buffer.addFirst(k);
                    cont = false;
                }
            }
            if (cont){
                synchronized (full){
                    try{
                        full.wait();
                    }catch (Exception e){}
                }
            }
        }
        synchronized (empty) {

            try {
                empty.notify();
            } catch (Exception e) {
            }
        }
    }
    public K remove(){
        boolean cont = true;
        K msg = null;
        while (cont){
            synchronized (this){
                if  (buffer.size() > 0){
                    msg = buffer.removeLast();
                    cont = false;
                }
            }
            if (cont){
                synchronized (empty){
                    try{
                        empty.wait();
                    }catch (Exception e){}
                }
            }
        }
        synchronized (full){
            try{
                full.notify();
            }catch (Exception e){}
        }
        return msg;
    }
}
