package com.example.foldergallery.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.foldergallery.activity.EditActivity;
import com.example.foldergallery.data.ImageData;
import com.example.photobook.R;
import com.exampleqwe.foldergallery.MyApplication;

import java.util.ArrayList;
import java.util.Collections;

public class ImageEditAdapter extends
		RecyclerView.Adapter<ImageEditAdapter.Holder> {
	private MyApplication application;
	private LayoutInflater inflater;
	private OnItemClickListner<Object> clickListner;
	final int TYPE_IMAGE = 0, TYPE_BLANK = 1;
	private RequestManager glide;
	Context mContext;

	public ImageEditAdapter(Context activity) {
		mContext = activity;
		application = MyApplication.getInstance();
		inflater = LayoutInflater.from(activity);
		glide = Glide.with(activity);
	}

	public void setOnItemClickListner(OnItemClickListner<Object> clickListner) {
		this.clickListner = clickListner;
	}

	public class Holder extends RecyclerView.ViewHolder {
		View parent;
		private ImageView  ivEdit;
		private ImageView ivThumb,ivRemove;

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
		return application.getSelectedImages().size();
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
		holder.parent.setVisibility(View.VISIBLE);
		final ImageData data = getItem(pos);
		glide.load(data.imagePath).into(holder.ivThumb);
		if (getItemCount() <= 7) {
			holder.ivRemove.setVisibility(View.GONE);
		} else {
			holder.ivRemove.setVisibility(View.VISIBLE);
		}



		holder.ivRemove.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				application.min_pos = Math.min(application.min_pos,
						Math.max(0, pos - 1));
				MyApplication.isBreak = true;
				application.removeSelectedImage(pos);
				if (clickListner != null) {
					clickListner.onItemClick(v, data);
				}
				notifyDataSetChanged();
			}
		});
		holder.ivEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent editIntent = new Intent(mContext,
						EditActivity.class);
				MyApplication.TEMP_POSITION = pos;
				editIntent.putExtra("position", pos);
				((Activity) mContext).startActivityForResult(editIntent, 999);
			}
		});
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

	public synchronized void swap(int fromPosition, int toPosition) {
		ArrayList<ImageData> list = application.getSelectedImages();
		Collections.swap(list, fromPosition, toPosition);
		notifyItemMoved(fromPosition, toPosition);
	}

}