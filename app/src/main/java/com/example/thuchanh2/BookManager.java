package com.example.thuchanh2;

import java.util.ArrayList;

public class BookManager {
    private static BookManager instance;
    private final ArrayList<Book> bookList;

    private BookManager() {
        bookList = new ArrayList<>();
        bookList.add(new Book(
                "Lập trình Android cơ bản",
                "Nguyễn Văn A",
                "Công nghệ",
                "Đang đọc",
                4,
                "Cuốn sách rất hữu ích cho người mới bắt đầu lập trình Android."
        ));
        bookList.add(new Book(
                "Đắc nhân tâm",
                "Dale Carnegie",
                "Kỹ năng sống",
                "Đã đọc",
                5,
                "Kinh điển về giao tiếp và ứng xử. Nên đọc ít nhất một lần."
        ));
        bookList.add(new Book(
                "Dế Mèn phiêu lưu ký",
                "Tô Hoài",
                "Văn học",
                "Chưa đọc",
                0,
                ""
        ));
    }

    public static BookManager getInstance() {
        if (instance == null) {
            instance = new BookManager();
        }
        return instance;
    }

    public ArrayList<Book> getBookList() {
        return bookList;
    }

    public void addBook(Book book) {
        bookList.add(book);
    }

    public void removeBook(int index) {
        if (index >= 0 && index < bookList.size()) {
            bookList.remove(index);
        }
    }

    public Book getBook(int index) {
        if (index >= 0 && index < bookList.size()) {
            return bookList.get(index);
        }
        return null;
    }

    public int getReadCount() {
        int count = 0;
        for (Book book : bookList) {
            if ("Đã đọc".equals(book.getStatus())) {
                count++;
            }
        }
        return count;
    }

    public int getUnfinishedCount() {
        int count = 0;
        for (Book book : bookList) {
            if ("Chưa đọc".equals(book.getStatus()) || "Đang đọc".equals(book.getStatus())) {
                count++;
            }
        }
        return count;
    }
}
