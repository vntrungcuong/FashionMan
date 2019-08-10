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
public interface AdminsFacadeLocal {

    void create(final Admins p0);
    
    void edit(final Admins p0);
    
    void remove(final Admins p0);
    
    Admins find(final Object p0);
    
    List<Admins> findAll();
    
    List<Admins> findRange(final int[] p0);
    
    int count();
    
    Admins checkLogin(final String p0, final String p1);
    
}
