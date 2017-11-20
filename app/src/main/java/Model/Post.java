package Model;

import android.graphics.Bitmap;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by extre_000 on 05-06-2015.
 */
public class Post {
    private InputStream is = null;
    private String respuesta = "";
    private boolean band = true;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void conectaPost(ArrayList parametros, String URL) {
        ArrayList nameValuePairs;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(URL);
            nameValuePairs = new ArrayList();
            httppost.setHeader(HTTP.CONTENT_TYPE,
                    "image/jpeg;charset=UTF-8");
            if (parametros != null) {
                JSONObject jsonObject = new JSONObject();
                for (int i = 0; i < parametros.size() - 1; i += 2) {
                    jsonObject.accumulate((String)parametros.get(i), (String)parametros.get(i+1));
                    /*nameValuePairs.add(new BasicNameValuePair(
                            (String) parametros.get(i), (String) parametros
                            .get(i + 1)));*/

                }
                //new String(jsonObject.getString("messages").getBytes("ISO-8859-1"), "UTF-8");
                String json = new String(jsonObject.toString().getBytes("UTF-8"), "ISO-8859-1");//jsonObject.toString();//httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                Log.e("log_json", "Error in http connection " + json);
                StringEntity se = new StringEntity(json);
                httppost.setEntity(se);
            }
            httppost.setHeader("Accept", "application/json");
            httppost.setHeader("Content-type", "application/json");
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch (Exception e) {
            if (e.toString().contains("refused") && band) {
                band = false;
                String temp = URL.replace("rptoscoreanos.myq-see.com", "192.168.1.250");
                conectaPost(parametros, temp);
            }
            Log.e("log_tag", "Error in http connection " + e.toString());
        } finally {
        }
    }

    private void conectaPostImagen(ArrayList parametros, String imagen, String URL) {
        ArrayList nameValuePairs;
        //conectaPost(parametros,URL);
        Log.i("dir", "-" + imagen);
        File f = new File(imagen);
        //OutputStream os = new BufferedOutputStream(new FileOutputStream(f));
        Bitmap bitmap = BitmapRe.decodeFile(f,800,600);
        //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
        byte[] data = bos.toByteArray();
        StringBuilder s;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(URL);
            nameValuePairs = new ArrayList();
            /*httppost.addHeader("Content-Type" , "image/jpeg");
            httppost.addHeader("Encoding", "image/jpeg");
            httppost.setHeader(HTTP.CONTENT_TYPE,
                    "image/jpeg;charset=UTF-8");*/

            if (parametros != null) {
                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                Log.e("log_tag", "Error in http connection " + imagen + " " + URL);

                try {
                    //FileBody file = new FileBody(f);
                    //builder.addPart("uploadedfile", file);
                    /*String[] temp = URL.split("/");
                    builder.addBinaryBody("uploadedfile", f);*/
                    //builder.addBinaryBody("uploadedfile",file.getInputStream());
                    httppost.setEntity(new ByteArrayEntity(data));
                    //HttpEntity entity = builder.build();
                    //httppost.setEntity(entity);
                }
                catch (Exception ex) {
                    Log.e("postVerify 4", "you got error " + ex.getMessage());
                }
            }
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
        } finally {
        }
    }

    private void getRespuestaPost() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "utf-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            respuesta = sb.toString();
            Log.e("log_tag", "Cadena JSon " + respuesta);
        } catch (Exception e) {
            Log.e("log_tag", "Error converting result " + e.toString());
        }
    }

    @SuppressWarnings("finally")
    private JSONArray getJsonArray() {
        JSONArray jArray = null;
        try {
            jArray = new JSONArray(respuesta);
        } catch (Exception e) {
        } finally {
            return jArray;
        }
    }

    @SuppressWarnings("rawtypes")
    public JSONArray getServerData(ArrayList parametros, String URL) {
        conectaPost(parametros, URL);
        if (is != null) {
            getRespuestaPost();
        }
        if (respuesta != null && respuesta.trim() != "") {
            return getJsonArray();
        } else {
            return null;
        }
    }

    public String getServerDataString(ArrayList parametros, String URL) {
        conectaPost(parametros, URL);

        return getRespuestaPostString();

    }

    public String getServerDataStringImagen(ArrayList parametros, String imagen, String URL) {
        conectaPostImagen(parametros, imagen, URL);

        return getRespuestaPostString();

    }

    private String getRespuestaPostString() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            respuesta = sb.toString();
            return respuesta;
        } catch (Exception e) {
            Log.e("log_tag", "Error converting result " + e.toString());
        }
        return "";
    }
}
