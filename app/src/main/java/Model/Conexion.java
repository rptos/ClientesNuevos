package Model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import Tablas.CLI;
import Tablas.GCL;
import Tablas.GRU;
import Tablas.INV;
import Tablas.USR;

/**
 * Created by extre_000 on 05-06-2015.
 */
public class Conexion {
    Context mContext;
    public Conexion(Context mContext) {
        this.mContext = mContext;

    }
    //verifica la conexion de wifi
    public boolean isOnline() {
        try {
            ConnectivityManager cm;
            cm = (ConnectivityManager)
                    mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm.getActiveNetworkInfo() != null) {
                NetworkInfo mWifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                NetworkInfo tresG = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (mWifi.isConnected() || tresG.isConnected())
                {
                    return true;
                }
            }
            else
            {
                return false;
            }
        } catch (Exception e) {

            Log.i("error conexion", "conex: " +e.getMessage());
            e.printStackTrace();

        }
        return false;

    }
    //Correlativos de factura cotizacion o pedido
    public String buscarUltimoCorrelativo(String tipo) {
        if (isOnline()) {
            ArrayList parametros = new ArrayList();
            Post post = new Post();
            parametros.add("valor");
            parametros.add(tipo);
            String datos = post.getServerDataString(parametros, Variables.getDireccion()
                    + "Servicio.svc/correlativo");
            Log.i("buscado", tipo);
            Log.i("sin error", datos);
            return datos.replace("\"","");
        } else {
            Log.i("sin conexion", "Ultimo Correlativo");
        }
        return "";
    }

    //Datos de Usuarios de Mantis
    private ArrayList<USR> parseJSONdataUser_busq(String data)
            throws JSONException {

        Log.i("data", data);
        ArrayList<USR> usr = new ArrayList<USR>();
        JSONArray jsonArray = new JSONArray(data);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject item = jsonArray.getJSONObject(i);
            Log.i("item", "-" + item);
            USR usuario = new USR();
            usuario.setPk(Integer.parseInt(item.getString("usr_pk")));
            usuario.setAlias(item.getString("usr_lanid").trim());
            usr.add(i, usuario);
        }
        Log.i("prod", String.valueOf(usr.size()));
        return usr;
    }
    public ArrayList<USR> sincronizar_User(String usr, String clave) {
        try {
            if (isOnline()) {
                ArrayList parametros = new ArrayList();
                Post post = new Post();
                parametros.add("usr_lanid");
                parametros.add(usr);
                parametros.add("usr_password");
                parametros.add(clave);
                String datos = post.getServerDataString(parametros, Variables.getDireccion()
                        + "Servicio.svc/User");
                Log.i("data", Variables.getDireccion() + "datos" + datos);
                return  parseJSONdataUser_busq(datos);
            } else {
                Log.i("sin conexion", Variables.getDireccion()
                        + "Servicio.svc/User");
            }
        } catch (JSONException e) {

            Log.i("error", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    //Datos de Usuarios de Mantis

    //Sincronizar Grupos clientes cuentas por cobrar
    private ArrayList<GCL> parseJSONdataGrupoCli(String data)
            throws JSONException {
        ArrayList<GCL> gru = new ArrayList<GCL>();
        JSONArray jsonArray = new JSONArray(data);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject item = jsonArray.getJSONObject(i);
            GCL grupo = new GCL();
            grupo.setPk(Integer.parseInt(item.getString("GCL_PK")));
            grupo.setCodigo(item.getString("GCL_CODIGO").trim());
            grupo.setNombre(item.getString("GCL_NOMBRE").trim());
            gru.add(i, grupo);
        }
        return gru;
    }
    public ArrayList<GCL> sincronizar_GRUPO_cli() throws JSONException {
        if (isOnline()) {
            ArrayList parametros = new ArrayList();
            Post post = new Post();
            String datos = post.getServerDataString(parametros, Variables.getDireccion()
                    + "Servicio.svc/GrupoxCliente");
            try {
                return parseJSONdataGrupoCli(datos);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.i("sin conexion", "inv_busq2.php");
        }
        return new ArrayList<GCL>();
    }
    //Sincronizar Grupos clientes cuentas por cobrar
    //Sincronizar Clientes
    private ArrayList<CLI> parseJSONdataCli(String data)
            throws JSONException {
        ArrayList<CLI> cli = new ArrayList<CLI>();
        JSONArray jsonArray = new JSONArray(data);
        String pk = "";
        int j = 0;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject item = jsonArray.getJSONObject(i);
            CLI cliente = new CLI();
            if(!pk.equals(item.getString("CLI_PK").trim())) {
                cliente.setPk(Integer.parseInt(item.getString("CLI_PK")));
                cliente.setCodigo(item.getString("CLI_CODIGO").trim());
                cliente.setNombre(item.getString("CLI_NOMBRE").trim());
                /*if (!Variables.getGruPK().equals("0"))
                    cliente.setSaldo(item.getString("CLI_SALDO").trim());
                else*/
                try {
                    cliente.setEmail(item.getString("CLI_EMAIL").trim());
                }
                catch (Exception x){}
                try {
                    cliente.setDir(item.getString("CLI_DIR").trim());
                }
                catch (Exception x){}
                try {
                    cliente.setGrupo(item.getString("CLI_GCLFK").trim());
                }
                catch (Exception x){}
                try {
                    cliente.setObs(item.getString("CLI_OBS").trim());
                }
                catch (Exception x){}
                try {
                    cliente.setTelefono(item.getString("CLI_TELEFONO").trim());
                }
                catch (Exception x){}
                try {
                    cliente.setTam(item.getString("CLI_TAMANO").trim());
                }
                catch (Exception x){}
                try {
                    cliente.setFoto(item.getString("CLI_FOTO").trim());
                }
                catch (Exception x){}
                cli.add(j, cliente);
                j++;
                pk = item.getString("CLI_PK").trim();
            }
            else{
                cli.get(j-1).setSaldo(Float.toString(Float.parseFloat(cli.get(j-1).getSaldo().replace(",",".")) + Float.parseFloat(item.getString("CLI_SALDO").trim().replace(",","."))));
            }
        }
        return cli;
    }
    public ArrayList<CLI> sincronizar_cli(String pk) throws JSONException {
        if (isOnline()) {
            ArrayList parametros = new ArrayList();
            Post post = new Post();
            String datos = post.getServerDataString(parametros, Variables.getDireccion()
                    + "Servicio.svc/ClienteN/" + pk);
            Log.i("sin conexion", Variables.getDireccion()
                    + "Servicio.svc/ClienteN/" + pk + datos);
            try {
                return parseJSONdataCli(datos);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.i("sin conexion", "inv_busq2.php");
        }
        return new ArrayList<CLI>();
    }
    public ArrayList<CLI> sincronizar_cli(String pk, String buscar) throws JSONException {
        if (isOnline()) {
            ArrayList parametros = new ArrayList();
            Post post = new Post();
            parametros.add("valor");
            parametros.add(buscar);
            String datos = post.getServerDataString(parametros, Variables.getDireccion()
                    + "Servicio.svc/Cliente/" + pk);
            try {
                return parseJSONdataCli(datos);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.i("sin conexion", "inv_busq2.php");
        }
        return new ArrayList<CLI>();
    }

    public ArrayList<CLI> sincronizar_cli_nuevos(String pk) throws JSONException {
        if (isOnline()) {
            ArrayList parametros = new ArrayList();
            Post post = new Post();
            String datos = post.getServerDataString(parametros, Variables.getDireccion()
                    + "Servicio.svc/ClienteN/" + pk);
            Log.i("sin conexion", Variables.getDireccion()
                    + "Servicio.svc/ClienteN/" + pk + datos);
            try {
                return parseJSONdataCli(datos);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.i("sin conexion", "inv_busq2.php");
        }
        return new ArrayList<CLI>();
    }
    public ArrayList<CLI> sincronizar_cli_nuevos(String pk, String buscar) throws JSONException {
        if (isOnline()) {
            ArrayList parametros = new ArrayList();
            Post post = new Post();
            parametros.add("valor");
            parametros.add(buscar);
            String datos = post.getServerDataString(parametros, Variables.getDireccion()
                    + "Servicio.svc/ClienteN/" + pk);
            try {
                return parseJSONdataCli(datos);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.i("sin conexion", "inv_busq2.php");
        }
        return new ArrayList<CLI>();
    }

    public String guardar_cli_nuevo(String rif, String nom,
                                    String tel, String dir,
                                    String obs, String mail, String tamano, String foto) throws JSONException {
        if (isOnline()) {
            ArrayList parametros = new ArrayList();
            Post post = new Post();
            parametros.add("CLN_RIF");
            parametros.add(rif);
            parametros.add("CLN_NOMBRE");
            parametros.add(nom);
            parametros.add("CLN_GCLFK");
            parametros.add(Variables.getGruPK());
            parametros.add("CLN_TEL");
            parametros.add(tel);
            parametros.add("CLN_DIR");
            parametros.add(dir);
            parametros.add("CLN_OBS");
            parametros.add(obs);
            parametros.add("CLN_EMAIL");
            parametros.add(mail);
            parametros.add("CLN_TAMANO");
            parametros.add(tamano);
            parametros.add("CLN_FOTO");
            parametros.add(foto);
            if (!Variables.getCliPk().equals("")){
                parametros.add("CLN_PK");
                parametros.add(Variables.getCliPk());
            }
            String datos = post.getServerDataString(parametros, Variables.getDireccion()
                    + "Servicio.svc/GuardarNuevoCliente");
            Log.i("sin conexion", Variables.getDireccion()
                    + "Servicio.svc/GuardarNuevoCliente" + datos);
            String[] temp = datos.replace("\"","").split("-");
            if(Variables.isNumeric(temp[1].replace("\n","")))
                Variables.setCliPk(temp[1].replace("\n",""));
            return temp[0];
        } else {
            Log.i("sin conexion", "inv_busq2.php");
        }
        return "";
    }
    //Sincronizar Clientes

    //Enviar pago
    public String enviar_pago(int pk, String saldo, String mensaje) throws JSONException {
        if (isOnline()) {
            ArrayList parametros = new ArrayList();
            Post post = new Post();
            parametros.add("CPA_CLIFK");
            parametros.add(String.valueOf(pk));
            parametros.add("CPA_MENSAJE");
            parametros.add(mensaje);
            parametros.add("CPA_MONTO");
            parametros.add(saldo);
            String datos = post.getServerDataString(parametros, Variables.getDireccion()
                    + "Servicio.svc/CuentasPagadas");
            return datos.replace("\"","");
        } else {
            Log.i("sin conexion", "inv_busq2.php");
        }
        return "";
    }
    //Enviar pago

    //Enviar cuentas por cobrar
    public String enviar_CXC(String pk) throws JSONException {
        if (isOnline()) {
            ArrayList parametros = new ArrayList();
            Post post = new Post();
            String datos = post.getServerDataString(parametros, Variables.getDireccion()
                    + "Servicio.svc/EnviarCXC/" + pk);
            return datos.replace("\"","");
        } else {
            Log.i("sin conexion", "inv_busq2.php");
        }
        return "";
    }
    //Enviar cuentas por cobrar

    //Detalle Grupo de productos
    private ArrayList<GRU> parseJSONdataGru(String data)
            throws JSONException {
        ArrayList<GRU> cxc = new ArrayList<GRU>();
        JSONArray jsonArray = new JSONArray(data);
        GRU temp = new GRU();
        temp.setNombre("Seleccione una marca");
        temp.setPk(0);
        cxc.add(0,temp);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject item = jsonArray.getJSONObject(i);
            GRU cuantasxcobrar = new GRU();
            cuantasxcobrar.setPk(Integer.parseInt(item.getString("GRU_PK")));
            cuantasxcobrar.setNombre(item.getString("GRU_NOMBRE").trim());
            cxc.add(i+1, cuantasxcobrar);
        }
        return cxc;
    }
    public ArrayList<GRU> sincronizar_GRU() throws JSONException {
        if (isOnline()) {
            ArrayList parametros = new ArrayList();
            Post post = new Post();
            String datos = post.getServerDataString(parametros, Variables.getDireccion()
                    + "Servicio.svc/Grupos");
            try {
                Log.i("Grupos", datos);
                    return parseJSONdataGru(datos);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.i("sin conexion", "inv_busq2.php");
        }
        return new ArrayList<GRU>();
    }
    //Detalle grupo de productos

    //Enviar lista de precios
    public String enviar_ListaPrecios(String valor, String gru, String porcen, String email) throws JSONException {
        if (isOnline()) {
            ArrayList parametros = new ArrayList();
            Post post = new Post();
            parametros.add("valor");
            parametros.add(valor);
            parametros.add("pocentaje");
            parametros.add(porcen);
            parametros.add("cliente");
            if (email.equals(""))
                parametros.add(Variables.getCliPk());
            else
                parametros.add(email);
            parametros.add("asunto");
            parametros.add(Variables.getAsunto());
            parametros.add("msg");
            parametros.add(Variables.getMsg());
            if (Variables.getMasVendido()){
                parametros.add("masVendido");
                parametros.add("True");
            }
            String datos = post.getServerDataString(parametros, Variables.getDireccion()
                    + "Servicio.svc/EnviarCorreo/" + gru);
            return datos.replace("\"","");
        } else {
            Log.i("sin conexion", "inv_busq2.php");
        }
        return "";
    }
    //Enviar lista de precios
    //Inventario
    private ArrayList<INV> parseJSONdata_INV(String data)
            throws JSONException, UnsupportedEncodingException {

        Log.i("data", data);
        ArrayList<INV> prod = new ArrayList<INV>();
        JSONArray jsonArray = new JSONArray(data);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject item = jsonArray.getJSONObject(i);
            INV producto = new INV();
            producto.setPk(item.getInt("INV_PK"));
            producto.setCodigo(item.getString("INV_CODIGO").trim());
            producto.setNombre(new String(item.getString("INV_NOMBRE").getBytes("ISO-8859-1"), "UTF-8"));
            producto.setainPk(item.getString("AIN_PK").trim());
            producto.setalmacen(item.getString("AIN_ALMFK").trim());
            producto.setubi1(item.getString("AIN_UBI1").trim());
            producto.setubi2(item.getString("AIN_UBI2").trim());
            producto.setubi3(item.getString("AIN_UBI3").trim());
            producto.setubi4(item.getString("AIN_UBI4").trim());
            producto.setubi5(item.getString("AIN_UBI5").trim());
            producto.setubi6(item.getString("AIN_UBI6").trim());
            try{
                if (item.getString("CONTADOS").trim()!="null"){
                    producto.setcontados(item.getString("CONTADOS").trim());
                }
            }
            catch (JSONException e) {

                Log.i("error", e.getMessage());
                e.printStackTrace();
            }
            try{
                if (item.getString("EXISTENCIA_ACTUAL").trim()!="null") {
                    producto.setexistencia_actual(item.getString("EXISTENCIA_ACTUAL").trim());
                }
            }
            catch (JSONException e) {

                Log.i("error", e.getMessage());
                e.printStackTrace();
            }

            producto.setExistencia(Float.valueOf(item.getString(
                    "INV_EXISTENCIA").trim()));
            // producto.setPrecio(Float.valueOf(item.getString("PRECIO").trim()));
            if (!(item.getString("INV_FOTO").equals("")))
                producto.setFoto(Variables.getDireccion_fotos()
                        + item.getString("INV_FOTO").trim() + "&width=250");
            prod.add(i, producto);
            Log.i("msj", "pk " + item.getInt("INV_PK"));
            Log.i("foto", Variables.getDireccion_fotos() + item.getString("INV_FOTO").trim()
                    + "&width=250");

        }
        return prod;
    }
    public ArrayList<INV> buscar_INV_GRU(String valor, String gru) {
        try {
            if (isOnline()) {
                ArrayList parametros = new ArrayList();
                Post post = new Post();
                parametros.add("valor");
                parametros.add(valor);
                if (!Variables.getInventario().equals("")){
                    parametros.add("valor_inv");
                    parametros.add(Variables.getInventario());
                }
                String datos = post.getServerDataString(parametros, Variables.getDireccion()
                        + "Servicio.svc/ProductosGru/" + gru);
                Log.i("buscado", valor);
                Log.i("sin error", "Busqueda de Inventario");
                return parseJSONdata_INV(datos);
            } else {
                Log.i("sin conexion", "Busqueda de Inventario");
            }
        } catch (JSONException e) {

            Log.i("error", e.getMessage());
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
    //Inventario
    //Verificar Rif
    //Enviar cuentas por cobrar
    public String verificar_rif(String valor) throws JSONException {
        if (isOnline()) {
            ArrayList parametros = new ArrayList();
            Post post = new Post();
            parametros.add("valor");
            parametros.add(valor);
            String datos = post.getServerDataString(parametros, Variables.getDireccion()
                    + "Servicio.svc/VerificarRif");
            return datos.replace("\"","");
        } else {
            Log.i("sin conexion", "Servicio.svc/VerificarRif");
        }
        return "";
    }
    //Enviar cuentas por cobrar
}
