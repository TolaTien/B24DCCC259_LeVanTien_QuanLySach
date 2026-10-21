package com.example.thuchanh2;

import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;

public class BookDetailActivity extends AppCompatActivity {

    private TextView tvBookName, tvAuthor, tvCategory, tvStatus, tvNotes, tvRatingDetailValue;
    private RatingBar ratingBar;
    private MaterialButton btnMarkRead, btnDelete;

    private int bookIndex;
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_detail);

        tvBookName = findViewById(R.id.tvBookName);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvCategory = findViewById(R.id.tvCategory);
        tvStatus = findViewById(R.id.tvStatus);
        tvNotes = findViewById(R.id.tvNotes);
        tvRatingDetailValue = findViewById(R.id.tvRatingDetailValue);
        ratingBar = findViewById(R.id.ratingBar);
        btnMarkRead = findViewById(R.id.btnMarkRead);
        btnDelete = findViewById(R.id.btnDelete);

        View.OnClickListener backClickListener = v -> {
            setResult(RESULT_OK);
            finish();
        };
        findViewById(R.id.btnBack).setOnClickListener(backClickListener);
        findViewById(R.id.btnBackBottom).setOnClickListener(backClickListener);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                setResult(RESULT_OK);
                finish();
            }
        });

        bookIndex = getIntent().getIntExtra("book_index", -1);
        if (bookIndex == -1) {
            finish();
            return;
        }

        book = BookManager.getInstance().getBook(bookIndex);
        if (book == null) {
            finish();
            return;
        }

        displayBookInfo();

        ratingBar.setOnRatingBarChangeListener((bar, rating, fromUser) -> {
            if (fromUser) {
                book.setRating(rating);
                if (tvRatingDetailValue != null) {
                    tvRatingDetailValue.setText(String.format("%.0f sao", rating));
                }
            }
        });

        btnMarkRead.setOnClickListener(v -> {
            book.setStatus("Đã đọc");
            displayBookInfo();
            Toast.makeText(this, "Đã cập nhật trạng thái!", Toast.LENGTH_SHORT).show();
        });

        btnDelete.setOnClickListener(v -> showDeleteConfirmation());
    }

    private void displayBookInfo() {
        tvBookName.setText(book.getName());
        tvAuthor.setText(book.getAuthor());
        tvCategory.setText(book.getCategory());
        ratingBar.setRating(book.getRating());
        if (tvRatingDetailValue != null) {
            tvRatingDetailValue.setText(String.format("%.0f sao", book.getRating()));
        }

        setStatusBadge(tvStatus, book.getStatus());

        String notes = book.getNotes();
        if (notes == null || notes.isEmpty()) {
            tvNotes.setText(getString(R.string.no_notes));
            tvNotes.setTextColor(ContextCompat.getColor(this, R.color.text_hint));
        } else {
            tvNotes.setText(notes);
            tvNotes.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
        }

        if ("Đã đọc".equals(book.getStatus())) {
            btnMarkRead.setEnabled(false);
            btnMarkRead.setText("Đã đọc xong ✓");
            btnMarkRead.setAlpha(0.5f);
        } else {
            btnMarkRead.setEnabled(true);
            btnMarkRead.setText(getString(R.string.btn_mark_read));
            btnMarkRead.setAlpha(1f);
        }
    }

    private void setStatusBadge(TextView tvStatus, String status) {
        tvStatus.setText(status);
        switch (status) {
            case "Chưa đọc":
                tvStatus.setTextColor(ContextCompat.getColor(this, R.color.status_unread));
                tvStatus.setBackgroundResource(R.drawable.bg_badge_unread);
                break;
            case "Đang đọc":
                tvStatus.setTextColor(ContextCompat.getColor(this, R.color.status_reading));
                tvStatus.setBackgroundResource(R.drawable.bg_badge_reading);
                break;
            case "Đã đọc":
                tvStatus.setTextColor(ContextCompat.getColor(this, R.color.status_done));
                tvStatus.setBackgroundResource(R.drawable.bg_badge_done);
                break;
        }
    }

    private void showDeleteConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.delete_confirm_title))
                .setMessage(getString(R.string.delete_confirm_message))
                .setPositiveButton(getString(R.string.btn_confirm_delete), (dialog, which) -> {
                    BookManager.getInstance().removeBook(bookIndex);
                    Toast.makeText(this, "Đã xóa sách!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                })
                .setNegativeButton(getString(R.string.btn_cancel), null)
                .show();
    }
}
