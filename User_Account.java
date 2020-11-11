package com.company;

//User_Account as a class where all the information about the user's bank details is stored
public class User_Account {

    //name, account number, pin number, balance are declared private to enhance the security
    int id;
    final String name;
    final int acc_no;
    private int pin;
    private long bal;
    private long phone_no;
    String IFSC;

    //constructor for initializing the name, account number, pin and balance
    User_Account(int id, String name, int acc_no, int pin, long bal, long phone_no, String IFSC){
        this.id = id;
        this.name = name;
        this.acc_no = acc_no;
        this.pin = pin;
        this.bal = bal;
        this.phone_no = phone_no;
        this.IFSC = IFSC;
    }

    public int getId(){
        return id;
    }

    //method to get name of the customer
    public String getName(){
        return name;
    }

    public int getPin(){
        return pin;
    }

    //method to get the account number of the customer
    public int getAcc_no(){
        return acc_no;
    }

    //method to get the balance of the customer
    public long getBal(){
        return bal;
    }

    //returns phone number
    public long getPhone_no(){
        return phone_no;
    }

    //returns ifsc code
    public String getIFSC(){
        return IFSC;
    }

    //method to update the balance of the customer after depositing the amount
    public void setBal(long amount){
        bal += amount;
    }

    //sets the id
    public void setId(int id){
        this.id = id;
    }

    //sets the pin
    public void setPin(int pin){
        this.pin = pin;
    }

    //sets new phone number
    public void setPhone_no(long ph){
        phone_no = ph;
    }

    //method to update the balance after withdrawing the amount
    public boolean withdraw(long amount){
        if(amount <= bal){
            bal -= amount;
            return true;
        }
        else{
            return false;
        }
    }
}
