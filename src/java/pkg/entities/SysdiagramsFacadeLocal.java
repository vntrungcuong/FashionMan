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
public interface SysdiagramsFacadeLocal {

    void create(Sysdiagrams sysdiagrams);

    void edit(Sysdiagrams sysdiagrams);

    void remove(Sysdiagrams sysdiagrams);

    Sysdiagrams find(Object id);

    List<Sysdiagrams> findAll();

    List<Sysdiagrams> findRange(int[] range);

    int count();
    
}
