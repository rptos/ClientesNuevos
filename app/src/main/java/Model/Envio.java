package Model;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

//import com.android.dataframework.DataFramework;
//import com.android.dataframework.Entity;

public class Envio extends Conexion {

	private int proxpk;

	public Envio(Context mContext) {
		super(mContext);

	}



	public String doFileUpload(String pk, String archivo, String dir) {

		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		DataInputStream inStream = null;
		String existingFileName = "";
		String datos="";
		if (dir.equals("")) {
			existingFileName = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/DCIM/Camera/" + archivo;
		}
		else {
			existingFileName = dir;
		}
		if (isOnline()) {
			ArrayList par = new ArrayList();
			Post post = new Post();
			par.add("pk");
			par.add("");
			datos = post.getServerDataStringImagen(par, existingFileName, Variables.getDireccion()
					+ "Servicio.svc/Foto/"+archivo+"/"+pk);

			return (datos.replace("\"",""));

		} else {
			Log.i("sin conexion", "Inventario/Inv.aspx");
		}
		return datos;
	}

	public String doFileUploadDVI(String pk, String archivo, String dir) {

		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		DataInputStream inStream = null;
		String existingFileName = "";
		String datos="";
		if (dir.equals("")) {
			existingFileName = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/DCIM/Camera/" + archivo;
		}
		else {
			existingFileName = dir;
		}
		if (isOnline()) {
			ArrayList par = new ArrayList();
			Post post = new Post();
			par.add("pk");
			par.add("");
			datos = post.getServerDataStringImagen(par, existingFileName, Variables.getDireccion()
					+ "Servicio.svc/SetDivisasF/"+archivo+"/"+pk);

			return (datos.replace("\"",""));

		} else {
			Log.i("sin conexion", "Servicio.svc/SetDivisasF/"+archivo+"/"+pk);
		}
		return datos;
	}

	public String doFileUploadDetalleDVI(String pk, String archivo, String dir) {

		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		DataInputStream inStream = null;
		String existingFileName = "";
		String datos="";
		if (dir.equals("")) {
			existingFileName = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/DCIM/Camera/" + archivo;
		}
		else {
			existingFileName = dir;
		}
		if (isOnline()) {
			ArrayList par = new ArrayList();
			Post post = new Post();
			par.add("pk");
			par.add("");
			datos = post.getServerDataStringImagen(par, existingFileName, Variables.getDireccion()
					+ "Servicio.svc/ImagenMED/"+pk+"/"+archivo);

			return (datos.replace("\"",""));

		} else {
			Log.i("sin conexion", "Servicio.svc/ImagenMED/"+pk+"/"+archivo);
		}
		return datos;
	}

	public String doFileUpload(String archivo,String tipo, String numero,Boolean eliminar, String dir) {

		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		DataInputStream inStream = null;
		String existingFileName = "";
		String datos="";
		if (dir.equals(""))
			existingFileName = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/DCIM/Camera/" + archivo;
		else{
			existingFileName = dir;
		}
		if (isOnline()) {
			ArrayList par = new ArrayList();
			Post post = new Post();
			par.add("pk");
			par.add("");
			datos = post.getServerDataStringImagen(par, existingFileName, Variables.getDireccion()
					+ "Servicio.svc/ImagenFacPedCot/"+tipo+"/"+numero);

			return (datos.replace("\"",""));

		} else {
			Log.i("sin conexion", "Inventario/Inv.aspx");
		}
		return datos;
	}
}
