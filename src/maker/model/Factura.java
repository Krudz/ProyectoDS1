package maker.model;


import java.time.LocalDate;

public class Factura {
    private String numeroRef;
    private String datosCliente;
    private LocalDate fechaDeVenta;
    private String medioDePago;
    private String totalVenta;

    public Factura(String numeroRef, String datosCliente, LocalDate fechaDeVenta,  String medioDePago, String totalVenta) {
        this.numeroRef = numeroRef;
        this.fechaDeVenta = fechaDeVenta;
        this.datosCliente = datosCliente;
        this.totalVenta = totalVenta;
        this.medioDePago = medioDePago;
    }


    public String getNumeroRef() {
        return numeroRef;
    }

    public void setNumeroRef(String numeroRef) {
        this.numeroRef = numeroRef;
    }

    public String getDatosCliente() {
        return datosCliente;
    }

    public void setDatosCliente(String datosCliente) {
        this.datosCliente = datosCliente;
    }

    public LocalDate getFechaDeVenta() {
        return fechaDeVenta;
    }

    public void setFechaDeVenta(LocalDate fechaDeVenta) {
        this.fechaDeVenta = fechaDeVenta;
    }

    public String getMedioDePago() {
        return medioDePago;
    }

    public void setMedioDePago(String medioDePago) {
        this.medioDePago = medioDePago;
    }

    public String getTotalVenta() {
        return totalVenta;
    }

    public void setTotalVenta(String totalVenta) {
        this.totalVenta = totalVenta;
    }
}
