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
import java.util.List;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletResponse;
import managers.ManagerHistorico;
import modelo.Paciente;
import modelo.Produto;
import modelo.ProdutosDispensados;
import modelo.Venda;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.Visibility;
import servicos.ServicoPaciente;
import servicos.ServicoProduto;
import servicos.ServicoVenda;

/**
 *
 * @author paulo
 */
@Named
@ViewScoped
public class FiltroHistorico implements Serializable {

    @EJB
    private ServicoVenda servicoVenda;
    @EJB
    ServicoProduto servicoProduto;
    @EJB
    ServicoPaciente servicoPaciente;
    private ManagerHistorico managerHistorico = new ManagerHistorico();
    private boolean globalFilter;
    private List<Venda> vendas;
    private List<String> colunas;
    private List<String> colunasSelecionadas;
    private List<Boolean> colunasVisiveis;
    private Venda venda;
    private List<Produto> produtosVenda;
    private List<ProdutosDispensados> produtosDispensados;
    private Paciente paciente;
    private List<Integer> quantidade;
    private int selectedColunas = 5;
    private SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private Font font = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
    private boolean gerarPDFcolSelecionadas = true, tabelaDispensa = false;
    private RequestPDF request;
    private StreamedContent file;
    private Timestamp timestamp;
    private String nomeArquivo;
    private String caminhoCompleto;

    @PostConstruct
    public void inti() {
        vendas = servicoVenda.findAll();
        colunasVisiveis = new ArrayList<>();
        colunas();
        colunasVisiveis();
    }

    public void colunas() {
        if (!tabelaDispensa) {
            colunas = new ArrayList<>();
            colunas.add("Paciente");
            colunas.add("SUS");
            colunas.add("Data");
            colunas.add("Responsável");
        } else {
            colunas = new ArrayList<>();
            colunas.add("Medicamento");
            colunas.add("Lote");
            colunas.add("Quantidade");
            colunas.add("Validade");
        }
    }

    public void pesquisarVenda(Venda v) {
        managerHistorico.pesquisaVenda(servicoVenda, servicoProduto, servicoPaciente, v);
        venda = managerHistorico.getVenda();
        paciente = managerHistorico.getPaciente();
    }

