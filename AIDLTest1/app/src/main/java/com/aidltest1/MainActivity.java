package com.aidltest1;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.aidltest.Book;
import com.aidltest.IMyAidlInterface;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_getbook;
    private Button btn_addbook;

    private final String TAG = "Client";

    private IMyAidlInterface bookController;

    private boolean connected;

    private List<Book> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_getbook = (Button) findViewById(R.id.btn_getbook);
        btn_addbook = (Button) findViewById(R.id.btn_addbook);
        btn_addbook.setOnClickListener(this);
        btn_getbook.setOnClickListener(this);

        bindService();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_getbook:
                if (connected) {
                    try {
                        bookList = bookController.getBookList();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    log();
                }
                break;
            case R.id.btn_addbook:
                if (connected) {
                    Book book = new Book("这是一本新书 InOut");
                    try {
                        bookController.addBookInOut(book);
                        Log.e(TAG, "向服务器以InOut方式添加了一本新书");
                        Log.e(TAG, "新书名：" + book.getName());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            bookController = IMyAidlInterface.Stub.asInterface(iBinder);
            connected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            connected = false;
        }
    };


    private void bindService(){
        Intent intent = new Intent();
        intent.setPackage("com.aidltest");
        intent.setAction("android.intent.action.book");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void log() {
        for (Book book : bookList) {
            Log.e(TAG, book.toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (connected) {
            unbindService(serviceConnection);
        }
    }
}
