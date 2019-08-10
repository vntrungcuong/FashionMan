/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg.controllers;

import pkg.entities.Products;
import pkg.entities.Types;
import pkg.entities.TypesFacadeLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

@Named("typeController")
@SessionScoped
public class TypeController
        implements Serializable {

    @EJB
    private TypesFacadeLocal typesFacade;

    public Types getCurrentType() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        if (request.getParameter("typeID") == null) {
            return null;
        }
        return getType(request.getParameter("typeID"));
    }

    public List<Types> getTypes() {
        List<Types> list = new ArrayList<Types>();
        for (Types t : this.typesFacade.findAll()) {
            if (t.getTypeState().booleanValue()) {
                list.add(t);
            }
        }
        return list;
    }

    public Types getType(String typeID) {
        Types tempType = this.typesFacade.find(typeID);
        if (tempType != null && tempType.getTypeState().booleanValue()) {
            return tempType;
        }
        return null;
    }

    public List<Types> getTypesByCat(String catID) {
        List<Types> list = new ArrayList<Types>();
        for (Types t : getTypes()) {
            if (this.typesFacade.containCat(t, catID)) {
                list.add(t);
            }
        }
        return list;
    }

    public boolean isHasNew(String typeID, String catID) {
        Types type = getType(typeID);
        if (type == null) {
            return false;
        }
        List<Products> products = new ArrayList<Products>(type.getProductsCollection());
        for (Products p : products) {
            if (p.getProductState().booleanValue() && "New".equals(p.getFeature()) && catID.equals(p.getCategoryID().getCategoryID())) {
                return true;
            }
        }
        return false;
    }
}
