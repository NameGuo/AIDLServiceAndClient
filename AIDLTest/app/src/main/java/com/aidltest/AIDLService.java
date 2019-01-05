package com.aidltest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2019/1/5.
 */
public class AIDLService extends Service {


    private final String TAG = "Server";

    private List<Book> bookList;

    public AIDLService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        bookList = new ArrayList<>();

        initData();
    }

    private void initData() {
        Book book1 = new Book("活着");
        Book book2 = new Book("或者");
        Book book3 = new Book("生活");
        Book book4 = new Book("生存");
        Book book5 = new Book("希望");

        bookList.add(book1);
        bookList.add(book2);
        bookList.add(book3);
        bookList.add(book4);
        bookList.add(book5);
    }

    private final IMyAidlInterface.Stub stub = new IMyAidlInterface.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            return bookList;
        }

        @Override
        public void addBookInOut(Book book) throws RemoteException {
            if (book != null){
                book.setName("服务器改了新书的名字 InOut");
                bookList.add(book);
            }else {
                Log.e(TAG, "接收到一个空的对象");
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }





}
