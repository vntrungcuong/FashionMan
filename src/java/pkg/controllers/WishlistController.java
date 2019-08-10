/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg.controllers;

import pkg.entities.Wishlist;
import pkg.entities.WishlistFacadeLocal;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

@Named("wishlistController")
@SessionScoped
public class WishlistController
        implements Serializable {

    @EJB
    private WishlistFacadeLocal wishlistFacade;

    public String getCustomerEmail() {
        return ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).getAttribute("user").toString();
    }

    public List<Wishlist> getWishlist() {
        return this.wishlistFacade.getWishlist(getCustomerEmail());
    }

    public String removeWishlist(String wishListID) {
        this.wishlistFacade.remove(this.wishlistFacade.find(Integer.valueOf(wishListID)));
        return "wishlist";
    }

    public String removeAllWishlist() {
        this.wishlistFacade.removeAll(getCustomerEmail());
        return "wishlist";
    }
}
