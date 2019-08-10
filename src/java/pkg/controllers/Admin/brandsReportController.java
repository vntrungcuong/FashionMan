// Decompiled using: fernflower
// Took: 52ms

package pkg.controllers.Admin;

import java.util.Iterator;
import pkg.entities.OrdersDetails;
import pkg.entities.Products;
import pkg.entities.Brands;
import java.util.ArrayList;
import pkg.models.BrandReport;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ejb.EJB;
import pkg.entities.BrandsFacadeLocal;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named("brandsReportController")
@SessionScoped
public class brandsReportController implements Serializable {
    @EJB
    private BrandsFacadeLocal brandsFacade;
    private double total;
    
    public brandsReportController() {
        super();
    }
    
    public double getTotal() {
        return this.total;
    }
    
    private HttpSession getSession() {
        return ((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getSession();
    }
    
    public String toBrands() {
        final HttpSession session = this.getSession();
        session.setAttribute("currentPage", "reports");
        session.setAttribute("currentPageChild", "brands");
        return "reportByBrands";
    }
    
    public List<BrandReport> getData() {
        final List<BrandReport> data = new ArrayList<BrandReport>();
        this.total = 0.0;
        for (final Brands b : this.brandsFacade.findAll()) {
            final String brand = b.getBrandName();
            double income = 0.0;
            for (final Products p : b.getProductsCollection()) {
                for (final OrdersDetails od : p.getOrdersDetailsCollection()) {
                    income += od.getSellingPrice() * od.getQuantity();
                }
            }
            data.add(new BrandReport(brand, income));
            this.total += income;
        }
        return data;
    }
    
    public int numOfBrands() {
        return this.brandsFacade.count();
    }
}
