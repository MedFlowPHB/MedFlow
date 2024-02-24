/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ultilitario;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletResponse;
import modelo.Paciente;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.Visibility;
import servicos.ServicoPaciente;

/**
 *
 * @author paulo
 */
@Named
@ViewScoped
public class FiltroPaciente implements Serializable {

    @EJB
    private ServicoPaciente servico;
    private boolean globalFilter;
    private List<Paciente> pacientes;
    private List<String> colunas;
    private List<String> colunasSelecionadas;
    private List<Boolean> colunasVisiveis;
    private int selectedColunas = 5;
    private SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private Font font = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
    private boolean gerarPDFcolSelecionadas = true;
    private RequestPDF request;
    private StreamedContent file;
    private Timestamp timestamp;
    private String nomeArquivo;
    private String caminhoCompleto;

    @PostConstruct
    public void inti() {
        pacientes = servico.findAll();
        colunas = new ArrayList<>();
        colunas.add("Nome");
        colunas.add("SUS");
        colunas.add("Area");
        colunas.add("Idade");
        colunas.add("QMF");
        colunas.add("Sexo");
        colunas.add("Patologia");
        colunas.add("Telefone");
        colunasVisiveis = new ArrayList<>();
        colunasVisiveis();
    }

    public void FileDownloadView() {
        try {
            file = DefaultStreamedContent.builder()
                    .name(nomeArquivo)
                    .contentType("application/pdf")
                    .stream(() -> FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getResourceAsStream(caminhoCompleto))
                    .build();
        } catch (Exception ex) {
            message("Error", ex.getMessage());
        }
    }

    public StreamedContent getFile() {
        return file;
    }

    public List<String> colunasSelecionadas() {
        colunasSelecionadas = new ArrayList<>();
        for (int i = 0; i < colunasVisiveis.size(); i++) {
            if (colunasVisiveis.get(i)) {
                colunasSelecionadas.add(colunas.get(i));
            }
        }
        if (colunasSelecionadas.size() < 8) {
            gerarPDFcolSelecionadas = false;
            return colunasSelecionadas;
        }
        gerarPDFcolSelecionadas = true;
        return colunasSelecionadas;
    }

    public int calcularIdade(Date dataNascimento) {
        Calendar hoje = Calendar.getInstance();
        Calendar nascimento = Calendar.getInstance();
        nascimento.setTime(dataNascimento);

        int idade = hoje.get(Calendar.YEAR) - nascimento.get(Calendar.YEAR);
        if (hoje.get(Calendar.MONTH) < nascimento.get(Calendar.MONTH)
                || (hoje.get(Calendar.MONTH) == nascimento.get(Calendar.MONTH) && hoje.get(Calendar.DAY_OF_MONTH) < nascimento.get(Calendar.DAY_OF_MONTH))) {
            idade--;
        }
        return idade;
    }

