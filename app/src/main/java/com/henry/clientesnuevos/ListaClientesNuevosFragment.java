package com.henry.clientesnuevos;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

import Model.Conexion;
import Model.ListaClienteAdapter;
import Model.Variables;
import Tablas.CLI;


public class ListaClientesNuevosFragment extends Fragment {

    View rootView;
    Context c;
    Conexion s;
    ArrayList<CLI> NavItms = new ArrayList<CLI>();
    ListView listview;

    public ListaClientesNuevosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_lista_clientes_nuevos, container, false);
        c = (Context)getActivity();
        s = new Conexion(c);
        Variables.setTituloVentana("ListaClientesNuevos");
        ((MainActivity) getActivity()).setActionBarTitle("Lista de clientes nuevos");
        listview = (ListView) rootView.findViewById(R.id.listViewGrupo);
        Variables.setEmailCliN("");
        new Clientes().execute("");

        ImageButton r = (ImageButton) rootView.findViewById(R.id.imageButtonRefresh);
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Clientes().execute("");
            }
        });

        SearchView search = (SearchView) rootView.findViewById(R.id.searchView);

        search.setQueryHint("Buscar Cliente");

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub

                new Clientes().execute(query);
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

        listview = (ListView) rootView.findViewById(R.id.listViewGrupo);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                Log.i("posicion", "posicion " + position);
                final CLI posActual = NavItms.get(position);
                Variables.setCliPk(String.valueOf(posActual.getPk()));
                Variables.setEmailCliN(posActual.getEmail());
                String pk = Variables.getCliPk();
                final CharSequence colors[] = new CharSequence[]{"Editar", "Lista de precios"};

                AlertDialog.Builder builder = new AlertDialog.Builder(c);
                builder.setTitle("Proveedor " + posActual.getNombre());
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //colors[which];
                        switch (which) {
                            case 0:
                                CrearUsuarioNuevoFragment fragment2 = new CrearUsuarioNuevoFragment();
                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.content_frame, fragment2);
                                fragmentTransaction.commit();
                                break;
                            case 1:
                                ConsultaListaPrecios fragment = new ConsultaListaPrecios();
                                FragmentManager fragmentManager1 = getFragmentManager();
                                FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                                fragmentTransaction1.replace(R.id.content_frame, fragment);
                                fragmentTransaction1.commit();
                                break;
                        }
                    }
                });
                builder.show();
            }
        });

        return rootView;
    }

    private class Clientes extends AsyncTask<String, Float, Integer> {
        ProgressDialog dialog;

        protected void onPreExecute() { // Mostramos antes de comenzar
            dialog = ProgressDialog.show(getActivity(), "", "Consultando Clientes...", true);
        }

        protected Integer doInBackground(String... params) {
            try {
                String pk = "0";
                if (params[0].equals(""))
                    NavItms = s.sincronizar_cli_nuevos(pk);
                else
                    NavItms = s.sincronizar_cli_nuevos(pk, params[0]);
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
            /*if (!verificar_internet()) {
                //dialog.dismiss();
            }*/
        }

        protected void onPostExecute(Integer bytes) {
            dialog.dismiss();
            if (bytes==1) {
                try {
                    if (NavItms!=null)
                    {
                        ListaClienteAdapter adaptadorGrid = new ListaClienteAdapter(c, NavItms);
                        listview.setAdapter(adaptadorGrid);
                    }
                } catch (Exception e) {
                    Log.i("error", e.getMessage());
                }
            }
            else {
                Log.i("error","Sin Grupo");
            }
        }

    }

}
