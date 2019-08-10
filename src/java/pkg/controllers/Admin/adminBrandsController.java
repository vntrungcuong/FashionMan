// Decompiled using: fernflower
// Took: 151ms

package pkg.controllers.Admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.CopyOption;
import java.io.File;
import java.util.Iterator;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.List;
import javax.servlet.http.Part;
import pkg.entities.Brands;
import javax.ejb.EJB;
import pkg.entities.BrandsFacadeLocal;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named("adminBrandsController")
@SessionScoped
public class adminBrandsController implements Serializable {
    @EJB
    private BrandsFacadeLocal brandsFacade;
    private Brands newBrand;
    private Brands selectedBrand;
    private Part image;
    String oldName;
    
    public adminBrandsController() {
        super();
        this.newBrand = new Brands();
        this.selectedBrand = null;
    }
    
    public String toCreate() {
        this.getSession().setAttribute("currentPage", "brandsAdmin");
        this.getSession().setAttribute("currentPageChild", "brandsCreate");
        return "brandsCreate";
    }
    
    public List<Brands> getBrands() {
        this.getSession().setAttribute("currentPage", "brandsAdmin");
        this.getSession().setAttribute("currentPageChild", "brandsView");
        final String action = this.getRequest().getParameter("action");
        final String id = this.getRequest().getParameter("id");
        final String state = this.getRequest().getParameter("state");
        if (action != null && id != null && state != null && action.equals("disable")) {
            final Brands disabledBrands = this.brandsFacade.find((Object)id);
            if (disabledBrands != null && disabledBrands.getBrandState().toString().toLowerCase().equals(state.toLowerCase())) {
                final boolean currentState = disabledBrands.getBrandState();
                disabledBrands.setBrandState(!currentState);
                this.brandsFacade.edit(disabledBrands);
            }
        }
        return (List<Brands>)this.brandsFacade.findAll();
    }
    
    public String create() {
        for (final Brands b : this.brandsFacade.findAll()) {
            if (b.getBrandName().equalsIgnoreCase(this.newBrand.getBrandName())) {
                FacesContext.getCurrentInstance().addMessage("profileForm", new FacesMessage("Brand Name existed."));
                return "brandsCreate";
            }
        }
        String brandID = "";
        final Integer numOfBrands = this.brandsFacade.count() + 1;
        switch (numOfBrands.toString().length()) {
            case 1: {
                brandID = "BR00" + numOfBrands;
                break;
            }
            case 2: {
                brandID = "BR0" + numOfBrands;
                break;
            }
            case 3: {
                brandID = "BR" + numOfBrands;
                break;
            }
        }
        String images = "images/logo.jpg";
        if (this.image != null && this.image.getSize() > 0L) {
            images = "images/Brands/" + this.copyImage(brandID, this.image);
        }
        this.newBrand.setBrandID(brandID);
        this.newBrand.setBrandImages(images);
        this.newBrand.setBrandState(true);
        this.brandsFacade.create(this.newBrand);
        this.newBrand = new Brands();
        this.image = null;
        return "brandsView";
    }
    
    public String update() {
        if (!this.oldName.equalsIgnoreCase(this.selectedBrand.getBrandName())) {
            for (final Brands b : this.brandsFacade.findAll()) {
                if (b.getBrandName().equalsIgnoreCase(this.selectedBrand.getBrandName())) {
                    FacesContext.getCurrentInstance().addMessage("profileForm", new FacesMessage("Brand Name existed."));
                    return "brandsUpdate";
                }
            }
        }
        if (this.image != null && this.image.getSize() > 0L) {
            this.selectedBrand.setBrandImages("images/Brands/" + this.copyImage(this.selectedBrand.getBrandID(), this.image));
        }
        this.brandsFacade.edit(this.selectedBrand);
        this.selectedBrand = null;
        this.image = null;
        return "brandsView";
    }
    
    private String copyImage(final String brandID, Part file) {
        final String dirPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/images/Brands").replace("build\\web", "web");
        final String fullFilename = file.getSubmittedFileName();
        final int lastIndex = fullFilename.lastIndexOf(46);
        final String extension = fullFilename.substring(lastIndex + 1, fullFilename.length());
        final String filename = brandID + "." + extension;
        try (final InputStream input = file.getInputStream()) {
            Files.copy(input, new File(dirPath + "/" + filename).toPath(), StandardCopyOption.REPLACE_EXISTING);
            file = null;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return filename;
    }
    
    private HttpSession getSession() {
        return this.getRequest().getSession();
    }
    
    private HttpServletRequest getRequest() {
        return (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }
    
    public Brands getNewBrand() {
        return this.newBrand;
    }
    
    public void setNewBrand(final Brands newBrand) {
        this.newBrand = newBrand;
    }
    
    public Brands getSelectedBrand() {
        if (this.getRequest().getParameter("id") != null) {
            this.selectedBrand = this.brandsFacade.find((Object)this.getRequest().getParameter("id"));
            this.oldName = new String(this.selectedBrand.getBrandName());
        }
        return this.selectedBrand;
    }
    
    public Part getImage() {
        return this.image;
    }
    
    public void setImage(final Part image) {
        this.image = image;
    }
}
