import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ComponentSystemEvent;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.IOException;

@Named
@RequestScoped
public class LoginBean {
    @Inject
    private SavedUserBean sub;

    public void before(ComponentSystemEvent event) throws IOException {
        if (sub.getCurrentUser() == null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/Users/register.xhtml");
        } else {

        }
    }
}