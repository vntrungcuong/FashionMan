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
public interface WishlistFacadeLocal {

    void create(Wishlist paramWishlist);

    void edit(Wishlist paramWishlist);

    void remove(Wishlist paramWishlist);

    void removeAll(String paramString);

    Wishlist find(Object paramObject);

    Wishlist find(String paramString1, String paramString2);

    List<Wishlist> findAll();

    List<Wishlist> findRange(int[] paramArrayOfInt);

    List<Wishlist> getWishlist(String paramString);

    int count();

}
