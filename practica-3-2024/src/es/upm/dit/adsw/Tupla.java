package es.upm.dit.adsw;

public class Tupla<T>{
    public T primero;
    public T segundo;
    public Tupla(T primero, T segundo){
        this.primero = primero;
        this.segundo = segundo;
    }

    @Override
    public int hashCode(){
        return (primero.toString()+segundo.toString()).hashCode();
    }
}
