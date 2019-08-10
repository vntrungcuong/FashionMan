/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("requireValidator")
public class RequireValidator
        implements Validator {

    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String str = value.toString().trim();
        if (str.length() == 0) {
            throw new ValidatorException(new FacesMessage("String is empty."));
        }
    }
}
