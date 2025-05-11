package com.chi.PhongTro.service;

import com.chi.PhongTro.entity.Invoices;
import com.chi.PhongTro.entity.InvoiceDetails;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class DocumentGenerationService {

    public byte[] generatePdfInvoice(Invoices invoice) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("HÓA ĐƠN"));
        document.add(new Paragraph("Số hóa đơn: " + invoice.getInvoiceId()));
        document.add(new Paragraph("Ngày tạo: " + invoice.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE)));
        document.add(new Paragraph("Hạn thanh toán: " + invoice.getDueDate().format(DateTimeFormatter.ISO_LOCAL_DATE)));

        document.add(new Paragraph("Người thuê: " + invoice.getRenter().getFullName()));
        document.add(new Paragraph("Phòng: " + invoice.getRoom().getRoomNumber()));

        Table table = new Table(3);
        table.addCell("Mô tả");
        table.addCell("Số tiền");
        table.addCell("Ghi chú");

        for (InvoiceDetails detail : invoice.getDetails()) {
            table.addCell(detail.getDescription());
            table.addCell(String.format("%,.0f VNĐ", detail.getAmount()));
            table.addCell("");
        }

        document.add(table);

        document.add(new Paragraph("Tổng tiền: " + String.format("%,.0f VNĐ", invoice.getTotalAmount())));
        document.close();

        return baos.toByteArray();
    }

    public byte[] generateWordInvoice(Invoices invoice) throws Exception {
        XWPFDocument document = new XWPFDocument();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String address = String.join(", ",
                invoice.getRoom().getBuilding().getStreet(),
                invoice.getRoom().getBuilding().getDistrict(),
                invoice.getRoom().getBuilding().getCity()
        );
        addCenterParagraph(document, address, false, 12);

        String roomNumber = invoice.getRoom().getRoomNumber();
        String buildingName = invoice.getRoom().getBuilding().getName();
        addCenterParagraph(document, "HÓA ĐƠN PHÒNG TRỌ SỐ " + roomNumber + " / " + buildingName, true, 16);

        addCenterParagraph(document, "Mã hóa đơn: " + invoice.getInvoiceId(), false, 11);

        addInfo(document, "Ngày lập", invoice.getCreatedAt().format(dateFormatter));

        addInfo(document, "Số điện thoại (chủ trọ)", invoice.getOwner().getPhone());

        addInfo(document, "Người thuê", invoice.getRenter().getFullName());

        addInfo(document, "Hạn thanh toán", invoice.getDueDate().format(dateFormatter));

        document.createParagraph().createRun().addBreak();

        addCenterParagraph(document, "Chi tiết hóa đơn", true, 14);

        XWPFTable table = document.createTable();
        table.setWidth("100%");

        XWPFTableRow headerRow = table.getRow(0);
        styleCell(headerRow.getCell(0), "Mô tả", true);
        headerRow.addNewTableCell();
        styleCell(headerRow.getCell(1), "Số tiền", true);
        headerRow.addNewTableCell();
        styleCell(headerRow.getCell(2), "Ghi chú", true);

        for (InvoiceDetails detail : invoice.getDetails()) {
            XWPFTableRow row = table.createRow();
            styleCell(row.getCell(0), detail.getDescription(), false);
            styleCell(row.getCell(1), String.format("%,.0f VNĐ", detail.getAmount()), false);
            styleCell(row.getCell(2), "", false);
        }

        document.createParagraph().createRun().addBreak();

        XWPFParagraph totalPara = document.createParagraph();
        totalPara.setAlignment(ParagraphAlignment.RIGHT);
        XWPFRun totalRun = totalPara.createRun();
        totalRun.setBold(true);
        totalRun.setFontSize(17);
        totalRun.setFontFamily("Times New Roman");
        totalRun.setText("TỔNG TIỀN: " + String.format("%,.0f VNĐ", invoice.getTotalAmount()));

        document.write(baos);
        return baos.toByteArray();
    }

    private void addInfo(XWPFDocument doc, String label, String value) {
        XWPFParagraph p = doc.createParagraph();
        p.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun run = p.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(16);
        run.setBold(true);
        run.setText(label + ": ");

        XWPFRun valueRun = p.createRun();
        valueRun.setFontFamily("Times New Roman");
        valueRun.setFontSize(16);
        valueRun.setBold(false);
        valueRun.setText(value);
    }

    private void addCenterParagraph(XWPFDocument doc, String text, boolean bold, int fontSize) {
        XWPFParagraph p = doc.createParagraph();
        p.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = p.createRun();
        run.setText(text);
        run.setBold(bold);
        run.setFontSize(fontSize);
        run.setFontFamily("Times New Roman");
    }

    private void styleCell(XWPFTableCell cell, String text, boolean isHeader) {
        XWPFParagraph p = cell.getParagraphs().get(0);
        p.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = p.createRun();
        run.setText(text);
        run.setFontFamily("Times New Roman");
        run.setFontSize(16);
        run.setBold(isHeader);

        cell.getCTTc().addNewTcPr().addNewTcBorders().addNewBottom().setVal(STBorder.SINGLE);
    }


    private void addParagraph(XWPFDocument document, String text) {
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.setText(text);
    }
}