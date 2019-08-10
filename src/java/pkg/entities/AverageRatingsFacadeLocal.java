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
public interface AverageRatingsFacadeLocal {

    void create(AverageRatings paramAverageRatings);

    void edit(AverageRatings paramAverageRatings);

    void remove(AverageRatings paramAverageRatings);

    AverageRatings find(Object paramObject);

    List<AverageRatings> findAll();

    List<AverageRatings> findRange(int[] paramArrayOfInt);

    int count();

    double getAverageRating(String paramString);

}
