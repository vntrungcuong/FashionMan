// Decompiled using: fernflower
// Took: 12ms

package pkg.models;

public class CustomerReport {
    private String email;
    private String name;
    private double income;
    
    public CustomerReport() {
        super();
    }
    
    public CustomerReport(final String email, final String name, final double income) {
        super();
        this.email = email;
        this.name = name;
        this.income = income;
    }
    
    public CustomerReport(final CustomerReport newCust) {
        super();
        this.email = new String(newCust.getEmail());
        this.name = new String(newCust.getName());
        this.income = newCust.getIncome();
    }
    
    public double getIncome() {
        return this.income;
    }
    
    public void setIncome(final double income) {
        this.income = income;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(final String email) {
        this.email = email;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
}
