// Decompiled using: fernflower
// Took: 50ms

package pkg.controllers.Admin;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import pkg.entities.Customers;
import java.util.List;
import javax.ejb.EJB;
import pkg.entities.CustomersFacadeLocal;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named("adminCustomerController")
@SessionScoped
public class adminCustomerController implements Serializable {
    @EJB
    private CustomersFacadeLocal customersFacade;
    
    public adminCustomerController() {
        super();
    }
    
    public List<Customers> getCustomers() {
        this.getSession().setAttribute("currentPage", "customersAdmin");
        this.getSession().setAttribute("currentPageChild", "accountsView");
        final String action = this.getRequest().getParameter("action");
        final String id = this.getRequest().getParameter("id");
        final String state = this.getRequest().getParameter("state");
        if (action != null && id != null && state != null && action.equals("disable")) {
            final Customers disabledCustomers = this.customersFacade.find((Object)id);
            if (disabledCustomers != null && disabledCustomers.getCustomerState().toString().toLowerCase().equals(state.toLowerCase())) {
                final boolean currentState = disabledCustomers.getCustomerState();
                disabledCustomers.setCustomerState(!currentState);
                this.customersFacade.edit(disabledCustomers);
            }
        }
        return (List<Customers>)this.customersFacade.findAll();
    }
    
    private HttpSession getSession() {
        return this.getRequest().getSession();
    }
    
    private HttpServletRequest getRequest() {
        return (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }
}
