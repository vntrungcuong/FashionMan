/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg.entities;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author vntru
 */
@Stateless
public class TypesFacade extends AbstractFacade<Types> implements TypesFacadeLocal {

    @PersistenceContext(unitName = "FashionManPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TypesFacade() {
        super(Types.class);
    }

    @Override
    public boolean containCat(Types type, String catID) {
        for (Products p : type.getProductsCollection()) {
            if (p.getCategoryID().getCategoryID().equals(catID)) {
                return true;
            }
        }
        return false;
    }
}
