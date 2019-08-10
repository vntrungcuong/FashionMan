// Decompiled using: fernflower
// Took: 121ms

package pkg.controllers.Admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.List;
import pkg.entities.Types;
import javax.ejb.EJB;
import pkg.entities.TypesFacadeLocal;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named("adminTypesController")
@SessionScoped
public class adminTypesController implements Serializable {
    @EJB
    private TypesFacadeLocal typesFacade;
    private Types newType;
    private Types selectedType;
    String oldName;
    
    public adminTypesController() {
        super();
        this.newType = new Types();
        this.selectedType = null;
    }
    
    public String toCreate() {
        this.getSession().setAttribute("currentPage", "typesAdmin");
        this.getSession().setAttribute("currentPageChild", "typesCreate");
        return "typesCreate";
    }
    
    public List<Types> getTypes() {
        this.getSession().setAttribute("currentPage", "typesAdmin");
        this.getSession().setAttribute("currentPageChild", "typesView");
        final String action = this.getRequest().getParameter("action");
        final String id = this.getRequest().getParameter("id");
        final String state = this.getRequest().getParameter("state");
        if (action != null && id != null && state != null && action.equals("disable")) {
            final Types disabledTypes = this.typesFacade.find((Object)id);
            if (disabledTypes != null && disabledTypes.getTypeState().toString().toLowerCase().equals(state.toLowerCase())) {
                final boolean currentState = disabledTypes.getTypeState();
                disabledTypes.setTypeState(!currentState);
                this.typesFacade.edit(disabledTypes);
            }
        }
        return (List<Types>)this.typesFacade.findAll();
    }
    
    public String create() {
        for (final Types t : this.typesFacade.findAll()) {
            if (t.getTypeName().equalsIgnoreCase(this.newType.getTypeName())) {
                FacesContext.getCurrentInstance().addMessage("profileForm", new FacesMessage("Type Name existed."));
                return "typesCreate";
            }
        }
        String typeID = "";
        final Integer numOfTypes = this.typesFacade.count() + 1;
        switch (numOfTypes.toString().length()) {
            case 1: {
                typeID = "TY00" + numOfTypes;
                break;
            }
            case 2: {
                typeID = "TY0" + numOfTypes;
                break;
            }
            case 3: {
                typeID = "TY" + numOfTypes;
                break;
            }
        }
        this.newType.setTypeID(typeID);
        this.newType.setTypeState(true);
        this.typesFacade.create(this.newType);
        this.newType = new Types();
        return "typesView";
    }
    
    public String update() {
        if (!this.oldName.equalsIgnoreCase(this.selectedType.getTypeName())) {
            for (final Types t : this.typesFacade.findAll()) {
                if (t.getTypeName().equalsIgnoreCase(this.selectedType.getTypeName())) {
                    FacesContext.getCurrentInstance().addMessage("profileForm", new FacesMessage("Type Name existed."));
                    return "typesUpdate";
                }
            }
        }
        this.typesFacade.edit(this.selectedType);
        this.selectedType = null;
        return "typesView";
    }
    
    public Types getSelectedType() {
        if (this.getRequest().getParameter("id") != null) {
            this.selectedType = this.typesFacade.find((Object)this.getRequest().getParameter("id"));
            this.oldName = new String(this.selectedType.getTypeName());
        }
        return this.selectedType;
    }
    
    private HttpSession getSession() {
        return this.getRequest().getSession();
    }
    
    private HttpServletRequest getRequest() {
        return (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }
    
    public Types getNewType() {
        return this.newType;
    }
    
    public void setNewType(final Types newType) {
        this.newType = newType;
    }
}
