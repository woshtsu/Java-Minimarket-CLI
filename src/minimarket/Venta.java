package minimarket;

import java.util.List;
import java.util.Date;

public class Venta {
    private int id;
    private Cliente cliente;
    private Date fecha;
    private List<Producto> productos;

    public Venta(int id, Cliente cliente, List<Producto> productos) {
        this.id = id;
        this.cliente = cliente;
        this.fecha = new Date();
        this.productos = productos;
    }

    public int getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Date getFecha() {
        return fecha;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public double getMontoTotal() {
        return productos.stream().mapToDouble(Producto::getPrecio).sum();
    }

    @Override
    public String toString() {
        return "Venta{" +
               "id=" + id +
               ", cliente=" + cliente.getNombre() +
               ", fecha=" + fecha +
               ", productos=" + productos +
               ", montoTotal=" + getMontoTotal() +
               '}';
    }
}
