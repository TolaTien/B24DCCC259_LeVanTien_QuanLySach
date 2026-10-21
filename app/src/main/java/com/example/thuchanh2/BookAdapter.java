package com.example.thuchanh2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class BookAdapter extends BaseAdapter {

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private final Context context;
    private final ArrayList<Book> bookList;
    private OnItemClickListener listener;

    public BookAdapter(Context context, ArrayList<Book> bookList) {
        this.context = context;
        this.bookList = bookList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return bookList.size();
    }

    @Override
    public Object getItem(int position) {
        return bookList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
            holder = new ViewHolder();
            holder.tvBookName = convertView.findViewById(R.id.tvBookName);
            holder.tvAuthor = convertView.findViewById(R.id.tvAuthor);
            holder.tvCategory = convertView.findViewById(R.id.tvCategory);
            holder.tvStatus = convertView.findViewById(R.id.tvStatus);
            holder.tvRatingNum = convertView.findViewById(R.id.tvRatingNum);
            holder.stars = new ImageView[]{
                    convertView.findViewById(R.id.star1),
                    convertView.findViewById(R.id.star2),
                    convertView.findViewById(R.id.star3),
                    convertView.findViewById(R.id.star4),
                    convertView.findViewById(R.id.star5)
            };
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Book book = bookList.get(position);

        holder.tvBookName.setText(book.getName());
        holder.tvAuthor.setText(book.getAuthor());
        holder.tvCategory.setText(book.getCategory());

        setStatusBadge(holder.tvStatus, book.getStatus());

        int rating = (int) book.getRating();
        for (int i = 0; i < 5; i++) {
            if (i < rating) {
                holder.stars[i].setImageResource(R.drawable.ic_star_filled);
            } else {
                holder.stars[i].setImageResource(R.drawable.ic_star_empty);
            }
        }

        if (holder.tvRatingNum != null) {
            if (rating > 0) {
                holder.tvRatingNum.setText(rating + "★");
                holder.tvRatingNum.setVisibility(View.VISIBLE);
            } else {
                holder.tvRatingNum.setText("Chưa đánh giá");
                holder.tvRatingNum.setVisibility(View.VISIBLE);
            }
        }

        final int currentPosition = position;
        convertView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(currentPosition);
            }
        });

        return convertView;
    }

    private void setStatusBadge(TextView tvStatus, String status) {
        tvStatus.setText(status);
        switch (status) {
            case "Chưa đọc":
                tvStatus.setTextColor(ContextCompat.getColor(context, R.color.status_unread));
                tvStatus.setBackgroundResource(R.drawable.bg_badge_unread);
                break;
            case "Đang đọc":
                tvStatus.setTextColor(ContextCompat.getColor(context, R.color.status_reading));
                tvStatus.setBackgroundResource(R.drawable.bg_badge_reading);
                break;
            case "Đã đọc":
                tvStatus.setTextColor(ContextCompat.getColor(context, R.color.status_done));
                tvStatus.setBackgroundResource(R.drawable.bg_badge_done);
                break;
        }
    }

    private static class ViewHolder {
        TextView tvBookName;
        TextView tvAuthor;
        TextView tvCategory;
        TextView tvStatus;
        TextView tvRatingNum;
        ImageView[] stars;
    }
}
