/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg.controllers;

import pkg.entities.Brands;
import pkg.entities.BrandsFacadeLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named("brandController")
@SessionScoped
public class BrandController
        implements Serializable {

    @EJB
    private BrandsFacadeLocal brandsFacade;

    public List<Brands> getBrands() {
        List<Brands> list = new ArrayList<Brands>();
        for (Brands b : this.brandsFacade.findAll()) {
            if (b.getBrandState().booleanValue()) {
                list.add(b);
            }
        }
        return list;
    }
}
