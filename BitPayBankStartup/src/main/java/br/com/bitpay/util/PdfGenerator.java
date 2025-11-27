package br.com.bitpay.util;

import java.io.OutputStream;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import br.com.bitpay.model.Movimentacao;
import br.com.bitpay.model.Enums.TipoMovimento;
import br.com.bitpay.model.Conta;
//import br.com.bitpay.model.Cliente;

public class PdfGenerator {
    
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final NumberFormat NF = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));
    private static final Font FONT_HEADER = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
    private static final Font FONT_NORMAL = FontFactory.getFont(FontFactory.HELVETICA, 10);
    private static final Font FONT_TITLE = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);

    public static void gerarExtratoPdf(OutputStream outputStream, Conta conta, List<Movimentacao> extrato, LocalDate dataInicial, LocalDate dataFinal) throws Exception {
        
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();
        
//        Cliente cliente = conta.getCliente(); // Obtém o objeto Cliente
//        String nomeCliente = (cliente != null && cliente.getNome() != null) ? cliente.getNome() : "Cliente Não Identificado";

        // 1. TÍTULO E CABEÇALHO
        document.add(new Paragraph("Extrato Bancário BitPay", FONT_TITLE));
//        document.add(new Paragraph("Titular: " + nomeCliente, FONT_NORMAL)); 
        document.add(new Paragraph("Conta: " + conta.getNumeroConta(), FONT_NORMAL));
        document.add(new Paragraph("Período: " + dataInicial.format(DATE_FORMAT) + " a " + dataFinal.format(DATE_FORMAT), FONT_NORMAL));
        document.add(new Paragraph(" ")); 

        // 2. CRIAÇÃO DA TABELA (6 COLUNAS)
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new float[] {1.3f, 1.5f, 3.0f, 3.0f, 1.5f});

        // 3. CABEÇALHO DA TABELA
        addTableHeader(table, "Data", "Nº Conta", "Nome Cliente", "Tipo", "Valor");

        // 4. CONTEÚDO DA TABELA
        for (Movimentacao mov : extrato) {
            addContentRow(table, mov);
        }

        document.add(table);
        document.close();
    }
    
    private static void addTableHeader(PdfPTable table, String... headers) {
        for (String header : headers) {
            PdfPCell headerCell = new PdfPCell(new Phrase(header, FONT_HEADER));
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setBackgroundColor(BaseColor.DARK_GRAY);
            headerCell.setPadding(5);
            table.addCell(headerCell);
        }
    }
    
    private static void addContentRow(PdfPTable table, Movimentacao mov) {
        // Coluna 1: Data
        table.addCell(createCell(mov.getDataMovimento().format(DATE_FORMAT), Element.ALIGN_CENTER, FONT_NORMAL));

        // Coluna 2: Conta Parceira
        String numeroConta = mov.getNumeroContaDestino();
        String contaDisplay = formatPartnerDisplay(mov.getTipoMovimento(), numeroConta, "Para: ", "De: ", "Externo");
        table.addCell(createCell(contaDisplay, Element.ALIGN_LEFT, FONT_NORMAL));
        
        // Coluna 3: Nome Parceiro/Cliente
        String nome = mov.getNomeClienteDestino();
        String nomeDisplay = formatPartnerDisplay(mov.getTipoMovimento(), nome, "Para: ", "De: ", "Inválido");
        if (mov.getTipoMovimento() == TipoMovimento.DEPOSITO) {
             nomeDisplay = "Própria Conta";
        } else if (mov.getTipoMovimento() == TipoMovimento.SAQUE) {
             nomeDisplay = "Caixa Eletrônico";
        }
        table.addCell(createCell(nomeDisplay, Element.ALIGN_LEFT, FONT_NORMAL));
        
        // Coluna 4: Tipo
        table.addCell(createCell(mov.getTipoMovimento().getDescricao(), Element.ALIGN_LEFT, FONT_NORMAL));
        
        // Coluna 5: Valor (com cor)
        boolean isDebit = mov.getTipoMovimento().name().contains("ENVIADA") || mov.getTipoMovimento().name().equals("SAQUE");
        BaseColor color = isDebit ? new BaseColor(200, 0, 0) : new BaseColor(0, 150, 0); // Vermelho ou Verde
        Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 10, color);
        table.addCell(createCell(NF.format(mov.getValor()), Element.ALIGN_RIGHT, valueFont));
    }
    
    private static PdfPCell createCell(String content, int alignment, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setHorizontalAlignment(alignment);
        cell.setPadding(5);
        return cell;
    }
    
    private static String formatPartnerDisplay(TipoMovimento tipo, String value, String prefixSend, String prefixReceive, String defaultText) {
        if (value == null) {
             return defaultText;
        }
        if (tipo == TipoMovimento.TRANSFERENCIA_ENVIADA) {
             return prefixSend + value;
        } else if (tipo == TipoMovimento.TRANSFERENCIA_RECEBIDA) {
             return prefixReceive + value;
        }
        return "-";
    }
}