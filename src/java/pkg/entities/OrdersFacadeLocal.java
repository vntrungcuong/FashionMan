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
public interface OrdersFacadeLocal {

    void create(Orders paramOrders);

    void edit(Orders paramOrders);

    void remove(Orders paramOrders);

    Orders find(Object paramObject);

    List<Orders> findAll();

    List<Orders> findRange(int[] paramArrayOfInt);

    List<Orders> getOrdersByUser(String paramString);

    List<Orders> getUncompletedOrdersByUser(String paramString);

    int count();

    String getNewID();

}
