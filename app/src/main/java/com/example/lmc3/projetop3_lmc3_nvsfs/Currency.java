package com.example.lmc3.projetop3_lmc3_nvsfs;

/**
 * Created by lmc3 on 03/06/2017.
 */

public class Currency {
    public int id;
    public double quantity;
    public String destination;
    public String local;


    public Currency(double quantity, String local, String destination){
        this.quantity = quantity;
        this.local = local;
        this.destination = destination;
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
}
