package com.example.foldergallery.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.foldergallery.util.TypeFaceUtil;
import com.example.photobook.R;
import com.example.foldergallery.activity.ImageSelectionActivity;
import com.example.foldergallery.data.ImageData;
import com.exampleqwe.foldergallery.MyApplication;

public class ImageByAlbumAdapter extends
		RecyclerView.Adapter<ImageByAlbumAdapter.Holder> {
	private MyApplication application;
	private LayoutInflater inflater;
	private OnItemClickListner<Object> clickListner;
	private RequestManager glide;
	private ImageSelectionActivity activity;

	// private boolean isForFirst = false;

	public ImageByAlbumAdapter(Context context) {
		application = MyApplication.getInstance();
		inflater = LayoutInflater.from(context);
		glide = Glide.with(context);
		this.activity = (ImageSelectionActivity) context;
		if (activity.isFromCameraNotification) {
			ImageSelectionActivity.isForFirst = true;
			ImageSelectionActivity.tempImage = application.getSelectedImages();
		}

	}

	public void setOnItemClickListner(OnItemClickListner<Object> clickListner) {
		this.clickListner = clickListner;
	}

	public class Holder extends RecyclerView.ViewHolder {
		ImageView imageView;
		TextView textView;
		View parent;
		View clickableView;

		public Holder(View v) {
			super(v);
			parent = v;
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
		return application.getImageByAlbum(application.getSelectedFolderId())
				.size();
	}

	public ImageData getItem(int pos) {
		return application.getImageByAlbum(application.getSelectedFolderId())
				.get(pos);
	}

	@Override
	public void onBindViewHolder(final Holder holder, final int pos) {
		final ImageData data = getItem(pos);
		holder.textView.setTypeface(TypeFaceUtil.TEXTSTYLE.getTypeFace());
		holder.textView.setSelected(true);
		holder.textView.setText(data.imageCount == 0 ? "" : String.format(
				"%02d", data.imageCount));
		glide.load(data.imagePath).into(holder.imageView);

		holder.textView
				.setBackgroundColor(data.imageCount == 0 ? Color.TRANSPARENT
						: 0x60000000);




		if (ImageSelectionActivity.tempImage.size() == 0) {
			ImageSelectionActivity.isForFirst = false;
		}
		if (activity.isFromCameraNotification
				&& ImageSelectionActivity.isForFirst) {

			for (int j = 0; j < application.getSelectedImages().size(); j++) {
				ImageData newPath = application.getSelectedImages().get(j);
				if (newPath.imagePath.equals(data.imagePath)) {

					holder.textView.setText(String.format("%02d",
							newPath.imageCount));
					holder.textView.setBackgroundColor(activity.getResources().getColor(R.color.Teal_500));
					// data.imagePath = newPath.imagePath;
					// data.imageCount = newPath.imageCount;
					// data.folderName = newPath.folderName;
				}
			}
		}
		holder.clickableView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (holder.imageView.getDrawable() == null) {
					Toast.makeText(application,
							"Image currpted or not support.", Toast.LENGTH_LONG)
							.show();
					return;
				}
				if (activity.isFromCameraNotification) {
					for (int j = 0; j < application.getSelectedImages().size(); j++) {
						ImageData newPath = application.getSelectedImages()
								.get(j);
						if (newPath.imagePath.equals(data.imagePath)) {
							// newPath.imageCount = newPath.imageCount+1;
							holder.textView.setText(String.format("%02d",
									newPath.imageCount + 1));
							holder.textView.setBackgroundColor(activity.getResources().getColor(android.R.color.black));
							application.addSelectedImage(newPath);
							notifyItemChanged(pos);
							if (clickListner != null) {
								clickListner.onItemClick(v, data);
							}
							return;
							// data.imagePath = newPath.imagePath;
							// data.imageCount = newPath.imageCount;
							// data.folderName = newPath.folderName;
						}
					}
				}
//				application.addSelectedImage(data);
				// if (ImageSelectionActivity.isForFirst &&
				// ImageSelectionActivity.tempImage.size()==0) {
				// ImageSelectionActivity.isForFirst = false;
				//
				// }
//				notifyItemChanged(pos);
//				if (clickListner != null) {
//					clickListner.onItemClick(v, data);
//				}
				if (application.getSelectedImages().size() < 20) {

					application.addSelectedImage(data);

					if (application.getSelectedImages().size() > 3) {
					}
				

					notifyItemChanged(pos);
					if (clickListner != null) {
						clickListner.onItemClick(v, data);
					}

				} else {
					Toast.makeText(v.getContext(), "Max " + "20" + " image select ", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	public Holder onCreateViewHolder(ViewGroup parent, int pos) {
		return new Holder(inflater.inflate(R.layout.items_by_folder, parent,
				false));
	}

}