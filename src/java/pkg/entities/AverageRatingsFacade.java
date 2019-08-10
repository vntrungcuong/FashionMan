/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg.entities;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author vntru
 */
@Stateless
public class AverageRatingsFacade extends AbstractFacade<AverageRatings> implements AverageRatingsFacadeLocal {

    @PersistenceContext(unitName = "FashionManPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AverageRatingsFacade() {
        super(AverageRatings.class);
    }

    @Override
    public double getAverageRating(String productID) {
        Query q = this.em.createQuery("SELECT a.averageRating FROM AverageRatings a WHERE a.productID = :productID");
        q.setParameter("productID", productID);
        try {
            return ((Double) q.getSingleResult()).doubleValue();
        } catch (NoResultException e) {
            return 0.0D;
        }

    }
}
