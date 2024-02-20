package ultilitario;


import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

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