package com.henry.clientesnuevos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import Model.Conexion;
import Model.CustomListAdapter;
import Model.Variables;
import Tablas.GRU;
import Tablas.INV;

public class ConsultaListaPrecios extends Fragment {
    View rootView;
    Context c;
    Conexion s;
    ArrayList<GRU> NavItms = new ArrayList<GRU>();
    ArrayList<INV> invent = new ArrayList<INV>();
    ListView listview;
    EditText porcen;
    EditText correo;
    String enviado;

    public ConsultaListaPrecios() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_consulta_lista_precios, container, false);
        c = (Context)getActivity();
        s = new Conexion(c);
        Variables.setTituloVentana("ConsultaListaPrecios");
        ((MainActivity) getActivity()).setActionBarTitle("Consultar y enviar lista de precios");
        new Grupo().execute("");
        listview = (ListView) rootView.findViewById(R.id.listView2);
        porcen = (EditText) rootView.findViewById(R.id.editTextPorcen);
        correo = (EditText) rootView.findViewById(R.id.editTextCorreo);
        if (!Variables.getEmailCliN().equals("")){
            correo.setText(Variables.getEmailCliN());
        }
        MainActivity.ocultar();
        ImageButton buscar = (ImageButton) rootView.findViewById(R.id.buttonBuscar);
        ImageButton email = (ImageButton) rootView.findViewById(R.id.buttonEmail);
        final android.widget.SearchView search = (android.widget.SearchView) rootView.findViewById(R.id.searchView2);

        search.setQueryHint("Buscar Productos");

        search.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub

                new BuscarInv().execute(query);
                Toast.makeText(c, query,
                        Toast.LENGTH_SHORT).show();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO Auto-generated method stub

                /*Toast.makeText(c, newText,
                Toast.LENGTH_SHORT).show();*/
                return false;
            }
        });
        buscar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new BuscarInv().execute(String.valueOf(search.getQuery()));
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(c);
                builder.setTitle("Tomar Foto");
                final CharSequence colors[] = new CharSequence[] {"Enviar Productos", "Enviar mas vendidos"};
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Variables.setMasVendido(false);
                                break;
                            case 1:
                                Variables.setMasVendido(true);
                                break;
                        }
                    }
                });
                builder.show();
                new EnviarEmail().execute(String.valueOf(search.getQuery()),porcen.getText().toString(), correo.getText().toString());
            }
        });

        return rootView;
    }

    private class Grupo extends AsyncTask<String, Float, Integer> {
        ProgressDialog dialog;

        protected void onPreExecute() { // Mostramos antes de comenzar
            dialog = ProgressDialog.show(getActivity(), "", "Consultando grupos de productos...", true);
        }

        protected Integer doInBackground(String... params) {
            try {
                NavItms = s.sincronizar_GRU();
                if (NavItms!= null)
                {
                    return 1;
                }
                else
                    return 0;
            } catch (Exception e) {
                Log.i("error_grupo", "-"+e.getLocalizedMessage());
                e.printStackTrace();
                return 0;
            }
        }

        protected void onProgressUpdate(Float... valores) {
            dialog.dismiss();
        }

        protected void onPostExecute(Integer bytes) {
            dialog.dismiss();
            if (bytes==1) {
                try {
                    if (NavItms!=null)
                    {
                        String array_spinner[]=new String[NavItms.size()];
                        for (int i = 0; i<NavItms.size(); i++){
                            array_spinner[i] = NavItms.get(i).getNombre();
                        }
                        final Spinner auditoria = (Spinner) rootView.findViewById(R.id.spinnerGrupo);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, array_spinner);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        auditoria.setAdapter(adapter);
                        auditoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                                Variables.setGruPK(String.valueOf(NavItms.get(auditoria.getSelectedItemPosition()).getPk()));
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> arg0) {
                                // TODO Auto-generated method stub

                            }
                        });
                    }
                } catch (Exception e) {
                    //Log.i("error", e.getMessage());
                }
            }
            else {
                //Log.i("error","Sin Grupo");
            }
        }

    }

    private class BuscarInv extends AsyncTask<String, Float, Integer> {
        ProgressDialog dialog;

        protected void onPreExecute() { // Mostramos antes de comenzar
            dialog = ProgressDialog.show(getActivity(), "", "Consultando productos...", true);
        }

        protected Integer doInBackground(String... params) {
            try {

                String nivel = "1";

                publishProgress();
                invent = s.buscar_INV_GRU(params[0].trim(),Variables.getGruPK());

            } catch (Exception e) {

                e.printStackTrace();
                return 0;
            }
            return 1;
        }

        protected void onProgressUpdate(Float... valores) {
            dialog.dismiss();
        }

        protected void onPostExecute(Integer bytes) {
            dialog.dismiss();
            if ((bytes == 1) && (invent != null)) {

                CustomListAdapter adaptadorGrid = new CustomListAdapter(c, invent);
                listview.setAdapter(adaptadorGrid);
            }
        }
    }
    private class EnviarEmail extends AsyncTask<String, Float, Integer> {
        ProgressDialog dialog;

        protected void onPreExecute() { // Mostramos antes de comenzar
            dialog = ProgressDialog.show(getActivity(), "", "Enviando correo...", true);
        }

        protected Integer doInBackground(String... params) {
            try {

                String nivel = "1";
                publishProgress();
                enviado = s.enviar_ListaPrecios(params[0].trim(),Variables.getGruPK(),params[1].trim(), params[2].trim());

            } catch (Exception e) {

                e.printStackTrace();
                return 0;
            }
            return 1;
        }

        protected void onProgressUpdate(Float... valores) {
            dialog.dismiss();
        }

        protected void onPostExecute(Integer bytes) {

            if ((bytes == 1) && (invent != null)) {

                alerta_cantidad(enviado);
            }
        }
    }
    //Mensaje de Alerta
    private void alerta_cantidad(String mensaje) {

        AlertDialog alertDialog = new AlertDialog.Builder(c).create();
        alertDialog.setTitle("");
        alertDialog.setMessage(mensaje);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                return;

            }
        });
        alertDialog.show();
    }
    //Mensaje de Alerta
}
