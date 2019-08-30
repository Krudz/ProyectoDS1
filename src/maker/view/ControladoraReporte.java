package maker.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import maker.model.*;


public class ControladoraReporte {

    @FXML
    private DatePicker dpSelectorFecha;

    private Stage stage;

    // Fonts definitions (Definición de fuentes).
    private static final Font chapterFont = FontFactory.getFont(FontFactory.HELVETICA, 30, Font.BOLDITALIC);
    private static final Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL);

    private static final Font categoryFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static final Font subcategoryFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    private static final Font blueFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
    private static final Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD, BaseColor.BLUE);

    private static final String fondoPantalla = "/Users/alexis/Downloads/parapdf.png";

    public void setStage(Stage stage){
        this.stage = stage;
    }

    private FacturaBD bdFactura;
    private ClienteBD bdCliente;
    private ItemFacturaBD bdItem;
    private ObservableList<Factura> listaFacturasConsulta = FXCollections.observableArrayList();
    private ObservableList<ItemFactura> listaItems = FXCollections.observableArrayList();

    @FXML
    public void initialize(){
        bdFactura = new FacturaBD();
        bdCliente = new ClienteBD();
        bdItem = new ItemFacturaBD();
    }




    @FXML
    public void verReporte(){
        if(!dpSelectorFecha.getEditor().getText().equals("".trim()) && !dpSelectorFecha.getEditor().getText().equals("coincide con base de datos")){
            try{
                listaFacturasConsulta = bdFactura.consultar(dpSelectorFecha.getValue());
                System.out.println("la cantidad de facturas encontradas es: " + listaFacturasConsulta.size() + "\n\n\n");

                createPDF(new File("/Users/alexis/Desktop/ReporteDamasco.pdf"));
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }


    public void createPDF(File pdfNewFile) {
        // Creamos el documento e indicamos el nombre del fichero.
        try {
            Document document = new Document();
            try {

                PdfWriter.getInstance(document, new FileOutputStream(pdfNewFile));

            } catch (FileNotFoundException fileNotFoundException) {
                System.out.println("No Encontrado "
                        + "(No se encontró el fichero para generar el pdf)" + fileNotFoundException);
            }
            document.open();
            // Añadimos los metadatos del PDF
            document.addTitle("Reporte de ventas");
            document.addSubject("Shopping");
            document.addKeywords("Java, PDF");
            document.addAuthor("Damasco Records");
            document.addCreator("Christian Rodriguez");

            // Primera página
            Chunk chunk = new Chunk("Reporte de ventas DAMASCO", chapterFont);
            chunk.setBackground(BaseColor.YELLOW);

            //(Creemos el primer capítulo)
            Chapter chapter = new Chapter(new Paragraph(chunk), 1);
            chapter.setNumberDepth(0);
            chapter.add(new Paragraph("Total ventas realizadas en el dia " + dpSelectorFecha.getValue().toString() + ":", paragraphFont));
            chapter.add(new Paragraph("->", paragraphFont));

            //(Añadimos una imagen)
            Image image = null;
            try {
                image = Image.getInstance(fondoPantalla);
                image.setAbsolutePosition(50, 120);
                chapter.add(image);
            } catch (BadElementException ex) {
                System.out.println("Image BadElementException" +  ex);
            } catch (IOException ex) {
                System.out.println("Image IOException " +  ex);
            }

            // Utilización de PdfPTable
            Integer numColumns = 4;
            Integer numRows = 5;

            // Creamos la tabla
            PdfPTable table = new PdfPTable(numColumns);
            table.setWidthPercentage(100);

            // Ahora llenamos la tabla del PDF
            PdfPCell columnHeader;

            // (rellenamos las COLUMNAS de la tabla).
            //columna 1
            columnHeader = new PdfPCell(new Phrase("DESCRIPCION ",smallBold));
            columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(columnHeader);

            //columna 2
            columnHeader = new PdfPCell(new Phrase("CANTIDAD DE VENTAS ",smallBold));
            columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(columnHeader);

            //columna 3
            columnHeader = new PdfPCell(new Phrase("TOTAL VENTAS ", smallBold));
            columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(columnHeader);

            //columna 4
            columnHeader = new PdfPCell(new Phrase("UTILIDAD VENTAS ",smallBold));
            columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(columnHeader);

            table.setHeaderRows(1);


            //(rellenamos las filas de la tabla).
            for (int row = 0; row < numRows; row++) {
                for (int column = 0; column < numColumns; column++) {
                    table.addCell("Row " + row + " - Col" + column);
                }
            }
            // (Añadimos la tabla)
            chapter.add(table);

            chapter.add(new Paragraph("->", paragraphFont));
            chapter.add(new Paragraph("Utilidad total: $ 150.000", paragraphFont));
            chapter.add(new Paragraph("->", paragraphFont));



            //organiza los datos encontrados para posteriormente mostrar
            for (int i = 0; i < listaFacturasConsulta.size(); i++) {
                Cliente clienteEncontrado = bdCliente.consultar(listaFacturasConsulta.get(i).getDatosCliente());
                listaItems = bdItem.consultar(listaFacturasConsulta.get(i).getNumeroRef());
                crearFactura(chapter, image, i, clienteEncontrado, listaItems);
            }


            // (Añadimos el elemento con la tabla).
            document.add(chapter);
            document.close();

            System.out.println("El reporte PDF se ha generado");
        } catch (DocumentException documentException) {
            System.out.println("Ha ocurrido un error al generar el archivo PDF " + documentException);
        }
    }





    public void crearFactura(Chapter chapter, Image image, int i, Cliente clienteEncontrado, ObservableList<ItemFactura> items){
        chapter.add(new Paragraph("->", paragraphFont));
        chapter.add(image);


        Integer numColumnsFact = 1;

        // Creamos la tabla
        PdfPTable tableFact = new PdfPTable(numColumnsFact);
        PdfPCell columnHeaderFact;

        columnHeaderFact = new PdfPCell(new Phrase("Factura # " + listaFacturasConsulta.get(i).getNumeroRef(),smallBold));
        columnHeaderFact.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableFact.addCell(columnHeaderFact);

        tableFact.setHeaderRows(1);

        String contenidoTabla = "Datos cliente: \n" + clienteEncontrado.getNombre() + " " + clienteEncontrado.getApellido()
                + "\n\nIdentificacion: \n" +  clienteEncontrado.getIdentificacion() + "\n\nCelular: \n" +  clienteEncontrado.getTelefono() + "\n\n\nDetalle de la compra: \n";

        for (int j = 0; j < items.size(); j++) {
            contenidoTabla += items.get(j).getDescripcion() + "     " + items.get(j).getSubtotal() + "\n";
        }

        contenidoTabla += "\nTotal Compra: " + listaFacturasConsulta.get(i).getTotalVenta() + "\n\n" +
        "Utilidad por Compra: $35.000\n\nMedio de pago: " + listaFacturasConsulta.get(i).getMedioDePago();

        //(rellenamos las filas de la tabla).
        tableFact.addCell(contenidoTabla);
        chapter.add(tableFact);
        listaItems.remove(0,listaItems.size()-1);

    }

}
