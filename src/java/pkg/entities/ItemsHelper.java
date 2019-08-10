/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg.entities;

/**
 *
 * @author vntru
 */
import java.util.ArrayList;
import java.util.List;

public class ItemsHelper {

    public static List<AverageRatings> findItems(List<AverageRatings> items, List<String> movieIds) {
        List<AverageRatings> list = new ArrayList<AverageRatings>();
        for (String movieId : movieIds) {
            list.add(findItem(items, movieId));
        }
        return list;
    }

    public static AverageRatings findItem(List<AverageRatings> items, String movieId) {
        for (AverageRatings m : items) {
            if (m.getProductID().equals(movieId)) {
                return m;
            }
        }
        return null;
    }
}
