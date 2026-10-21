package com.example.thuchanh2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView tvStatTotal, tvStatRead, tvStatUnread;
    private ListView lvBooks;
    private LinearLayout layoutEmpty;
    private FloatingActionButton fabAdd;

    private BookManager bookManager;
    private BookAdapter bookAdapter;

    private final ActivityResultLauncher<Intent> addBookLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    refreshList();
                }
            });

    private final ActivityResultLauncher<Intent> detailLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    refreshList();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvStatTotal = findViewById(R.id.tvStatTotal);
        tvStatRead = findViewById(R.id.tvStatRead);
        tvStatUnread = findViewById(R.id.tvStatUnread);
        lvBooks = findViewById(R.id.lvBooks);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        fabAdd = findViewById(R.id.fabAdd);

        bookManager = BookManager.getInstance();
        bookAdapter = new BookAdapter(this, bookManager.getBookList());
        bookAdapter.setOnItemClickListener(this::openBookDetail);
        lvBooks.setAdapter(bookAdapter);

        lvBooks.setOnItemClickListener((parent, view, position, id) -> openBookDetail(position));

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddBookActivity.class);
            addBookLauncher.launch(intent);
        });

        refreshList();
    }

    private void openBookDetail(int position) {
        if (position >= 0 && position < bookManager.getBookList().size()) {
            Intent intent = new Intent(MainActivity.this, BookDetailActivity.class);
            intent.putExtra("book_index", position);
            detailLauncher.launch(intent);
        }
    }

    private void refreshList() {
        bookAdapter.notifyDataSetChanged();

        ArrayList<Book> books = bookManager.getBookList();
        int total = books.size();
        int read = bookManager.getReadCount();
        int unfinished = bookManager.getUnfinishedCount();

        tvStatTotal.setText(String.valueOf(total));
        tvStatRead.setText(String.valueOf(read));
        tvStatUnread.setText(String.valueOf(unfinished));

        if (total == 0) {
            lvBooks.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
        } else {
            lvBooks.setVisibility(View.VISIBLE);
            layoutEmpty.setVisibility(View.GONE);
        }
    }
}
