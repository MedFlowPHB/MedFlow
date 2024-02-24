package ultilitario;

import java.io.FileNotFoundException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@Named
@RequestScoped
public class RequestPDF {

    private StreamedContent file;
    private String nomeArquivo;
    private String caminhoCompleto;

    public RequestPDF() {

    }

    public RequestPDF(String nomeArquivo, String caminhoCompleto) {
        this.nomeArquivo = nomeArquivo;
        this.caminhoCompleto = caminhoCompleto;
    }

    public void FileDownloadView() {
        file = DefaultStreamedContent.builder()
                .name(nomeArquivo)
                .contentType("application/pdf")
                .stream(() -> FacesContext.getCurrentInstance()
                .getExternalContext()
                .getResourceAsStream(caminhoCompleto))
                .build();
    }

    public StreamedContent getFile() throws FileNotFoundException {
        return file;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public String getCaminhoCompleto() {
        return caminhoCompleto;
    }

    public void setCaminhoCompleto(String caminhoCompleto) {
        this.caminhoCompleto = caminhoCompleto;
    }

}
