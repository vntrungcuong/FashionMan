/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg.controllers;

import pkg.entities.Categories;
import pkg.entities.CategoriesFacadeLocal;
import pkg.entities.Products;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named("categoryController")
@SessionScoped
public class CategoryController
        implements Serializable {

    public int getCol_width() {
        return 12 / this.categoriesFacade.count();
    }
    @EJB
    private CategoriesFacadeLocal categoriesFacade;

    public boolean isHasNew(String catID) {
        Categories cat = this.categoriesFacade.find(catID);
        List<Products> products = new ArrayList<Products>(cat.getProductsCollection());
        for (Products p : products) {
            if (p.getProductState().booleanValue() && "New".equals(p.getFeature())) {
                return true;
            }
        }
        return false;
    }

    public List<Categories> getCategories() {
        List<Categories> list = new ArrayList<Categories>();
        for (Categories c : this.categoriesFacade.findAll()) {
            if (c.getCategoryState().booleanValue()) {
                list.add(c);
            }
        }
        return list;
    }

    public String getFirstType(String catID) {
        return this.categoriesFacade.getFirstType(catID);
    }
}
