package Model;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.henry.clientesnuevos.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import Tablas.INV;

//import com.example.customizedlist.R;

public class CustomListAdapter extends ArrayAdapter<INV> {

	private ArrayList<INV> listData;
	private Context c;
	private LayoutInflater layoutInflater;
	private Drawable drawableCover;

	public CustomListAdapter(Context context, ArrayList<INV> listData) {
		super(context, 0, listData);
		this.listData = listData;
		layoutInflater = LayoutInflater.from(context);
		c = context;
	}

	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public INV getItem(int position) {
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			Log.i("item", "posicion " + position + " tam: " + getCount() + " Nombre ");
			convertView = layoutInflater
					.inflate(R.layout.list_row_layout, null);
			holder = new ViewHolder();
			holder.headlineView = (TextView) convertView
					.findViewById(R.id.title);
			holder.reporterNameView = (TextView) convertView
					.findViewById(R.id.reporter);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.thumbImage);
			holder.reportedExistencia = (TextView) convertView
					.findViewById(R.id.Existencia);
			convertView.setTag(holder);
			holder.fondo = (RelativeLayout) convertView
					.findViewById(R.id.fondo);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (Variables.getInventario().equals("")){
			holder.inventario = (LinearLayout) convertView
					.findViewById(R.id.LinearLayoutInv);
			holder.inventario.setVisibility(View.GONE);
		}

		INV newsItem = (INV) listData.get(position);

		holder.headlineView.setText(newsItem.getNombre());
		//holder.reportedContados.setText("Contados: " + newsItem.getcontados());
		holder.reportedExistencia.setText("Existencia: " + newsItem.getexistencia_actual());
		String cod = "Cod. " + newsItem.getCodigo();
		if (newsItem.getcontados()!= null && !newsItem.getcontados().equals("")){
			if (newsItem.getexistencia_actual()!=null && !newsItem.getexistencia_actual().equals("")){
				cod += " - Contados: " + newsItem.getcontados();
				if (newsItem.getcontados().equals(newsItem.getexistencia_actual())){
					holder.fondo.setBackgroundColor(Color.GREEN);
				}
				else{
					if(Float.parseFloat(newsItem.getcontados())> Float.parseFloat(newsItem.getexistencia_actual()))
						holder.fondo.setBackgroundColor(Color.rgb(255, 164, 032));
					else{
						holder.fondo.setBackgroundColor(Color.RED);
					}
				}
			}
			else{
				cod += " - Cantidad: " + newsItem.getcontados();
			}
		}
		else{
			holder.fondo.setBackgroundColor(Color.WHITE);
		}
		holder.reporterNameView.setText(cod);
		ImageLoader.getInstance().init(Variables.cargarImagen(c));
		holder.imageView.getLayoutParams().height = 150;
		holder.imageView.getLayoutParams().width = 125;
		holder.imageView.requestLayout();
		if (!(newsItem.getFoto().equals(""))) {
			if (holder.imageView != null) {
				ImageLoader.getInstance().displayImage(newsItem
						.getFoto(), holder.imageView);
				//new ImageDownloaderTask(holder.imageView).execute(newsItem.getFoto());
			}
		} else
			holder.imageView.setImageResource(R.drawable.nologo);

		return convertView;
	}

	static class ViewHolder {
		TextView headlineView;
		TextView reporterNameView;
		TextView reportedDateView;
		ImageView imageView;
		RelativeLayout fondo;
		LinearLayout inventario;
		TextView reportedContados;
		TextView reportedExistencia;
	}
}