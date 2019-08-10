// Decompiled using: fernflower
// Took: 59ms

package pkg.controllers.Admin;

import java.util.Iterator;
import pkg.entities.OrdersDetails;
import pkg.entities.Orders;
import pkg.entities.Customers;
import java.util.ArrayList;
import pkg.models.CustomerReport;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ejb.EJB;
import pkg.entities.CustomersFacadeLocal;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named("customersReportController")
@SessionScoped
public class customersReportController implements Serializable {
    @EJB
    private CustomersFacadeLocal customersFacade;
    private double total;
    
    public customersReportController() {
        super();
    }
    
    public double getTotal() {
        return this.total;
    }
    
    private HttpSession getSession() {
        return ((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getSession();
    }
    
    public String toCustomers() {
        final HttpSession session = this.getSession();
        session.setAttribute("currentPage", "reports");
        session.setAttribute("currentPageChild", "customers");
        return "reportByCustomers";
    }
    
    public List<CustomerReport> getData() {
        final List<CustomerReport> data = new ArrayList<CustomerReport>();
        this.total = 0.0;
        for (final Customers c : this.customersFacade.findAll()) {
            final String email = c.getEmail();
            final String name = c.getFirstName();
            double income = 0.0;
            for (final Orders o : c.getOrdersCollection()) {
                for (final OrdersDetails od : o.getOrdersDetailsCollection()) {
                    income += od.getSellingPrice() * od.getQuantity();
                }
            }
            if (income > 0.0) {
                data.add(new CustomerReport(email, name, income));
            }
        }
        for (int i = 0; i < data.size(); ++i) {
            for (int j = 0; j < data.size(); ++j) {
                if (data.get(i).getIncome() > data.get(j).getIncome()) {
                    final CustomerReport temp = new CustomerReport((CustomerReport)data.get(i));
                    data.set(i, new CustomerReport((CustomerReport)data.get(j)));
                    data.set(j, new CustomerReport(temp));
                }
            }
        }
        final int endIndex = (data.size() >= 15) ? 16 : data.size();
        for (final CustomerReport cr : data.subList(1, endIndex)) {
            this.total += cr.getIncome();
        }
        return data.subList(1, endIndex);
    }
    
    public int numOfCusts() {
        return this.getData().size();
    }
}
