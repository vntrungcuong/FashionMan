/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg.entities;

import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author vntru
 */
@Local
public interface OrdersDetailsFacadeLocal {

    void create(OrdersDetails paramOrdersDetails);

    void edit(OrdersDetails paramOrdersDetails);

    void remove(OrdersDetails paramOrdersDetails);

    OrdersDetails find(Object paramObject);

    List<OrdersDetails> findAll();

    List<OrdersDetails> findRange(int[] paramArrayOfInt);

    List<OrdersDetails> getOrdersDetails(String paramString);

    int count();

}
