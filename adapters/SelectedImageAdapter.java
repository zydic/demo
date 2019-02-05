package com.example.foldergallery.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.foldergallery.activity.EditActivity;
import com.example.notifymypic.Photobook.SelectedPhotoCurlActivity;
import com.example.photobook.R;
import com.example.foldergallery.activity.ImageSelectionActivity;
import com.example.foldergallery.data.ImageData;
import com.exampleqwe.foldergallery.MyApplication;

public class SelectedImageAdapter extends
		RecyclerView.Adapter<SelectedImageAdapter.Holder> {
	private MyApplication application;
	private LayoutInflater inflater;
	private OnItemClickListner<Object> clickListner;
	final int TYPE_IMAGE = 0, TYPE_BLANK = 1;
	public boolean isExpanded = false;
	private RequestManager glide;

	ImageSelectionActivity activity;
	Context mContext;

	public SelectedImageAdapter(Context activity) {
		mContext = activity;
		this.activity = (ImageSelectionActivity) activity;
		application = MyApplication.getInstance();
		inflater = LayoutInflater.from(activity);
		glide = Glide.with(activity);
	}

	public void setOnItemClickListner(OnItemClickListner<Object> clickListner) {
		this.clickListner = clickListner;

	}

	public class Holder extends RecyclerView.ViewHolder {
		View parent;
		private ImageView  ivEdit,ivRemove;
		private ImageView ivThumb;

		public Holder(View v) {
			super(v);
			parent = v;
			ivThumb = (ImageView) v.findViewById(R.id.ivThumb);
			ivRemove = (ImageView) v.findViewById(R.id.ivRemove);
			ivEdit = (ImageView) v.findViewById(R.id.ivEdit);
		}

		public void onItemClick(View view, Object item) {
			if (clickListner != null) {
				clickListner.onItemClick(view, item);
			}
		}
	}

	@Override
	public int getItemCount() {
		ArrayList<ImageData> list = application.getSelectedImages();
		if (!isExpanded) {
			return list.size() + 20;
		}

		return list.size();
	}

	@Override
	public int getItemViewType(int position) {
		super.getItemViewType(position);
		if (isExpanded) {
			return TYPE_IMAGE;
		}
		ArrayList<ImageData> list = application.getSelectedImages();
		if (position >= list.size()) {
			return TYPE_BLANK;
		}
		return TYPE_IMAGE;
	}

	private boolean hideRemoveBtn() {
		return application.getSelectedImages().size() <= 3
				&& activity.isFromPreview;
	}

	public ImageData getItem(int pos) {
		ArrayList<ImageData> list = application.getSelectedImages();
		if (list.size() <= pos) {
			return new ImageData();
		}
		return list.get(pos);
	}

	@Override
	public void onBindViewHolder(final Holder holder, final int pos) {

		if (getItemViewType(pos) == TYPE_BLANK) {
			holder.parent.setVisibility(View.INVISIBLE);
			return;
		}
		holder.parent.setVisibility(View.VISIBLE);
		final ImageData data = getItem(pos);
		glide.load(data.imagePath).into(holder.ivThumb);
		if (hideRemoveBtn()) {
			holder.ivRemove.setVisibility(View.GONE);
		} else {
			holder.ivRemove.setVisibility(View.VISIBLE);
		}
        holder.ivEdit.setVisibility(View.GONE);
		holder.ivRemove.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (activity.isFromPreview) {
					application.min_pos = Math.min(application.min_pos,
							Math.max(0, pos - 1));
				}
				if (ImageSelectionActivity.isForFirst) {
					if (pos <= ImageSelectionActivity.tempImage.size()) {
						if (application.getSelectedImages()
								.contains(
										ImageSelectionActivity.tempImage
												.get(pos).imagePath)) {
							ImageSelectionActivity.tempImage.remove(pos);
						}
					}
				}
				application.removeSelectedImage(pos);
				if (clickListner != null) {
					clickListner.onItemClick(v, data);
				}
				if (hideRemoveBtn()) {
					Toast.makeText(
							activity,
							R.string.at_least_3_images_require_if_you_want_to_remove_this_images_than_add_more_images_,
							Toast.LENGTH_LONG).show();
				}
				notifyDataSetChanged();
			}
		});
//


	}

	@Override
	public Holder onCreateViewHolder(ViewGroup parent, int pos) {
		View v = inflater.inflate(R.layout.grid_selected_item, parent, false);
		Holder holder = new Holder(v);
		if (getItemViewType(pos) == TYPE_BLANK) {
			v.setVisibility(View.INVISIBLE);
		} else {
			v.setVisibility(View.VISIBLE);
		}
		return holder;
	}
}