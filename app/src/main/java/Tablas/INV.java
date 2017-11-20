package Tablas;

/**
 * Created by extre_000 on 05-06-2015.
 */
public class INV {
    int pk;
    String codigo;
    String nombre;
    float existencia;
    String foto;
    String ain_pk;
    String almacen;
    String ubi1;
    String ubi2;
    String ubi3;
    String ubi4;
    String ubi5;
    String ubi6;
    String existencia_actual;
    String contados;

    public String getcontados() {
        return this.contados;
    }

    public void setcontados(String contados) {
        this.contados = contados;
    }

    public String getexistencia_actual() {
        return this.existencia_actual;
    }

    public void setexistencia_actual(String existencia_actual) {
        this.existencia_actual = existencia_actual;
    }

    public String getubi1() {
        return this.ubi1;
    }

    public void setubi1(String ubi) {
        this.ubi1 = ubi;
    }

    public String getubi2() {
        return this.ubi2;
    }

    public void setubi2(String ubi) {
        this.ubi2 = ubi;
    }

    public String getubi3() {
        return this.ubi3;
    }

    public void setubi3(String ubi) {
        this.ubi3 = ubi;
    }

    public String getubi4() {
        return this.ubi4;
    }

    public void setubi4(String ubi) {
        this.ubi4 = ubi;
    }

    public String getubi5() {
        return this.ubi5;
    }

    public void setubi5(String ubi) {
        this.ubi5 = ubi;
    }

    public String getubi6() {
        return this.ubi6;
    }

    public void setubi6(String ubi) {
        this.ubi6 = ubi;
    }

    public String getalmacen() {
        return this.almacen;
    }

    public void setalmacen(String almacen) {
        this.almacen = almacen;
    }

    public String getainPk() {
        return this.ain_pk;
    }

    public void setainPk(String pk) {
        this.ain_pk = pk;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public INV() {
        this.foto = "";
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getExistencia() {
        return existencia;
    }

    public void setExistencia(float existencia) {
        this.existencia = existencia;
    }
}
