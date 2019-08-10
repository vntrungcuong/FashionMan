/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg.models;

/**
 *
 * @author vntru
 */
public class BrandReport {

    private String brand;

    public BrandReport(String brand, double income) {
        this.brand = brand;
        this.income = income;
    }
    private double income;

    public BrandReport() {
    }

    public double getIncome() {
        return this.income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public String getBrand() {
        return this.brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
