package com.example.thuchanh2;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddBookActivity extends AppCompatActivity {

    private TextInputLayout tilBookName, tilAuthor;
    private TextInputEditText etBookName, etAuthor, etNotes;
    private Spinner spinnerCategory;
    private RadioGroup rgStatus;
    private RatingBar ratingBar;
    private TextView tvRatingValue;
    private MaterialButton btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_book);

        tilBookName = findViewById(R.id.tilBookName);
        tilAuthor = findViewById(R.id.tilAuthor);
        etBookName = findViewById(R.id.etBookName);
        etAuthor = findViewById(R.id.etAuthor);
        etNotes = findViewById(R.id.etNotes);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        rgStatus = findViewById(R.id.rgStatus);
        ratingBar = findViewById(R.id.ratingBar);
        tvRatingValue = findViewById(R.id.tvRatingValue);
        btnSave = findViewById(R.id.btnSave);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        String[] categories = getResources().getStringArray(R.array.categories);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        ratingBar.setOnRatingBarChangeListener((bar, rating, fromUser) -> {
            tvRatingValue.setText(String.format("%.0f sao", rating));
        });

        btnSave.setOnClickListener(v -> saveBook());
    }

    private void saveBook() {
        tilBookName.setError(null);
        tilAuthor.setError(null);

        String bookName = etBookName.getText() != null ? etBookName.getText().toString().trim() : "";
        String author = etAuthor.getText() != null ? etAuthor.getText().toString().trim() : "";

        if (bookName.isEmpty()) {
            tilBookName.setError(getString(R.string.error_empty_book_name));
            etBookName.requestFocus();
            return;
        }

        if (author.isEmpty()) {
            tilAuthor.setError(getString(R.string.error_empty_author));
            etAuthor.requestFocus();
            return;
        }

        String category = spinnerCategory.getSelectedItem().toString();

        String status;
        int selectedId = rgStatus.getCheckedRadioButtonId();
        if (selectedId == R.id.rbReading) {
            status = "Đang đọc";
        } else if (selectedId == R.id.rbDone) {
            status = "Đã đọc";
        } else {
            status = "Chưa đọc";
        }

        float rating = ratingBar.getRating();
        String notes = etNotes.getText() != null ? etNotes.getText().toString().trim() : "";

        Book book = new Book(bookName, author, category, status, rating, notes);
        BookManager.getInstance().addBook(book);

        Toast.makeText(this, getString(R.string.save_success), Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }
}
