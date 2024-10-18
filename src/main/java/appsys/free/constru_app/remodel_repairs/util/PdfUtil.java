package appsys.free.constru_app.remodel_repairs.util;

import appsys.free.constru_app.remodel_repairs.dtos.Requester_Dto;
import appsys.free.constru_app.remodel_repairs.dtos.Work_Dto;
import appsys.free.constru_app.remodel_repairs.entities.Equipment;
import appsys.free.constru_app.remodel_repairs.entities.Material;
import appsys.free.constru_app.remodel_repairs.entities.Transport;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PdfUtil {

    private static final Logger logger = LoggerFactory.getLogger(PdfUtil.class);
    private Document doc;
    private PdfWriter pdf;

    private NumberFormat formatoNumero = NumberFormat.getIntegerInstance(Locale.getDefault());
    private SimpleDateFormat formatFecha = new SimpleDateFormat("yyyy/MM/dd");
    private Font fontBold = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD);

    private Font fontBold2 = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD);
    private Font fontBold3 = new Font(Font.FontFamily.COURIER, 8, Font.BOLD);
    private Font fontBold4 = new Font(Font.FontFamily.COURIER, 8, Font.NORMAL);

    public void getPdfRequest(HttpServletResponse response, Requester_Dto requesterDto) {
        try {
            doc = new Document(PageSize.LETTER, 36, 36, 10, 10);
            PdfWriter.getInstance(doc, response.getOutputStream());
            doc.open();
            header(requesterDto);
            body(requesterDto);

            doc.close();
        } catch (Exception e) {
            logger.error("GeneratePdf.export " + e.getMessage());
        }

    }

    private void header(Requester_Dto requesterDto) {
        try {
            //logo
            Paragraph paragraph1 = new Paragraph();
            Image image = Image.getInstance("C:/distCA/assets/Logo2.jpg");
            image.scaleAbsolute(90, 70);
            image.setAlignment(Image.ALIGN_LEFT);
            Chunk chunk1 = new Chunk(image, -10, -60);
            paragraph1.add(chunk1);
            doc.add(paragraph1);
            doc.add(addParagraf("COTIZACION PARA REMODELACION Y REPARACION", fontBold, 0, 1));
            doc.add(addParagraf("SOLICITUD No: " + requesterDto.getId(), fontBold, 20, 1));
            String c1[] = {"No DOCUMENTO: " + requesterDto.getNumberDoc(), "NOMBRE: " + requesterDto.getNames() + " " + requesterDto.getSurnames()};
            doc.add(addFila(getCells(c1, fontBold3, 0), 20, 100));
            String c2[] = {"DIRECCIÓN: " + requesterDto.getAddress(), "DEPARATMENTO: " + requesterDto.getMunicipalityObj().getDepartament().getName(), "MUNICIPIO: " + requesterDto.getMunicipalityObj().getName()};
            doc.add(addFila(getCells(c2, fontBold3, 0), 0, 100));
            String c3[] = {"No CONTACTO: " + requesterDto.getPhoneNumber(), "CORREO: " + requesterDto.getEmail(), "COTIZACIÓN VALIDA HASTA: " + requesterDto.getDateLimit()};
            doc.add(addFila(getCells(c3, fontBold3, 0), 0, 100));


        } catch (Exception e) {
            logger.error("PdfUtil.getPdfRequest.header " + e.getMessage());
        }

    }

    private void body(Requester_Dto requesterDto) {
        try {

            int indWork = 1;
            int totalWork = 0;
            int totalMater = 0;
            int totalEquip = 0;
            int totalTransp = 0;
            for (Work_Dto work : requesterDto.getListWorksToBeDone()) {
                totalMater += work.getMaterialCost();
                totalWork += work.getLaborCost();
                String c1[] = {"LABOR A REALIZAR : " + work.getDescription()};
                doc.add(addFila(getCellsColor2(c1, fontBold2, 1), 20, 100));
                String c2[] = {"ITEM No", "MEDIDO EN", "CANTIDAD", "COSTO UNIDAD", "COSTO MANO OBRA", "COSTO MATERIALES", "TOTAL"};
                doc.add(addFila(getCells(c2, fontBold3, 1), 0, 100));
                String c3[] = {String.valueOf(indWork), work.getTypeManp(), String.valueOf(work.getNumberManp()), formatoNumero.format(work.getCostManp()), formatoNumero.format(work.getLaborCost()), formatoNumero.format(work.getMaterialCost()), formatoNumero.format(work.getLaborCost() + work.getMaterialCost())};
                doc.add(addFila(getCells(c3, fontBold4, 1), 0, 100));
                if (!work.getMaterials().isEmpty()) {
                    if (work.getMaterials().size() > 0) {
                        String c4[] = {"MATERIALES REQUERIDOS"};
                        doc.add(addFila(getCells(c4, fontBold2, 1), 0, 100));
                        String c5[] = {"", "DESCRIPCIÓN", "CANTIDAD", "VALOR UNID", "SUBTOTAL"};
                        doc.add(addFila(getCells(c5, fontBold3, 1), 0, 100));
                        int indMater = 0;
                        for (Material material : work.getMaterials()) {

                            String c6[] = {indWork + "." + indMater, material.getDescription(), formatoNumero.format(material.getAmount()), formatoNumero.format(material.getValue()), formatoNumero.format(material.getSubtot())};
                            doc.add(addFila(getCells(c6, fontBold4, 1), 0, 100));
                            indMater += 1;
                        }
                    }
                }
                indWork += 1;
            }
            if (!requesterDto.getListEquipment().isEmpty()) {
                if (requesterDto.getListEquipment().size() > 0) {
                    String c1[] = {"EQUIPOS REQUERIDOS"};
                    doc.add(addFila(getCellsColor2(c1, fontBold2, 1), 20, 100));
                    String c2[] = {"No", "NOMBRE EQUIPO", "CANT EQUIPOS", "TAR.DIA", "No DIAS", "SUBT.DIA", "TAR.HORA", "No HORAS", "SUBT.HORAS"};
                    doc.add(addFila(getCells(c2, fontBold3, 1), 0, 100));
                    int indEquip = 1;
                    for (Equipment equipment : requesterDto.getListEquipment()) {
                        String c3[] = {String.valueOf(indEquip), equipment.getEquipment(), String.valueOf(equipment.getNumberEquipments()), formatoNumero.format(equipment.getDayValue()), String.valueOf(equipment.getNumberDays()), formatoNumero.format(equipment.getSubtDayValue()), formatoNumero.format(equipment.getHourValue()), String.valueOf(equipment.getNumberHours()), formatoNumero.format(equipment.getSubtHourValue())};
                        doc.add(addFila(getCells(c3, fontBold4, 1), 0, 100));
                        totalEquip += (equipment.getSubtHourValue() + equipment.getSubtDayValue());
                        indEquip++;
                    }
                }
            }
            if (!requesterDto.getListTransport().isEmpty()) {
                if (requesterDto.getListTransport().size() > 0) {
                    int indTrans = 1;
                    String c1[] = {"TRANSPORTE"};
                    doc.add(addFila(getCellsColor2(c1, fontBold2, 1), 20, 100));
                    String c2[] = {"No", "DESCRIPCIÓN", "CANT", "VALOR FLETE", "SUBTOT"};
                    doc.add(addFila(getCells(c2, fontBold3, 1), 0, 100));
                    for (Transport transport : requesterDto.getListTransport()) {
                        String c3[] = {String.valueOf(indTrans), transport.getDescription(), String.valueOf(transport.getAmount()), formatoNumero.format(transport.getValue()), formatoNumero.format(transport.getSubtot())};
                        doc.add(addFila(getCells(c3, fontBold4, 1), 0, 100));
                        totalTransp += transport.getSubtot();
                        indTrans++;
                    }
                }
            }
            String c1[] = {"TOTAL MANO DE OBRA:", "$" + formatoNumero.format(totalWork)};
            doc.add(addFila(getCellsColor3(c1, fontBold2, 1), 20, 50));

            String c1_1[] = {"TOTAL MATERIALES:", "$" + formatoNumero.format(totalMater)};
            doc.add(addFila(getCellsColor3(c1_1, fontBold2, 1), 0, 50));

            String c2[] = {"TOTAL EQUIPOS:", "$" + formatoNumero.format(totalEquip)};
            doc.add(addFila(getCellsColor3(c2, fontBold2, 1), 0, 50));

            String c3[] = {"TOTAL TRANSPORTE:", "$" + formatoNumero.format(totalTransp)};
            doc.add(addFila(getCellsColor3(c3, fontBold2, 1), 0, 50));

            String c4[] = {"TOTAL COTIZACIÓN:", "$" + formatoNumero.format(totalTransp + totalEquip + totalWork + totalMater)};
            doc.add(addFila(getCellsColor3(c4, fontBold2, 1), 0, 50));


            doc.add(addParagraf("COTIZACIÓN GENERADA CON FECHA "+formatFecha.format(new Date())+" POR: ___________________________", fontBold, 20, 0));
            doc.add(addParagraf("C.C: ___________________________", fontBold, 0, 0));

        } catch (Exception e) {
            logger.error("PdfUtil.getPdfRequest.body " + e.getMessage());
        }

    }

    private Paragraph addParagraf(String text, Font font, int spacing, int align) {
        //center=1  left=0  rigth=2  bottom=6 top=4
        Paragraph paragraph = new Paragraph();
        Chunk chunk = new Chunk();
        chunk.append(text);
        chunk.setFont(font);
        paragraph.add(chunk);
        paragraph.setSpacingBefore(spacing);
        paragraph.setAlignment(align);
        return paragraph;

    }


    public PdfPTable addFila(ArrayList<PdfPCell> cells, int spacing, int width) {
        try {
            int columns = cells.size();
            PdfPTable fila = new PdfPTable(columns);
            fila.setSpacingBefore(spacing);
            fila.setWidthPercentage(width);
            fila.setHorizontalAlignment(0);

            if (cells != null) {
                if (cells.size() > 0) {
                    for (PdfPCell pdfCell : cells) {
                        fila.addCell(pdfCell);
                    }
                }
            }
            return fila;
        } catch (Exception e) {
            return null;
        }

    }


    public ArrayList<PdfPCell> getCells(String[] texts, Font font, int align) {
        if (texts.length > 0) {
            ArrayList<PdfPCell> cells = new ArrayList<>();
            for (String text : texts) {
                PdfPCell pdfPCell = new PdfPCell(addParagraf(text, font, 0, align));
                pdfPCell.setHorizontalAlignment(align);
                pdfPCell.setVerticalAlignment(align);

                cells.add(pdfPCell);
            }
            return cells;
        }
        return null;
    }

    public ArrayList<PdfPCell> getCellsColor(String[] texts, Font font, int align) {
        if (texts.length > 0) {
            ArrayList<PdfPCell> cells = new ArrayList<>();
            for (String text : texts) {
                PdfPCell pdfPCell = new PdfPCell(addParagraf(text, font, 0, align));
                pdfPCell.setHorizontalAlignment(align);
                pdfPCell.setVerticalAlignment(align);
                pdfPCell.setBackgroundColor(BaseColor.GRAY);
                cells.add(pdfPCell);
            }
            return cells;
        }
        return null;
    }

    public ArrayList<PdfPCell> getCellsColor2(String[] texts, Font font, int align) {
        if (texts.length > 0) {
            ArrayList<PdfPCell> cells = new ArrayList<>();

            // Elegir un color más formal, como un gris oscuro
           // BaseColor backgroundColor = new BaseColor(169, 169, 169);
          BaseColor backgroundColor = new BaseColor(173, 216, 230); // Azul Claro
          BaseColor borderColor = new BaseColor(50, 50, 50); // Color gris oscuro o negro para bordes

            for (String text : texts) {
                PdfPCell pdfPCell = new PdfPCell(addParagraf(text, font, 0, align));

                // Alinear el contenido
                pdfPCell.setHorizontalAlignment(align);
                pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE); // Alineación vertical centrada

                // Colores de fondo y borde
                pdfPCell.setBackgroundColor(backgroundColor);
                pdfPCell.setBorderColor(borderColor);
                pdfPCell.setPadding(5); // Agregar un poco de espacio dentro de la celda para mejorar la estética

                // Ajustes de borde
                pdfPCell.setBorderWidth(1.2f); // Ancho del borde

                cells.add(pdfPCell);
            }

            return cells;
        }
        return null;
    }

    public ArrayList<PdfPCell> getCellsColor3(String[] texts, Font font, int align) {
        if (texts.length > 0) {
            ArrayList<PdfPCell> cells = new ArrayList<>();


            BaseColor backgroundColor = new BaseColor(210, 228, 235); // Azul Claro
            BaseColor borderColor = new BaseColor(50, 50, 50); // Color gris oscuro o negro para bordes

            for (String text : texts) {
                PdfPCell pdfPCell = new PdfPCell(addParagraf(text, font, 0, align));

                // Alinear el contenido
                pdfPCell.setHorizontalAlignment(align);
                pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE); // Alineación vertical centrada

                // Colores de fondo y borde
                pdfPCell.setBackgroundColor(backgroundColor);
                pdfPCell.setBorderColor(borderColor);
                pdfPCell.setPadding(5); // Agregar un poco de espacio dentro de la celda para mejorar la estética

                // Ajustes de borde
                pdfPCell.setBorderWidth(0.9f); // Ancho del borde

                cells.add(pdfPCell);
            }

            return cells;
        }
        return null;
    }



}