    public void gerarPDFSelecionado() {
        Document doc = new Document();
        timestamp = new Timestamp(System.currentTimeMillis());
        nomeArquivo = "Relatório de Pacientes.pdf";
        caminhoCompleto = "C:\\Users\\Public\\Desktop\\relatorios\\" + nomeArquivo;

        try {
            File arquivo = new File(caminhoCompleto);
            File diretorio = arquivo.getParentFile();
            if (!diretorio.exists()) {
                if (diretorio.mkdirs()) {
                    System.out.println("Diretório criado!");
                } else {
                    System.out.println("Houve um erro!");
                    return;
                }
            }

            FileOutputStream documento = new FileOutputStream(arquivo);
            PdfWriter.getInstance(doc, documento);
            doc.open();

            Paragraph p = new Paragraph("Relatório de Pacientes");
            p.setAlignment(1);
            doc.add(p);
            p = new Paragraph(" ");
            doc.add(p);

            p = new Paragraph("OBS: QMF lê-se como Quantidade de Membros na Família.", font);
            doc.add(p);
            p = new Paragraph(" ");
            doc.add(p);

            PdfPTable table = new PdfPTable(colunasSelecionadas().size());
            PdfPCell cell;
            table.setTotalWidth(560); // Define a largura total da tabela em pontos
            table.setLockedWidth(true);
            table.setWidthPercentage(100); // Define a largura da tabela como 100% da largura da página
            for (int i = 0; i < colunasSelecionadas().size(); i++) {
                cell = new PdfPCell(new Paragraph(colunasSelecionadas().get(i)));
                cell.setBorderWidth(1.2f);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setFixedHeight(20);
                table.addCell(cell);
            }

            for (int i = 0; i < pacientes.size(); i++) {
                for (String coluna : colunasSelecionadas) {
                    switch (coluna) {
                        case "Nome":
                            cell = new PdfPCell(new Paragraph(pacientes.get(i).getNome(), font));
                            break;
                        case "SUS":
                            cell = new PdfPCell(new Paragraph(pacientes.get(i).getSus(), font));
                            break;
                        case "Area":
                            cell = new PdfPCell(new Paragraph(String.valueOf(pacientes.get(i).getArea()), font));
                            break;
                        case "Idade":
                            if (pacientes.get(i).getIdade() == null) {
                                cell = new PdfPCell(new Paragraph(""));
                            } else {
                                cell = new PdfPCell(new Paragraph(String.valueOf(calcularIdade(pacientes.get(i).getIdade())), font));
                            }
                            break;
                        case "QMF":
                            cell = new PdfPCell(new Paragraph(String.valueOf(pacientes.get(i).getQtdmembros()), font));
                            break;
                        case "Sexo":
                            cell = new PdfPCell(new Paragraph(String.valueOf(pacientes.get(i).getSexo()), font));
                            break;
                        case "Patologia":
                            cell = new PdfPCell(new Paragraph(String.valueOf(pacientes.get(i).getPatologia()), font));
                            break;
                        case "Telefone":
                            if (pacientes.get(i).getTelefone() == null) {
                                cell = new PdfPCell(new Paragraph(""));
                            } else {
                                cell = new PdfPCell(new Paragraph(String.valueOf(pacientes.get(i).getTelefone()), font));
                            }
                            break;
                        default:
                            cell = new PdfPCell(new Paragraph("")); // Outras colunas desconhecidas
                            break;
                    }
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                }
            }
            doc.add(table);
            doc.close();
            DownloadPDF();
            message("PDF GERADO", "");
        } catch (DocumentException | FileNotFoundException ex) {
            System.out.println(ex);
        }
    }

