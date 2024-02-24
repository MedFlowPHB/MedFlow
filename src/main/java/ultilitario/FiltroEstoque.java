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
import java.io.PrintWriter;
import jakarta.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import modelo.Produto;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.Visibility;
import servicos.ServicoProduto;

/**
 *
 * @author paulo
 */
@Named
@ViewScoped
public class FiltroEstoque implements Serializable {

    @EJB
    private ServicoProduto servico;
    private boolean globalFilter;
    private List<Produto> produtos;
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
        produtos = servico.findAll();
        colunas = new ArrayList<>();
        colunas.add("Nome");
        colunas.add("Lote");
        colunas.add("Quantidade");
        colunas.add("Validade");
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
        if (colunasSelecionadas.size() < 4) {
            gerarPDFcolSelecionadas = false;
            return colunasSelecionadas;
        }
        gerarPDFcolSelecionadas = true;
        return colunasSelecionadas;
    }

    public void gerarPDFSelecionado() {
        Document doc = new Document();
        timestamp = new Timestamp(System.currentTimeMillis());
        nomeArquivo = "Relatório de Estoque.pdf";
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
            Paragraph p = new Paragraph("Relatório de Estoque");
            p.setAlignment(1);
            doc.add(p);
            p = new Paragraph(" ");
            doc.add(p);

            PdfPTable table = new PdfPTable(colunasSelecionadas().size());
            PdfPCell cell;
            table.setTotalWidth(560); // Define a largura total da tabela em pontos
            table.setLockedWidth(true);
            table.setWidthPercentage(100); // Define a largura da tabela como 100% da largura da página
            for (int i = 0; i < colunasSelecionadas().size(); i++) {
                cell = new PdfPCell(new Paragraph(colunasSelecionadas().get(i), font));
                cell.setBorderWidth(1.2f);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setFixedHeight(20);
                table.addCell(cell);
            }

            for (int i = 0; i < produtos.size(); i++) {
                for (String coluna : colunasSelecionadas) {
                    switch (coluna) {
                        case "Nome":
                            cell = new PdfPCell(new Paragraph(produtos.get(i).getNome(), font));
                            break;
                        case "Lote":
                            cell = new PdfPCell(new Paragraph(produtos.get(i).getLote(), font));
                            break;
                        case "Quantidade":
                            cell = new PdfPCell(new Paragraph(String.valueOf(produtos.get(i).getQuantidade()), font));
                            break;
                        case "Validade":
                            if (produtos.get(i).getValidade() == null) {
                                cell = new PdfPCell(new Paragraph("", font));
                            } else {
                                String dataFormatada = outputDateFormat.format(produtos.get(i).getValidade());
                                cell = new PdfPCell(new Paragraph(dataFormatada, font));
                            }
                            break;
                        default:
                            cell = new PdfPCell(new Paragraph("", font)); // Outras colunas desconhecidas
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
        nomeArquivo = "Relatório de Estoque.pdf";
        caminhoCompleto = "C:\\Users\\Public\\relatorios\\" + nomeArquivo;
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(caminhoCompleto));
            doc.open();

            Paragraph p = new Paragraph("Relatório de Estoque");
            p.setAlignment(1);
            doc.add(p);
            p = new Paragraph(" ");
            doc.add(p);

            PdfPTable table = new PdfPTable(colunas.size());
            table.setTotalWidth(560); // Define a largura total da tabela em pontos
            table.setLockedWidth(true);
            table.setWidthPercentage(100); // Define a largura da tabela como 100% da largura da página

            float[] columnWidths = {40f, 20f, 20f, 20f}; // Define as larguras das colunas como uma porcentagem
            table.setWidths(columnWidths);
            PdfPCell cell;
            for (int i = 0; i < colunas.size(); i++) {
                cell = new PdfPCell(new Paragraph(colunas.get(i)));
                cell.setBorderWidth(1.2f);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setFixedHeight(20);
                table.addCell(cell);
            }

            for (int i = 0; i < produtos.size(); i++) {
                cell = new PdfPCell(new Paragraph(produtos.get(i).getNome(), font));
                cell.setPaddingBottom(3f);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(produtos.get(i).getLote(), font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(String.valueOf(produtos.get(i).getQuantidade()), font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                if (produtos.get(i).getValidade() == null) {
                    cell = new PdfPCell(new Paragraph("", font));
                    table.addCell(cell);
                } else {
                    String dataFormatada = outputDateFormat.format(produtos.get(i).getValidade());
                    cell = new PdfPCell(new Paragraph(dataFormatada, font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                }
            }
            doc.add(table);
            doc.close();
            DownloadPDF();
            message("PDF GERADO", "");
        } catch (DocumentException | FileNotFoundException ex) {
            System.out.println(ex.getMessage());
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

    public void setFile(StreamedContent file) {
        this.file = file;
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

    public RequestPDF getRequest() {
        return request;
    }

    public void setRequest(RequestPDF request) {
        this.request = request;
    }

    public ServicoProduto getServico() {
        return servico;
    }

    public void setServico(ServicoProduto servico) {
        this.servico = servico;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
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
