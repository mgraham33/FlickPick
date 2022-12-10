package com.flickpick.volleyexperiment.ui.gallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.flickpick.volleyexperiment.Controller;
import com.flickpick.volleyexperiment.R;
import com.flickpick.volleyexperiment.databinding.FragmentGalleryBinding;
import com.flickpick.volleyexperiment.util.LruBitmapCache;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class GalleryFragment extends Fragment {
	String url = "https://m.media-amazon.com/images/M/MV5BNTg2MDhhYzQtNDZjNi00MjRmLTg3NWYtMzg2ZGJkYmI2MmE0XkEyXkFqcGdeQXVyNjUxMDQ0MTg@._V1_QL75_UY562_CR17,0,380,562_.jpg";
	private FragmentGalleryBinding binding;
	private NetworkImageView imgNetWorkView;
	
	public View onCreateView(@NonNull LayoutInflater inflater,
							 ViewGroup container, Bundle savedInstanceState) {
		GalleryViewModel galleryViewModel =
			new ViewModelProvider(this).get(GalleryViewModel.class);
		
		binding = FragmentGalleryBinding.inflate(inflater, container, false);
		View root = binding.getRoot();
		imgNetWorkView = binding.imageView2;
		
		final TextView textView = binding.textGallery;
		
		
		RequestQueue q = Controller.getInstance().getRequestQueue();
		ImageLoader l = new ImageLoader(q, new LruBitmapCache());
		imgNetWorkView.setImageUrl(url, l);
		l.get(url, ImageLoader.getImageListener(imgNetWorkView, R.drawable.ic_loading, R.drawable.ic_baseline_broken_image_24));
		//No clue what the cache does
		//Cache cache = q.getCache();
		//Cache.Entry e = cache.get(url);
		//if (e != null) {
		//	Bitmap bmp = BitmapFactory.decodeByteArray(e.data, 0, e.data.length);
		//	binding.imageView3.setImageBitmap(bmp);
		//} else {
		//
		//}
		galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
		return root;
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}
}