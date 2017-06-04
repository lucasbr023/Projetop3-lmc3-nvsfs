package com.example.lmc3.projetop3_lmc3_nvsfs;

/**
 * Created by lmc3 on 03/06/2017.
 */

public class Currency {
    public int id;
    public double quantity;
    public String destination;
    public String local;
    public double result;


    public Currency(double quantity, String local, String destination, double result){
        this.quantity = quantity;
        this.local = local;
        this.destination = destination;
        this.result = result;
    }
    public String getLocal(){
        return local;
    }
    public String getDestination(){
        return destination;
    }
    public double getQuantity(){
        return quantity;
    }
    public double getResult(){
        return quantity * result;
    }
}
