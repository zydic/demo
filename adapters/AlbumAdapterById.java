package com.example.foldergallery.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.foldergallery.activity.ImageSelectionActivity;
import com.example.foldergallery.data.ImageData;
import com.example.foldergallery.util.Log;
import com.example.foldergallery.util.TypeFaceUtil;
import com.example.photobook.R;
import com.exampleqwe.foldergallery.MyApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AlbumAdapterById extends
		RecyclerView.Adapter<AlbumAdapterById.Holder> {
	private MyApplication application;
	private ArrayList<String> folderId;
	private LayoutInflater inflater;
	private OnItemClickListner<Object> clickListner;
	RequestManager glide;
	private ImageSelectionActivity activity;
int tempPosition;
	public AlbumAdapterById(Context activity) {
		glide = Glide.with(activity);
		application = MyApplication.getInstance();
		folderId = new ArrayList<>(application.getAllAlbum().keySet());
		this.activity = (ImageSelectionActivity) activity;

		Collections.sort(folderId, new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				return s1.compareToIgnoreCase(s2);
			}
		});
		if (this.activity.isFromCameraNotification) {
			application.setSelectedFolderId("-1739773001");
			this.activity.scrollToPostion(scrollToPos());
			Log.e("DATA","Cam Pos : "+scrollToPos());
		} else {
			application.setSelectedFolderId(folderId.get(0));
		}
		// if (application.getSelectedFolderId() == "") {
		// Log.e("DATA","SELECTED FOLDER : "+application.getSelectedFolderId());

		// application.setSelectedFolderId("-1739773001");//folderId.get(0)

		// }else {
		// Log.e("DATA","SELECTED FOLDER Els : "+application.getSelectedFolderId());
		// application.setSelectedFolderId(application.getSelectedFolderId());
		// }
		inflater = LayoutInflater.from(activity);
	}

	public int scrollToPos() {
		
		for (int i = 0; i < folderId.size(); i++) {
			if (folderId.get(i).equals("-1739773001")) {
				return i;
			}
		}
		return 0;
	}

	public void setOnItemClickListner(OnItemClickListner<Object> clickListner) {
		this.clickListner = clickListner;

	}

	public class Holder extends RecyclerView.ViewHolder {
		ImageView imageView;
		TextView textView;
		View parent;
		ImageView cbSelect;
		private View clickableView;

		public Holder(View v) {
			super(v);
			parent = v;
			cbSelect = (ImageView) v.findViewById(R.id.cbSelect);
			imageView = (ImageView) v.findViewById(R.id.imageView1);
			textView = (TextView) v.findViewById(R.id.textView1);
			clickableView = v.findViewById(R.id.clickableView);
		}

		public void onItemClick(View view, Object item) {
			if (clickListner != null) {
				clickListner.onItemClick(view, item);
			}
		}
	}

	@Override
	public int getItemCount() {
		return folderId.size();
	}

	public String getItem(int pos) {
		return folderId.get(pos);
	}

	@Override
	public void onBindViewHolder(final Holder holder, final int pos) {
		final String currentFolderId = getItem(pos);
		final ImageData data = application.getImageByAlbum(currentFolderId)
				.get(0);
		// Log.e("","Folder Name : "+data.folderName);
		// Log.e("","Folder id : "+folderId.get(pos));
		holder.textView.setTypeface(TypeFaceUtil.TEXTSTYLE.getTypeFace());
		holder.textView.setSelected(true);
		holder.textView.setText(data.folderName);
		glide.load(data.imagePath).into(holder.imageView);
//		holder.cbSelect.setChecked(currentFolderId.equals(application
//				.getSelectedFolderId()));
		if (tempPosition == pos) {
			holder.cbSelect.setVisibility(View.VISIBLE);
			holder.textView.setTextColor(activity.getResources().getColor(R.color.colorAccent));

		} else {

			holder.cbSelect.setVisibility(View.INVISIBLE);
			holder.textView.setTextColor(activity.getResources().getColor(R.color.white));

		}
		holder.clickableView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				application.setSelectedFolderId(currentFolderId);
				if (clickListner != null) {
					clickListner.onItemClick(v, data);
				}
				tempPosition = pos;
				notifyDataSetChanged();
			}
		});

	}

	@Override
	public Holder onCreateViewHolder(ViewGroup parent, int pos) {
		return new Holder(inflater.inflate(R.layout.items, parent, false));
	}
}
