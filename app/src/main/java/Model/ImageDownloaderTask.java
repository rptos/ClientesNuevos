package Model;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.henry.clientesnuevos.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import java.io.InputStream;
import java.lang.ref.WeakReference;

//import com.example.customizedlist.R;

public class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
	private final WeakReference<ImageView> imageViewReference;
	private Bitmap imagen;

	public ImageDownloaderTask(ImageView imageView) {
		imageViewReference = new WeakReference<ImageView>(imageView);
	}

	@Override
	// Actual download method, run in the task thread
	protected Bitmap doInBackground(String... params) {
		// params comes from the execute() call: params[0] is the url.
		try {
			// imagen=downloadBitmap(params[0]);
			return downloadBitmap(params[0]);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	// Once the image is downloaded, associates it to the imageView
	protected void onPostExecute(Bitmap bitmap) {
		if (isCancelled()) {
			bitmap = null;
		}

		if (imageViewReference != null) {
			ImageView imageView = imageViewReference.get();
			if (imageView != null) {

				if (bitmap != null) {
					imageView.setImageBitmap(getRoundedCornerBitmap(bitmap,
							false));
				} else {
					imageView.setImageDrawable(imageView.getContext()
							.getResources().getDrawable(R.drawable.nologo));
				}
			}

		}
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, boolean square) {
		int width = 0;
		int height = 0;

		// Bitmap bitmap = drawable.getBitmap() ;

		if (square) {
			if (bitmap.getWidth() < bitmap.getHeight()) {
				width = bitmap.getWidth();
				height = bitmap.getWidth();
			} else {
				width = bitmap.getHeight();
				height = bitmap.getHeight();
			}
		} else {
			height = bitmap.getHeight();
			width = bitmap.getWidth();
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, width, height);
		final RectF rectF = new RectF(rect);
		final float roundPx = 90;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	static Bitmap downloadBitmap(String url) {
		Log.i("url", url);

		final AndroidHttpClient client = AndroidHttpClient
				.newInstance("Android");
		HttpGet getRequest = null;
		try {
			getRequest = new HttpGet(url);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (getRequest != null) {
			try {
				HttpResponse response = client.execute(getRequest);
				final int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK) {
					Log.w("ImageDownloader", "Error " + statusCode
							+ " while retrieving bitmap from " + url);
					return null;
				} else {
					final HttpEntity entity = response.getEntity();
					if (entity != null) {
						InputStream inputStream = null;
						try {
							inputStream = entity.getContent();
							final Bitmap bitmap = BitmapFactory
									.decodeStream(inputStream);
							return bitmap;
						} finally {
							if (inputStream != null) {
								inputStream.close();
							}
							entity.consumeContent();
						}
					}
				}
			} catch (Exception e) {
				// Could provide a more explicit error message for IOException
				// or
				// IllegalStateException
				getRequest.abort();
				Log.w("ImageDownloader", "Error while retrieving bitmap from "
						+ url);
			} finally {
				if (client != null) {
					client.close();
				}
			}
		}
		return null;
	}

}