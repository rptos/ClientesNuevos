package Model;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.henry.clientesnuevos.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import Tablas.CLI;

/**
 * Created by extre_000 on 15/4/2016.
 */
public class ListaClienteAdapter extends BaseAdapter {
    private Context context;
    private List<CLI> items;

    public ListaClienteAdapter(Context context, List<CLI> items) {
        //super(context, 0, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class Fila
    {
        TextView titulo_itm;
        TextView codigo;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        Fila view;
        LayoutInflater inflator = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        CLI item=items.get(position);
        Log.i("item","posicion "+position+" tam: "+getCount()+ " Nombre ");
        DecimalFormatSymbols simbolo=new DecimalFormatSymbols();
        simbolo.setDecimalSeparator(',');
        simbolo.setGroupingSeparator('.');
        DecimalFormat formateador = new DecimalFormat("###,###.##",simbolo);
        if (convertView == null) {
            view = new Fila();
            convertView = inflator.inflate(R.layout.layout_grupo_cli, null);
            view.titulo_itm = (TextView) convertView.findViewById(R.id.textViewNombre);
            view.codigo = (TextView) convertView.findViewById(R.id.textViewCodigo);
            view.titulo_itm.setText(item.getNombre());
            /*if (!Variables.getGruPK().equals("0"))
                view.codigo.setText(item.getCodigo() + " - " + formateador.format(Float.parseFloat(item.getSaldo().replace(",","."))));
            else*/
                view.codigo.setText(item.getCodigo());
            convertView.setTag(view);

        } else {
            view = (Fila) convertView.getTag();
            view.titulo_itm = (TextView) convertView.findViewById(R.id.textViewNombre);
            view.codigo = (TextView) convertView.findViewById(R.id.textViewCodigo);
            view.titulo_itm.setText(item.getNombre());
            /*if (!Variables.getGruPK().equals("0"))
                view.codigo.setText(item.getCodigo() + " - " + formateador.format(Float.parseFloat(item.getSaldo().replace(",","."))));
            else*/
                view.codigo.setText(item.getCodigo());
        }

        //Setear la imagen desde el recurso drawable

        return convertView;
    }
}
