package services;

import static config.ResourceNames.TICKETS_PDFS_ROOT;
import static config.ResourceNames.TICKET_PDF_FILENAME_ROOT;

import java.text.SimpleDateFormat;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import entities.core.Shopping;
import entities.core.Ticket;

public class TicketPdfGenerator extends PdfGenerator<Ticket> {
    private final static float[] SHOPPING_LIST_COLUMNS_WIDTHS = new float[] {10.0f, 30.0f, 30.0f, 10.0f, 40.0f, 30.0f};

    public TicketPdfGenerator(Ticket entity) {
        super(entity);
    }

    @Override
    protected String ownPath() {
        return TICKETS_PDFS_ROOT + TICKET_PDF_FILENAME_ROOT + entity.getId();
    }

    @Override
    protected PageSize ownPageSize() {
        return PageSize.A7;
    }

    @Override
    protected void buildPdf() {
        document.setFontSize(3.0f);
        document.add(new Paragraph("Reference: " + entity.getReference()));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        document.add(new Paragraph("Created on: " + formatter.format(entity.getCreated().getTime())));
        document.add(new Paragraph("Shopping list:"));
        Table shoppingListTable = new Table(SHOPPING_LIST_COLUMNS_WIDTHS);
        shoppingListTable.addCell("Id");
        shoppingListTable.addCell("Amount");
        shoppingListTable.addCell("Discount");
        shoppingListTable.addCell("ProductId");
        shoppingListTable.addCell("Description");
        shoppingListTable.addCell("Retail price");
        for (Shopping shopping : entity.getShoppingList()) {
            shoppingListTable.addCell(String.valueOf(shopping.getId()));
            shoppingListTable.addCell(String.valueOf(shopping.getAmount()));
            shoppingListTable.addCell(String.valueOf(shopping.getDiscount() + "%"));
            shoppingListTable.addCell(String.valueOf(shopping.getProduct().getDescription()));
            shoppingListTable.addCell(String.valueOf(shopping.getDescription()));
            shoppingListTable.addCell(String.valueOf(shopping.getRetailPrice() + "€"));
        }
        document.add(shoppingListTable);
    }
}
