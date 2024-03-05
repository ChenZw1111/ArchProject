package com.example.hiui.aidl;
import com.example.hiui.aidl.Book;
interface IBookManager{
    List<Book> getBookList();
    void addBook(in Book book);
}