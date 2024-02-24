package ultilitario;


import java.io.Serializable;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named
@ViewScoped
public class Message implements Serializable {

    public void addMessage(String mensagem) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(mensagem));
    }
    
    public void mensagemErro(String titulo, String detalhe) {
        FacesMessage msg = new FacesMessage(titulo, detalhe);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}