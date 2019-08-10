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
public interface CategoriesFacadeLocal {

    void create(Categories paramCategories);

    void edit(Categories paramCategories);

    void remove(Categories paramCategories);

    Categories find(Object paramObject);

    List<Categories> findAll();

    List<Categories> findRange(int[] paramArrayOfInt);

    int count();

    String getFirstType(String paramString);
}
