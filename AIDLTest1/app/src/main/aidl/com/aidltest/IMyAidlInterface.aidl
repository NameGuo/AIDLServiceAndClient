// IMyAidlInterface.aidl
package com.aidltest;
import com.aidltest.Book;
// Declare any non-default types here with import statements

interface IMyAidlInterface {
    List<Book> getBookList();
    void addBookInOut(inout Book book);
}
