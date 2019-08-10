/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pkg.entities;

/**
 *
 * @author vntru
 */
import java.util.List;
public class CustomersHelper {
    
  public static Customers findCustomer(List<Customers> customers, String customerId) {
    for (Customers c : customers) {
      if (c.getEmail().equals(customerId))
        return c; 
    }  return null;
  }
}
