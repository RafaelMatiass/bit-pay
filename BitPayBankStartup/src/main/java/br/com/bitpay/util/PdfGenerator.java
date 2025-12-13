package br.com.bitpay.util;

import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfShading;
import com.itextpdf.text.pdf.PdfShadingPattern;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import br.com.bitpay.model.Conta;
import br.com.bitpay.model.Movimentacao;
import br.com.bitpay.model.Enums.TipoMovimento;

public class PdfGenerator {

    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final NumberFormat NF = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));

    // Tema BitPay Blue
    private static final BaseColor BLUE = new BaseColor(0, 70, 200);
    private static final BaseColor LIGHT_BLUE = new BaseColor(230, 240, 255);
    private static final BaseColor BORDER = new BaseColor(190, 200, 220);
    private static final BaseColor ROW_ALT = new BaseColor(248, 251, 255);

    public static void gerarExtratoPdf(OutputStream out,
                                       Conta conta,
                                       List<Movimentacao> extrato,
                                       LocalDate inicio,
                                       LocalDate fim) throws Exception {

        Document doc = new Document(PageSize.A4, 40, 40, 40, 40);
        PdfWriter writer = PdfWriter.getInstance(doc, out);
        doc.open();

        PdfContentByte cb = writer.getDirectContentUnder();

        // -------------------------------------------
        // FUNDO SUAVE (BANCO DIGITAL)
        // -------------------------------------------
        drawBackgroundGradient(cb);

        // -------------------------------------------
        // LOGO
        // -------------------------------------------
        try {
            Image logo = Image.getInstance("utils/logo-bitpay.png");
            logo.scaleAbsolute(95, 26);
            logo.setAbsolutePosition(40, PageSize.A4.getHeight() - 60);
            doc.add(logo);
        } catch (Exception e) {}

        // -------------------------------------------
        // TÍTULO
        // -------------------------------------------
        Paragraph title = new Paragraph(
                "Extrato Bancário",
                new Font(Font.FontFamily.HELVETICA, 19, Font.BOLD, BLUE)
        );
        title.setSpacingBefore(30);
        title.setSpacingAfter(6);
        doc.add(title);

        // Linha fina (estilo bancário)
        LineSeparator ls = new LineSeparator();
        ls.setLineColor(BLUE);
        ls.setLineWidth(1f);
        doc.add(ls);

        Paragraph infos = new Paragraph(
                "Conta: " + conta.getNumeroConta() +
                "\nPeríodo: " + inicio.format(DF) + " a " + fim.format(DF),
                new Font(Font.FontFamily.HELVETICA, 10.5f, Font.NORMAL, BaseColor.DARK_GRAY)
        );
        infos.setSpacingBefore(10);
        infos.setSpacingAfter(20);
        doc.add(infos);

     // -------------------------------------------
     // QR CODE (MENOR + OPACIDADE REAL)
     // -------------------------------------------
     BarcodeQRCode qr = new BarcodeQRCode(
             "BitPay | Conta: " + conta.getNumeroConta(),
             90, 90, null
     );

     Image qrImg = qr.getImage();
     qrImg.scalePercent(90);
     qrImg.setAbsolutePosition(
             PageSize.A4.getWidth() - 115,
             PageSize.A4.getHeight() - 135
     );

     // OPACIDADE CORRETA NO ITEXT 5
     PdfGState gState = new PdfGState();
     gState.setFillOpacity(0.85f);

     PdfContentByte canvas = writer.getDirectContent();
     canvas.saveState();
     canvas.setGState(gState);

     doc.add(qrImg);

     canvas.restoreState();

        // -------------------------------------------
        // TABELA DO EXTRATO
        // -------------------------------------------
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1.8f, 3.0f, 2.2f, 2.0f, 3.0f});
        table.setSpacingBefore(10);
        table.setSpacingAfter(18);

        addHeader(table, "Data");
        addHeader(table, "Tipo");
        addHeader(table, "Valor");
        addHeader(table, "Conta");
        addHeader(table, "Cliente");

        boolean alt = false;

        for (Movimentacao mov : extrato) {

            BaseColor bg = alt ? ROW_ALT : BaseColor.WHITE;
            alt = !alt;

            table.addCell(cell(mov.getDataMovimento().format(DF), Element.ALIGN_CENTER, bg));
            table.addCell(cell(mov.getTipoMovimento().getDescricao(), Element.ALIGN_LEFT, bg));

            boolean debito = mov.getTipoMovimento().name().contains("ENVIADA")
                    || mov.getTipoMovimento() == TipoMovimento.SAQUE;

            Font fValor = new Font(
                    Font.FontFamily.HELVETICA,
                    10,
                    Font.BOLD,
                    debito ? new BaseColor(190, 0, 0) : new BaseColor(0, 140, 90)
            );

            table.addCell(cellStyled(NF.format(mov.getValor()), Element.ALIGN_RIGHT, bg, fValor));
            table.addCell(cell(getContaParceira(mov), Element.ALIGN_LEFT, bg));
            table.addCell(cell(getNomeParceiro(mov), Element.ALIGN_LEFT, bg));
        }

        doc.add(table);

        // -------------------------------------------
        // ASSINATURA DIGITAL (VISUAL COMPLIANCE)
        // -------------------------------------------
        String hash = gerarHash(extrato.toString());

        PdfPTable sign = new PdfPTable(1);
        sign.setWidthPercentage(100);
        PdfPCell sc = new PdfPCell(new Phrase(
                "Assinatura Digital • SHA-256\n" + hash,
                new Font(Font.FontFamily.COURIER, 8.5f, Font.NORMAL, BaseColor.GRAY)
        ));
        sc.setPadding(10);
        sc.setBorderColor(BORDER);
        sc.setBackgroundColor(LIGHT_BLUE);
        sign.addCell(sc);

        sign.setSpacingBefore(18);
        doc.add(sign);

        // -------------------------------------------
        // RODAPÉ
        // -------------------------------------------
        Paragraph footer = new Paragraph(
                "BitPay Blue • Banco Digital Futurista | Documento gerado automaticamente",
                new Font(Font.FontFamily.HELVETICA, 9, Font.ITALIC, BaseColor.GRAY)
        );
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(28);
        doc.add(footer);

        doc.close();
    }

    // ---------------------------------------------------------------------
    private static void drawBackgroundGradient(PdfContentByte cb) {
        BaseColor top = new BaseColor(248, 251, 255);
        BaseColor bottom = new BaseColor(220, 232, 255);

        Rectangle rect = new Rectangle(0, 0, PageSize.A4.getWidth(), PageSize.A4.getHeight());
        PdfShading shading = PdfShading.simpleAxial(
                cb.getPdfWriter(),
                0, 0, 0, PageSize.A4.getHeight(),
                bottom, top
        );

        PdfShadingPattern pattern = new PdfShadingPattern(shading);
        cb.setShadingFill(pattern);
        cb.rectangle(rect.getLeft(), rect.getBottom(), rect.getWidth(), rect.getHeight());
        cb.fill();
    }

    private static String getContaParceira(Movimentacao m) {
        return m.getNumeroContaDestino() == null ? "-" : m.getNumeroContaDestino();
    }

    private static String getNomeParceiro(Movimentacao m) {
        return m.getNomeClienteDestino() == null ? "-" : m.getNomeClienteDestino();
    }

    private static void addHeader(PdfPTable t, String text) {
        PdfPCell c = new PdfPCell(new Phrase(
                text,
                new Font(Font.FontFamily.HELVETICA, 10.5f, Font.BOLD, BaseColor.WHITE)
        ));
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        c.setPadding(6);
        c.setBackgroundColor(BLUE);
        c.setBorderColor(BORDER);
        t.addCell(c);
    }

    private static PdfPCell cell(String text, int align, BaseColor bg) {
        return cellStyled(text, align, bg,
                new Font(Font.FontFamily.HELVETICA, 10));
    }

    private static PdfPCell cellStyled(String text, int align, BaseColor bg, Font font) {
        PdfPCell c = new PdfPCell(new Phrase(text, font));
        c.setPadding(6);
        c.setHorizontalAlignment(align);
        c.setBorderColor(BORDER);
        c.setBackgroundColor(bg);
        return c;
    }

    private static String gerarHash(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return String.format("%064x",
                new BigInteger(1, md.digest(input.getBytes("UTF-8"))));
    }
}
