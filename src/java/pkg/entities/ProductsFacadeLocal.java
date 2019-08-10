/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg.entities;

import java.util.List;
import javax.ejb.Local;
import lib.Result;

/**
 *
 * @author vntru
 */
@Local
public interface ProductsFacadeLocal {

    void create(Products paramProducts);

    void edit(Products paramProducts);

    void remove(Products paramProducts);

    Products find(Object paramObject);

    List<Products> findAll();

    List<Products> findRange(int[] paramArrayOfInt);

    int count();

    List<Products> getProductsForGuest(Result paramResult);

    List<Products> getProductsForUser(Result paramResult, String paramString1, String paramString2);

    List<Products> getRelatedProducts(Result paramResult, String paramString);

    List<Products> getFeaturedProducts(String paramString);

    List<Products> searchByPrice(int paramInt1, int paramInt2);

    List<Products> searchByCatType(Categories paramCategories, Types paramTypes);

    List<Products> searchByName(String paramString);

    List<String> getProductNames();

    long byCatType(String paramString1, String paramString2);

    Long getMinPrice();

    Long getMaxPrice();

}
