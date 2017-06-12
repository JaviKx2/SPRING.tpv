package services;

import static config.ResourceNames.TICKETS_PDFS_ROOT;
import static config.ResourceNames.TICKET_PDF_FILENAME_ROOT;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;

import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import config.ResourceNames;
import entities.core.Shopping;
import entities.core.Ticket;

public class TicketPdfGenerator extends PdfGenerator<Ticket> {

    private final static float[] SHOPPING_LIST_COLUMNS_WIDTHS = new float[] {1, 1, 1, 1, 1, 1};

    private final static float TICKET_PAGE_WIDHT = 227;

    private final static float TICKET_PAGE_HEIGHT = 842;

    private final static float QR_CODE_MODULE_SIZE = 2.5f;

    public TicketPdfGenerator(Ticket entity) {
        super(entity);
    }

    @Override
    protected String path() {
        return TICKETS_PDFS_ROOT + TICKET_PDF_FILENAME_ROOT + entity.getId();
    }

    @Override
    protected PageSize pageSize() {
        return new PageSize(TICKET_PAGE_WIDHT, TICKET_PAGE_HEIGHT);
    }

    @Override
    protected PdfFont font() throws IOException {
        return PdfFontFactory.createFont(ResourceNames.FONTS + ResourceNames.FAKE_RECEIPT_REGULAR_FONT, PdfEncodings.CP1250, true);
    }

    @Override
    protected float fontSize() {
        return 8.0f;
    }

    @Override
    protected HorizontalAlignment horizontalAlignment() {
        return HorizontalAlignment.CENTER;
    }

    @Override
    protected TextAlignment textAlignment() {
        return TextAlignment.CENTER;
    }

    @Override
    protected float leftMargin() {
        return 0;
    }

    @Override
    protected float rightMargin() {
        return 0;
    }

    @Override
    protected float topMargin() {
        return 0;
    }

    @Override
    protected float bottomMargin() {
        return 0;
    }

    @Override
    protected void buildPdf() {
        try {
            Image img = new Image(ImageDataFactory.create(ResourceNames.IMAGES + ResourceNames.UPM_LOGO));
            img.setWidth(50);
            img.setHorizontalAlignment(HorizontalAlignment.CENTER);
            document.add(img);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        document.add(new Paragraph("Campus Sur UPM, Calle Nikola Tesla, s/n, 28031 Madrid"));
        CustomDashedLineSeparator separator = new CustomDashedLineSeparator();
        separator.setDash(1);
        separator.setGap(2);
        separator.setLineWidth(0.5f);
        document.add(new LineSeparator(separator));

        document.add(new Paragraph("REFERENCE: " + entity.getReference()));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        document.add(new Paragraph("CREATED ON: " + formatter.format(entity.getCreated().getTime())));
        Table shoppingListTable = new Table(SHOPPING_LIST_COLUMNS_WIDTHS);
        shoppingListTable.setBorder(new SolidBorder(Color.WHITE, 5));
        shoppingListTable.setVerticalAlignment(VerticalAlignment.MIDDLE);
        shoppingListTable.setHorizontalAlignment(HorizontalAlignment.CENTER);
        shoppingListTable.addHeaderCell("");
        shoppingListTable.addHeaderCell("Amount".toUpperCase());
        shoppingListTable.addHeaderCell("%".toUpperCase());
        shoppingListTable.addHeaderCell("Product".toUpperCase());
        shoppingListTable.addHeaderCell("Description".toUpperCase());
        shoppingListTable.addHeaderCell("Price".toUpperCase());
        entity.getShoppingList().addAll(entity.getShoppingList());
        entity.getShoppingList().addAll(entity.getShoppingList());
        entity.getShoppingList().addAll(entity.getShoppingList());
        double total = 0.0;
        for (int i = 0; i < entity.getShoppingList().size(); i++) {
            Shopping shopping = entity.getShoppingList().get(i);
            shoppingListTable.addCell(String.valueOf(i + 1));
            shoppingListTable.addCell(String.valueOf(shopping.getAmount()));
            shoppingListTable.addCell(String.valueOf(shopping.getDiscount() + "%"));
            shoppingListTable.addCell(String.valueOf(shopping.getProduct().getDescription()));
            shoppingListTable.addCell(String.valueOf(shopping.getDescription()));
            shoppingListTable.addCell(String.valueOf(shopping.getRetailPrice() + "€"));
            total += shopping.getRetailPrice().doubleValue();
        }
        Cell sixColumsCell = new Cell(1, 6);
        sixColumsCell.setTextAlignment(TextAlignment.RIGHT);
        sixColumsCell.add("TOTAL: " + String.valueOf(new BigDecimal(total)) + "€");
        shoppingListTable.addCell(sixColumsCell);
        document.add(shoppingListTable);
        document.add(new LineSeparator(separator));
        document.add(new Cell().add("TICKET TRACKING"));
        document.add(qrCodeImage(entity.getReference(), document.getPdfDocument()));
        document.add(new LineSeparator(separator));
        document.add(new Paragraph("PLAZO DEVOLUC. 15 DIAS. CONSERVE TICKET"));
    }

    private Image qrCodeImage(String reference, PdfDocument pdfDocument) {
        BarcodeQRCode qrCode = new BarcodeQRCode(reference);
        PdfFormXObject pdfFormXObject = qrCode.createFormXObject(Color.BLACK, QR_CODE_MODULE_SIZE, pdfDocument);
        Image qrCodeImage = new Image(pdfFormXObject);
        qrCodeImage.setHorizontalAlignment(HorizontalAlignment.CENTER);
        qrCodeImage.setTextAlignment(TextAlignment.CENTER);
        return qrCodeImage;
    }

}