    public List<Produto> substituirQuantidades(List<Produto> produtosVenda, List<Integer> quantidades) {
        if (produtosVenda.size() != quantidades.size()) {
            throw new IllegalArgumentException("As listas não têm o mesmo tamanho");
        }

        List<Produto> novaListaProdutosVenda = new ArrayList<>();

        for (int i = 0; i < produtosVenda.size(); i++) {
            Produto produto = produtosVenda.get(i);
            int novaQuantidade = quantidades.get(i);

            Produto novoProduto = new Produto(); // Crie uma nova instância de Produto
            novoProduto.setId(produto.getId());
            novoProduto.setNome(produto.getNome());
            novoProduto.setQuantidade(novaQuantidade); // Substitui a quantidade
            novoProduto.setLote(produto.getLote());
            novoProduto.setValidade(produto.getValidade());

            novaListaProdutosVenda.add(novoProduto);
        }

        return novaListaProdutosVenda;
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
        nomeArquivo = "Relatório de Histórico de Dispensas.pdf";
        caminhoCompleto = "C:\\Users\\Public\\Desktop\\relatorios\\" + nomeArquivo;

        try {
            File arquivo = new File(caminhoCompleto);
            File diretorio = arquivo.getParentFile();
            if (!diretorio.exists()) {
                if (diretorio.mkdirs()) {
                    System.out.println("Diretório criado!");
                } else {
                    System.out.println("Houve um erro ao criar diretório!");
                    return;
                }
            }

            FileOutputStream documento = new FileOutputStream(arquivo);
            PdfWriter.getInstance(doc, documento);
            doc.open();

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

            if (tabelaDispensa) {
                Paragraph p = new Paragraph("Relatório de Dispensa");
                p.setAlignment(1);
                doc.add(p);
                p = new Paragraph(" ");
                doc.add(p);
                p = new Paragraph("Paciente: " + venda.getPaciente().getNome());
                doc.add(p);
                p = new Paragraph("SUS: " + venda.getPaciente().getSus());
                doc.add(p);
                p = new Paragraph(" ");
                doc.add(p);
                for (int i = 0; i < venda.getProdutosDispensados().size(); i++) {
                    for (String coluna : colunasSelecionadas) {
                        switch (coluna) {
                            case "Medicamento":
                                cell = new PdfPCell(new Paragraph(venda.getProdutosDispensados().get(i).getProduto().getNome(), font));
                                break;
                            case "Lote":
                                cell = new PdfPCell(new Paragraph(venda.getProdutosDispensados().get(i).getLote(), font));
                                break;
                            case "Quantidade":
                                cell = new PdfPCell(new Paragraph(String.valueOf(venda.getProdutosDispensados().get(i).getQuantidade()), font));
                                break;
                            case "Validade":
                                if (venda.getProdutosDispensados().get(i).getValidade() == null) {
                                    cell = new PdfPCell(new Paragraph("", font));
                                } else {
                                    String dataFormatada = outputDateFormat.format(venda.getProdutosDispensados().get(i).getValidade());
                                    cell = new PdfPCell(new Paragraph(String.valueOf(dataFormatada), font));
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
            } else {
                Paragraph p = new Paragraph("Relatório de Histórico");
                p.setAlignment(1);
                doc.add(p);
                p = new Paragraph(" ");
                doc.add(p);
                for (int i = 0; i < vendas.size(); i++) {
                    for (String coluna : colunasSelecionadas) {
                        switch (coluna) {
                            case "Paciente":
                                cell = new PdfPCell(new Paragraph(vendas.get(i).getPaciente().getNome(), font));
                                break;
                            case "SUS":
                                cell = new PdfPCell(new Paragraph(vendas.get(i).getPaciente().getSus(), font));
                                break;
                            case "Data":
                                if (vendas.get(i).getData() == null) {
                                    cell = new PdfPCell(new Paragraph("", font));
                                } else {
                                    String dataFormatada = outputDateFormat.format(vendas.get(i).getData());
                                    cell = new PdfPCell(new Paragraph(dataFormatada, font));
                                }
                                break;
                            case "Responsável":
                                cell = new PdfPCell(new Paragraph(vendas.get(i).getResponsavel(), font));
                                break;
                            default:
                                cell = new PdfPCell(new Paragraph("", font)); // Outras colunas desconhecidas
                                break;
                        }
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell);
                    }
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
        nomeArquivo = "Relatório de Histórico de Dispensa.pdf";
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
            Paragraph p;
            PdfPTable table = new PdfPTable(colunasSelecionadas().size());
            PdfPCell cell;
            table.setTotalWidth(560); // Define a largura total da tabela em pontos
            table.setLockedWidth(true);
            table.setWidthPercentage(100); // Define a largura da tabela como 100% da largura da página
            float[] columnWidths = {30f, 20f, 20f, 30f}; // Define as larguras das colunas como uma porcentagem
            table.setWidths(columnWidths);
            for (int i = 0; i < colunas.size(); i++) {
                cell = new PdfPCell(new Paragraph(colunas.get(i), font));
                cell.setBorderWidth(1.2f);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setFixedHeight(20);
                table.addCell(cell);
            }

            if (tabelaDispensa) {
                p = new Paragraph("Relatório de Dispensa");
                p.setAlignment(1);
                doc.add(p);
                p = new Paragraph(" ");
                doc.add(p);
                p = new Paragraph("Paciente: " + venda.getPaciente().getNome());
                doc.add(p);
                p = new Paragraph("SUS: " + venda.getPaciente().getSus());
                doc.add(p);
                p = new Paragraph(" ");
                doc.add(p);
                for (int i = 0; i < venda.getProdutosDispensados().size(); i++) {
                    cell = new PdfPCell(new Paragraph(venda.getProdutosDispensados().get(i).getProduto().getNome(), font));
                    cell.setPaddingBottom(3f);
                    table.addCell(cell);
                    cell = new PdfPCell(new Paragraph(venda.getProdutosDispensados().get(i).getLote(), font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Paragraph(String.valueOf(venda.getProdutosDispensados().get(i).getQuantidade()), font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    if (venda.getProdutosDispensados().get(i).getValidade() == null) {
                        cell = new PdfPCell(new Paragraph("", font));
                        table.addCell(cell);
                    } else {
                        String dataFormatada = outputDateFormat.format(venda.getProdutosDispensados().get(i).getValidade());
                        cell = new PdfPCell(new Paragraph(dataFormatada, font));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell);
                    }
                }
            } else {
                p = new Paragraph("Relatório de Histórico");
                p.setAlignment(1);
                doc.add(p);
                p = new Paragraph(" ");
                doc.add(p);
                for (int i = 0; i < vendas.size(); i++) {
                    cell = new PdfPCell(new Paragraph(vendas.get(i).getPaciente().getNome(), font));
                    cell.setPaddingBottom(3f);
                    table.addCell(cell);
                    cell = new PdfPCell(new Paragraph(vendas.get(i).getPaciente().getSus(), font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    if (vendas.get(i).getData() == null) {
                        cell = new PdfPCell(new Paragraph("", font));
                        table.addCell(cell);
                    } else {
                        String dataFormatada = outputDateFormat.format(vendas.get(i).getData());
                        cell = new PdfPCell(new Paragraph(dataFormatada, font));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell);
                    }
                    cell = new PdfPCell(new Paragraph(vendas.get(i).getResponsavel(), font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                }
            }
            doc.add(table);
            p = new Paragraph("Responsável: " + venda.getResponsavel());
            p.setAlignment(2);
            doc.add(p);
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
        } catch (IOException ex) {
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

    public boolean isTabelaDispensa() {
        return tabelaDispensa;
    }

    public void setTabelaDispensa(boolean tabelaDispensa) {
        this.tabelaDispensa = tabelaDispensa;
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

    public ServicoVenda getServicoVenda() {
        return servicoVenda;
    }

    public void setServicoVenda(ServicoVenda servicoVenda) {
        this.servicoVenda = servicoVenda;
    }

    public List<Venda> getVendas() {
        return vendas;
    }

    public void setVendas(List<Venda> vendas) {
        this.vendas = vendas;
    }

    public ServicoProduto getServicoProduto() {
        return servicoProduto;
    }

    public void setServicoProduto(ServicoProduto servicoProduto) {
        this.servicoProduto = servicoProduto;
    }

    public ServicoPaciente getServicoPaciente() {
        return servicoPaciente;
    }

    public void setServicoPaciente(ServicoPaciente servicoPaciente) {
        this.servicoPaciente = servicoPaciente;
    }

    public ManagerHistorico getManagerHistorico() {
        return managerHistorico;
    }

    public void setManagerHistorico(ManagerHistorico managerHistorico) {
        this.managerHistorico = managerHistorico;
    }

    public List<ProdutosDispensados> getProdutosDispensados() {
        return produtosDispensados;
    }

    public void setProdutosDispensados(List<ProdutosDispensados> produtosDispensados) {
        this.produtosDispensados = produtosDispensados;
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        tabelaDispensa = !tabelaDispensa;
        colunas();
        this.venda = venda;
    }

    public List<Produto> getProdutosVenda() {
        return produtosVenda;
    }

    public void setProdutosVenda(List<Produto> produtosVenda) {
        this.produtosVenda = produtosVenda;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public List<Integer> getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(List<Integer> quantidade) {
        this.quantidade = quantidade;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public RequestPDF getRequest() {
        return request;
    }

    public void setRequest(RequestPDF request) {
        this.request = request;
    }

    public StreamedContent getFile() {
        return file;
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

    public boolean isGerarPDFcolSelecionadas() {
        return gerarPDFcolSelecionadas;
    }

    public void setGerarPDFcolSelecionadas(boolean gerarPDFcolSelecionadas) {
        this.gerarPDFcolSelecionadas = gerarPDFcolSelecionadas;
    }
}
