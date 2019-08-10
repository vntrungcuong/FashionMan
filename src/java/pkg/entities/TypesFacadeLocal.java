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
public interface TypesFacadeLocal {

    void create(Types paramTypes);

    void edit(Types paramTypes);

    void remove(Types paramTypes);

    Types find(Object paramObject);

    List<Types> findAll();

    List<Types> findRange(int[] paramArrayOfInt);

    int count();

    boolean containCat(Types paramTypes, String paramString);

}
