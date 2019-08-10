// Decompiled using: fernflower
// Took: 191ms

package pkg.controllers.Admin;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.CopyOption;
import java.io.File;
import java.util.Iterator;
import java.util.Date;
import javax.servlet.http.HttpSession;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.List;
import javax.servlet.http.Part;
import pkg.entities.Admins;
import javax.ejb.EJB;
import pkg.entities.AdminsFacadeLocal;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named("adminAccountsController")
@SessionScoped
public class adminAccountsController implements Serializable {
    @EJB
    private AdminsFacadeLocal adminsFacade;
    private Admins currentAdmin;
    private Admins editedAdmin;
    private Admins newAdmin;
    private String email;
    private String password;
    private String oldPass;
    private String newPass;
    private String confirmPass;
    private Part avatar;
    
    public adminAccountsController() {
        super();
        this.currentAdmin = null;
        this.editedAdmin = null;
        this.newAdmin = new Admins();
    }
    
    public List<Admins> getAdminAccounts() {
        this.getSession().setAttribute("currentPage", "accountsAdmin");
        this.getSession().setAttribute("currentPageChild", "");
        final String action = this.getRequest().getParameter("action");
        final String id = this.getRequest().getParameter("id");
        final String state = this.getRequest().getParameter("state");
        if (action != null && id != null && state != null && action.equals("disable")) {
            final Admins disabledAdmins = this.adminsFacade.find((Object)id);
            if (disabledAdmins != null && disabledAdmins.getAdminState().toString().toLowerCase().equals(state.toLowerCase())) {
                final boolean currentState = disabledAdmins.getAdminState();
                disabledAdmins.setAdminState(!currentState);
                this.adminsFacade.edit(disabledAdmins);
            }
        }
        return (List<Admins>)this.adminsFacade.findAll();
    }
    
    public String toCreate() {
        this.getSession().setAttribute("currentPage", "accountsAdmin");
        this.getSession().setAttribute("currentPageChild", "accountsCreate");
        return "accountsCreate";
    }
    
    public String toProfile() {
        this.getSession().setAttribute("currentPage", "profile");
        this.getSession().removeAttribute("currentPageChild");
        return "adminProfile";
    }
    
    public String checkLogin() {
        this.currentAdmin = this.adminsFacade.checkLogin(this.email.trim(), this.password);
        if (this.currentAdmin != null && this.currentAdmin.getAdminState()) {
            final HttpSession session = this.getSession();
            session.setAttribute("admin", this.email.trim());
//            session.setAttribute("role", this.currentAdmin.getAdminRole());
            session.setMaxInactiveInterval(7200);
            session.setAttribute("currentPage", "reports");
            session.setAttribute("currentPageChild", "categories");
            return "Admin/reportByCategories";
        }
        FacesContext.getCurrentInstance().addMessage("loginForm", new FacesMessage("Wrong email or password."));
        this.email = "";
        this.password = "";
        return "login";
    }
    
    public String logout() {
        this.getSession().removeAttribute("admin");
        this.currentAdmin = null;
        this.email = "";
        this.password = "";
        return "/login";
    }
    
    public String create() {
        String email = this.newAdmin.getFullName().replaceAll(" ", "") + "@mail.com";
        for (final Admins a : this.adminsFacade.findAll()) {
            if (a.getEmail().equals(email)) {
                email = a.getEmail().replaceAll("@", this.adminsFacade.count() + "@");
                break;
            }
        }
        this.newAdmin.setEmail(email);
        this.newAdmin.setPassword("tShopAdmin");
//        this.newAdmin.setAvatar("images/Avatars/default-avatar.png");
        this.newAdmin.setCreatedDate(new Date());
        this.newAdmin.setAdminState(true);
        this.adminsFacade.create(this.newAdmin);
        this.newAdmin = new Admins();
        return "accountsView";
    }
    
    public String update() {
        this.adminsFacade.edit(this.editedAdmin);
        this.editedAdmin = null;
        return "accountsView";
    }
    

    
    public String changePassword() {
        if (this.currentAdmin.getPassword().equals(this.oldPass) && this.confirmPass.equals(this.newPass)) {
            this.currentAdmin.setPassword(this.newPass);
            this.adminsFacade.edit(this.currentAdmin);
            this.oldPass = "";
            this.newPass = "";
            this.confirmPass = "";
            this.setErrorMessage("changePasswordForm", "Password changed successfully.");
            return "adminProfile";
        }
        if (!this.currentAdmin.getPassword().equals(this.oldPass)) {
            this.setErrorMessage("changePasswordForm", "Old password is wrong.");
            return "adminProfile";
        }
        this.setErrorMessage("changePasswordForm", "Confirm Password does not match with New Password.");
        return "adminProfile";
    }
    
    private void setErrorMessage(final String uiID, final String message) {
        if (uiID != null && message != null) {
            FacesContext.getCurrentInstance().addMessage(uiID, new FacesMessage(message));
        }
    }
    
    private String copyImage(final String email, Part file) {
        final String dirPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/Admin/images/Avatars").replace("build\\web", "web");
        final String fullFilename = file.getSubmittedFileName();
        final int lastIndex = fullFilename.lastIndexOf(46);
        final String extension = fullFilename.substring(lastIndex + 1, fullFilename.length());
        final String filename = email + "." + extension;
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
    
    public Admins getEditedAdmins() {
        if (this.getRequest().getParameter("id") != null) {
            this.editedAdmin = this.adminsFacade.find((Object)this.getRequest().getParameter("id"));
        }
        return this.editedAdmin;
    }
    
    public Admins getCurrentAdmin() {
        return this.currentAdmin;
    }
    
    public Admins getNewAdmin() {
        return this.newAdmin;
    }
    
    public void setNewAdmin(final Admins admin) {
        this.newAdmin = admin;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(final String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(final String password) {
        this.password = password;
    }
    
    public Part getAvatar() {
        return this.avatar;
    }
    
    public void setAvatar(final Part avatar) {
        this.avatar = avatar;
    }
    
    public String getOldPass() {
        return this.oldPass;
    }
    
    public void setOldPass(final String oldPass) {
        this.oldPass = oldPass;
    }
    
    public String getNewPass() {
        return this.newPass;
    }
    
    public void setNewPass(final String newPass) {
        this.newPass = newPass;
    }
    
    public String getConfirmPass() {
        return this.confirmPass;
    }
    
    public void setConfirmPass(final String confirmPass) {
        this.confirmPass = confirmPass;
    }
}
