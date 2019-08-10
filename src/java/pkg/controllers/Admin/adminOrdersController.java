// Decompiled using: fernflower
// Took: 137ms

package pkg.controllers.Admin;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import pkg.entities.OrdersDetails;
import java.util.List;
import pkg.entities.Orders;
import pkg.entities.OrdersFacadeLocal;
import javax.ejb.EJB;
import pkg.entities.OrdersDetailsFacadeLocal;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named("adminOrdersController")
@SessionScoped
public class adminOrdersController implements Serializable {
    @EJB
    private OrdersDetailsFacadeLocal ordersDetailsFacade;
    @EJB
    private OrdersFacadeLocal ordersFacade;
    private Orders editedOrder;
    private Orders selectedOrder;
    
    public adminOrdersController() {
        super();
        this.editedOrder = null;
        this.selectedOrder = null;
    }
    
    public Orders getEditedOrder() {
        if (this.getRequest().getParameter("id") != null) {
            this.editedOrder = this.ordersFacade.find((Object)this.getRequest().getParameter("id"));
        }
        return this.editedOrder;
    }
    
    public Orders getSelectedOrder() {
        if (this.getRequest().getParameter("id") != null) {
            this.selectedOrder = this.ordersFacade.find((Object)this.getRequest().getParameter("id"));
        }
        return this.selectedOrder;
    }
    
    public List<OrdersDetails> getOD() {
        if (this.getSelectedOrder().getOrdersDetailsCollection() != null) {
            final String action = this.getRequest().getParameter("action");
            final String odId = this.getRequest().getParameter("odID");
            final String state = this.getRequest().getParameter("state");
            if (action != null && odId != null && state != null && action.equals("disable")) {
                final OrdersDetails disabledOD = this.ordersDetailsFacade.find((Object)Integer.parseInt(odId));
                if (disabledOD != null && disabledOD.getOdState().toString().toLowerCase().equals(state.toLowerCase())) {
                    final boolean currentState = disabledOD.getOdState();
                    disabledOD.setOdState(!currentState);
                    this.ordersDetailsFacade.edit(disabledOD);
                    final Orders editedOrder = this.ordersFacade.find((Object)disabledOD.getOrderID().getOrderID());
                    double newTotal = 0.0;
                    for (final OrdersDetails od : editedOrder.getOrdersDetailsCollection()) {
                        if (od.getOdState()) {
                            newTotal += od.getQuantity() * od.getSellingPrice();
                        }
                    }
                    editedOrder.setTotal(newTotal);
                    this.ordersFacade.edit(editedOrder);
                }
            }
            return new ArrayList<OrdersDetails>(this.getSelectedOrder().getOrdersDetailsCollection());
        }
        return null;
    }
    
    public List<Orders> getOrders() {
        this.getSession().setAttribute("currentPage", "ordersAdmin");
        final String action = this.getRequest().getParameter("action");
        final String id = this.getRequest().getParameter("id");
        final String state = this.getRequest().getParameter("state");
        if (action != null && id != null && state != null && action.equals("disable")) {
            final Orders disabledOrders = this.ordersFacade.find((Object)id);
            if (disabledOrders != null && !disabledOrders.getProcessStatus().equals("Completed") && disabledOrders.getOrderState().toString().toLowerCase().equals(state.toLowerCase())) {
                final boolean currentState = disabledOrders.getOrderState();
                disabledOrders.setOrderState(!currentState);
                this.ordersFacade.edit(disabledOrders);
            }
        }
        return (List<Orders>)this.ordersFacade.findAll();
    }
    
    private HttpSession getSession() {
        return this.getRequest().getSession();
    }
    
    private HttpServletRequest getRequest() {
        return (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }
    
    public String update() {
        this.ordersFacade.edit(this.editedOrder);
        return "ordersView";
    }
    
    public List<String> getNotifications() {
        final List<String> nots = new ArrayList<String>();
        int numOfProcessing = 0;
        int numOfShipping = 0;
        for (final Orders o : this.ordersFacade.findAll()) {
            if (o.getProcessStatus().equals("Processing")) {
                ++numOfProcessing;
            }
            if (o.getProcessStatus().equals("Shipping")) {
                ++numOfShipping;
            }
        }
        if (numOfProcessing > 0) {
            nots.add("You have " + numOfProcessing + " Processing orders");
        }
        if (numOfShipping > 0) {
            nots.add("You have " + numOfShipping + " Shipping orders");
        }
        return nots;
    }
}
