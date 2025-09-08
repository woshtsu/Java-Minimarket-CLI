package minimarket;

public enum Categoria {
    SNACKS("Snacks"),
    BEBIDAS("Bebidas"),
    LACTEOS("Lácteos"),
    CARNES("Carnes"),
    FRUTAS_VERDURAS("Frutas y Verduras"),
    LIMPIEZA("Limpieza"),
    OTROS("Otros");

    private final String nombre;

    Categoria(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
