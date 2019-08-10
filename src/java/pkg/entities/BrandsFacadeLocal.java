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
public interface BrandsFacadeLocal {

    void create(Brands paramBrands);

    void edit(Brands paramBrands);

    void remove(Brands paramBrands);

    Brands find(Object paramObject);

    List<Brands> findAll();

    List<Brands> findRange(int[] paramArrayOfInt);

    int count();

}