    public void gerarPDF() throws IOException {
        Document doc = new Document();
        timestamp = new Timestamp(System.currentTimeMillis());
        nomeArquivo = "Relatório de Pacientes.pdf";
        caminhoCompleto = "C:\\Users\\Public\\Desktop\\relatorios\\" + nomeArquivo;

        try {
            File arquivo = new File(caminhoCompleto);
            File diretorio = arquivo.getParentFile();
            if (!diretorio.exists()) {
                if (diretorio.mkdirs()) {
                    System.out.println("Diretório criado!");
                } else {
                    System.out.println("Houve um erro!");
                    return;
                }
            }

            FileOutputStream documento = new FileOutputStream(arquivo);
            PdfWriter.getInstance(doc, documento);
            doc.open();

            Paragraph p = new Paragraph("Relatório de Pacientes");
            p.setAlignment(1);
            doc.add(p);
            p = new Paragraph(" ");
            doc.add(p);

            p = new Paragraph("OBS: QMF lê-se como Quantidade de Membros na Família.", font);
            doc.add(p);
            p = new Paragraph(" ");
            doc.add(p);

            PdfPTable table = new PdfPTable(colunas.size());
            table.setTotalWidth(560); // Define a largura total da tabela em pontos
            table.setLockedWidth(true);
            table.setWidthPercentage(100); // Define a largura da tabela como 100% da largura da página

            float[] columnWidths = {30f, 15f, 10f, 10f, 5f, 10f, 10f, 10f}; // Define as larguras das colunas como uma porcentagem
            table.setWidths(columnWidths);
            PdfPCell cell;
            for (int i = 0; i < colunas.size(); i++) {
                if (colunas.get(i).equals("Membros")) {
                    colunas.set(i, "QMF");
                }
                cell = new PdfPCell(new Paragraph(colunas.get(i), font));
                cell.setBorderWidth(1.2f);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setFixedHeight(20);
                table.addCell(cell);
            }

            for (int i = 0; i < pacientes.size(); i++) {
                cell = new PdfPCell(new Paragraph(pacientes.get(i).getNome(), font));
                cell.setPaddingBottom(3f);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(String.valueOf(pacientes.get(i).getSus()), font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(pacientes.get(i).getArea(), font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                if (pacientes.get(i).getIdade() == null) {
                    cell = new PdfPCell(new Paragraph(""));
                    table.addCell(cell);
                } else {
                    String dataFormatada = outputDateFormat.format(pacientes.get(i).getIdade());
                    cell = new PdfPCell(new Paragraph(dataFormatada, font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                }
                cell = new PdfPCell(new Paragraph(String.valueOf(pacientes.get(i).getQtdmembros()), font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(pacientes.get(i).getSexo(), font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(String.valueOf(pacientes.get(i).getPatologia()), font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(pacientes.get(i).getTelefone(), font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }
            doc.add(table);
            doc.close();
            DownloadPDF();
            message("PDF GERADO", "");
        } catch (DocumentException | FileNotFoundException ex) {
            System.out.println(ex);
        }
    }

    public void DownloadPDF() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

        try {
            File file = new File(caminhoCompleto);
            if (file.exists()) {
                FileInputStream fileInputStream = new FileInputStream(file);

                // Configurar os cabeçalhos da resposta HTTP
                response.reset();
                response.setContentType("application/pdf");
                response.setContentLengthLong(file.length());
                String encodedFilename = URLEncoder.encode(nomeArquivo, "UTF-8");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFilename + "\"");

                // Configurar o conjunto de caracteres para UTF-8
                response.setCharacterEncoding("UTF-8");

                // Obter o fluxo de saída da resposta HTTP
                OutputStream outputStream = response.getOutputStream();

                // Ler o arquivo e escrever na resposta
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                // Fechar os streams
                fileInputStream.close();
                outputStream.close();

                facesContext.responseComplete();
            } else {
                message("Arquivo não existe", "");
            }
        } catch (Exception ex) {
            message("Erro", ex.getMessage());
        }
    }

    public void message(String titulo, String detalhes) {
        FacesMessage msg = new FacesMessage(titulo, detalhes);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void colunasVisiveis() {
        for (int i = 0; i < colunas.size(); i++) {
            colunasVisiveis.add(i, true);
        }
    }

    public RequestPDF getRequest() {
        return request;
    }

    public void setRequest(RequestPDF request) {
        this.request = request;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
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

    public void onToggle(ToggleEvent e) {
        colunasVisiveis.set((Integer) e.getData(), e.getVisibility() == Visibility.VISIBLE);
        colunasSelecionadas();
    }

    public List<Boolean> getColunasVisiveis() {
        return colunasVisiveis;
    }

    public void setColunasVisiveis(List<Boolean> colunasVisiveis) {
        this.colunasVisiveis = colunasVisiveis;
    }

    public List<String> getColunas() {
        return colunas;
    }

    public void setColunas(List<String> colunas) {
        this.colunas = colunas;
    }

    public int getSelectedColunas() {
        return selectedColunas;
    }

    public void setSelectedColunas(int selectedColunas) {
        this.selectedColunas = selectedColunas;
    }

    public boolean isGlobalFilter() {
        return globalFilter;
    }

    public void setGlobalFilter(boolean globalFilter) {
        this.globalFilter = globalFilter;
    }

    public List<String> getColunasSelecionadas() {
        return colunasSelecionadas;
    }

    public void setColunasSelecionadas(List<String> colunasSelecionadas) {
        this.colunasSelecionadas = colunasSelecionadas;
    }

    public SimpleDateFormat getOutputDateFormat() {
        return outputDateFormat;
    }

    public void setOutputDateFormat(SimpleDateFormat outputDateFormat) {
        this.outputDateFormat = outputDateFormat;
    }

    public ServicoPaciente getServico() {
        return servico;
    }

    public void setServico(ServicoPaciente servico) {
        this.servico = servico;
    }

    public List<Paciente> getPacientes() {
        return pacientes;
    }

    public void setPacientes(List<Paciente> pacientes) {
        this.pacientes = pacientes;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public boolean isGerarPDFcolSelecionadas() {
        return gerarPDFcolSelecionadas;
    }

    public void setGerarPDFcolSelecionadas(boolean gerarPDFcolSelecionadas) {
        this.gerarPDFcolSelecionadas = gerarPDFcolSelecionadas;
    }

}
