package Model;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by extre_000 on 05-06-2015.
 */
public class Variables extends Application {
    private static String bd = "Remoto";
    private static String direccion;
    private static String direccion_fotos;
    private static String url = "rptoscoreanos.myq-see.com";//http://rptoscoreanos.myq-see.com192.168.1.250
    private static String puerto = "3535";
    private static String fac = "";
    private static String ped = "";
    private static String cot = "";
    private static String com = "";
    private static String inventario = "";
    private static String anio = "";
    private static String mes = "";
    private static String audi = "";
    private static String user="";
    private static String pk = "";
    private static String mensaje_pk = "";
    private static String mensaje = "";
    private static String cliPk = "";
    private static String gruPK = "";
    private static String tituloVentana = "";
    private static String asunto = "";
    private static String msg = "";
    private static String emailCliN = "";
    private static boolean masVendido = false;

    public static String getDireccion() {
        return direccion;
    }

    public static void setDireccion() {
        Variables.direccion = "http://"+Variables.getUrl()+":"+Variables.getPuerto()+"/";
    }

    public static String getDireccion_fotos() {
        return direccion_fotos = "http://"+Variables.getUrl()+":8080/catalogo_smith/image.php?image=";
    }

    public static void setDireccion_fotos() {
        Variables.direccion_fotos = direccion_fotos;
    }

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        Variables.url = url;
    }

    public static String getPuerto() {
        return puerto;
    }

    public static void setPuerto(String puerto) {
        Variables.puerto = puerto;
    }

    public static String getBd() {
        return bd;
    }

    public static void setBd(String bd) {
        Variables.bd = bd;
    }

    public static String getFac() {
        return fac;
    }

    public static void setFac(String fac) {
        Variables.fac = fac;
    }

    public static String getPed() {
        return ped;
    }

    public static void setPed(String ped) {
        Variables.ped = ped;
    }

    public static String getCot() {
        return cot;
    }

    public static void setCot(String cot) {
        Variables.cot = cot;
    }

    public static String getCom() {
        return com;
    }

    public static void setCom(String com) {
        Variables.com = com;
    }

    public static String getInventario() {
        return inventario;
    }

    public static void setInventario(String inventario) {
        Variables.inventario = inventario;
    }

    public static String getAnio() {
        return anio;
    }

    public static void setAnio(String anio) {
        Variables.anio = anio;
    }

    public static String getMes() {
        return mes;
    }

    public static void setMes(String mes) {
        Variables.mes = mes;
    }

    public static String agregarCero(int valor){
        if(valor<10){
            return "0"+valor;
        }
        else {
            return String.valueOf(valor);
        }
    }

    public static String getAudi() {
        return audi;
    }

    public static void setAudi(String audi) {
        Variables.audi = audi;
    }

    public static String getUser() {
        if (user==null)
            user="";
        return user;
    }

    public static void setUser(String user) {
        Variables.user = user;
    }

    public static String getPk() {
        return pk;
    }

    public static void setPk(String pk) {
        Variables.pk = pk;
    }

    public static String getMensaje_pk() {
        return mensaje_pk;
    }

    public static void setMensaje_pk(String mensaje_pk) {
        Variables.mensaje_pk = mensaje_pk;
    }

    public static void guardarUsuario(Context context, int pk, String alias)
    {
        /*SharedPreferences settings = getSharedPreferences("perfil", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();*/
        if (context == null)
            throw new RuntimeException("Context is null, what are you doing?");

        SharedPreferences sp = context.getSharedPreferences("perfil",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("pk", String.valueOf(pk));
        editor.putString("alias", String.valueOf(alias));
        setUser(String.valueOf(alias));
        setPk(String.valueOf(pk));
        editor.commit();

    }
    public static ImageLoaderConfiguration cargarImagen(Context c){
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(c)
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        return config;
    }

    public static String getMensaje() {
        return mensaje;
    }

    public static void setMensaje(String mensaje) {
        Variables.mensaje = mensaje;
    }

    public static String getCliPk() {
        return cliPk;
    }

    public static void setCliPk(String cliPk) {
        Variables.cliPk = cliPk;
    }

    public static String getGruPK() {
        return gruPK;
    }

    public static void setGruPK(String gruPK) {
        Variables.gruPK = gruPK;
    }

    public static String getTituloVentana() {
        return tituloVentana;
    }

    public static void setTituloVentana(String tituloVentana) {
        Variables.tituloVentana = tituloVentana;
    }

    public static String getAsunto() {
        return asunto;
    }

    public static void setAsunto(String asunto) {
        Variables.asunto = asunto;
    }

    public static String getMsg() {
        return msg;
    }

    public static void setMsg(String msg) {
        Variables.msg = msg;
    }

    public static String getEmailCliN() {
        return emailCliN;
    }

    public static void setEmailCliN(String emailCliN) {
        Variables.emailCliN = emailCliN;
    }

    public static boolean getMasVendido() {
        return masVendido;
    }

    public static void setMasVendido(boolean masVendido) {
        Variables.masVendido = masVendido;
    }

    public static boolean isNumeric(String cadena){
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe){
            return false;
        }
    }
}
