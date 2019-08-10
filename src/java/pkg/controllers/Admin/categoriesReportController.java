// Decompiled using: fernflower
// Took: 36ms

package pkg.controllers.Admin;

import java.util.Iterator;
import pkg.entities.OrdersDetails;
import pkg.entities.Products;
import pkg.entities.Categories;
import java.util.ArrayList;
import pkg.models.CategoryReport;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ejb.EJB;
import pkg.entities.CategoriesFacadeLocal;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named("categoriesReportController")
@SessionScoped
public class categoriesReportController implements Serializable {
    @EJB
    private CategoriesFacadeLocal categoriesFacade;
    private double total;
    
    public categoriesReportController() {
        super();
    }
    
    public double getTotal() {
        return this.total;
    }
    
    private HttpSession getSession() {
        return ((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getSession();
    }
    
    public String toCategories() {
        final HttpSession session = this.getSession();
        session.setAttribute("currentPage", "reports");
        session.setAttribute("currentPageChild", "categories");
        return "reportByCategories";
    }
    
    public List<CategoryReport> getData() {
        final List<CategoryReport> data = new ArrayList<CategoryReport>();
        this.total = 0.0;
        for (final Categories c : this.categoriesFacade.findAll()) {
            final String category = c.getCategoryName();
            double income = 0.0;
            for (final Products p : c.getProductsCollection()) {
                for (final OrdersDetails od : p.getOrdersDetailsCollection()) {
                    income += od.getSellingPrice() * od.getQuantity();
                }
            }
            data.add(new CategoryReport(category, income));
            this.total += income;
        }
        for (final CategoryReport cr : data) {
            cr.setProportion(cr.getIncome() / this.total);
        }
        return data;
    }
    
    public int numOfCats() {
        return this.categoriesFacade.count();
    }
}
