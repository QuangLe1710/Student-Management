package com.example.sqlitetest;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText edtMaLop , edtTenLop , edtSiSo ;
    Button btnInsert , btnDelete , btnUpdate , btnQuery;
    ListView lvDSSV ;
    ArrayList<String> myList ;
    ArrayAdapter<String> myAdapter;
    SQLiteDatabase mydatabase ;
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

        // Tham chiếu biến giao diện
        myMapping();

        // Tạo và mở cơ sở dữ liệu SQLite
        mydatabase = openOrCreateDatabase("qlsinhvien.db" , MODE_PRIVATE , null );
        // Tạo table chứa dữ liệu
        try {
            String sql = "CREATE TABLE tbllop(malop TEXT primary key , tenlop TEXT , siso INTEGER)" ;
            mydatabase.execSQL(sql);
        } catch (Exception e) {
            Log.e("Error" , "Table đã tồn tại");
        }

        // Chức năng Insert
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String maLop = edtMaLop.getText().toString();
                String tenLop = edtTenLop.getText().toString();
                Integer siSo = Integer.parseInt(edtSiSo.getText().toString());
                ContentValues myValue = new ContentValues();
                myValue.put("malop" , maLop);
                myValue.put("tenlop" , tenLop);
                myValue.put("siso" , siSo);
                String msg = "" ;
                if ( mydatabase.insert("tbllop" , null , myValue) == -1 ) {
                    msg = "FAIL TO INSERT RECORD" ;
                } else {
                    msg = "INSERT RECORD SUCCESSFULLY" ;
                }
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        // Chức năng Delete
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String malop = edtMaLop.getText().toString();
                int n = mydatabase.delete("tbllop" , "malop = ?" , new String[]{malop});
                String msg = "";
                if (n == 0 ) {
                    msg = "NO RECORD TO DELETE";
                } else {
                    msg = n + "RECORD IS DELETED" ;
                }
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        // Chức năng Update
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int siso = Integer.parseInt(edtSiSo.getText().toString());
                String malop = edtMaLop.getText().toString();
                ContentValues myValue = new ContentValues();
                myValue.put("siso" , siso);

                int n = mydatabase.update("tbllop" , myValue , "malop = ?" ,new String[]{malop});
                String msg = "";
                if (n== 0) {
                    msg = "NO RECORD TO UPDATe" ;
                } else {
                    msg = n + "RECORD IS UPDATED" ;
                }
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myList.clear();
                Cursor cursor = mydatabase.query("tbllop" , null , null , null , null , null , null , null) ;
                cursor.moveToNext();
                String data = "" ;
                while (cursor.isAfterLast() == false){
                    data = cursor.getString(0) + " - " + cursor.getString(1) + " - " + cursor.getString(2);
                    cursor.moveToNext();
                    myList.add(data);
                }
                cursor.close();
                myAdapter.notifyDataSetChanged();
            }
        });
    }

    private void myMapping() {
        edtMaLop = findViewById(R.id.edt_MaLop);
        edtTenLop = findViewById(R.id.edt_TenLop);
        edtSiSo = findViewById(R.id.edt_SiSo);

        btnInsert = findViewById(R.id.btn_Insert);
        btnDelete = findViewById(R.id.btn_Delete);
        btnUpdate = findViewById(R.id.btn_Update);
        btnQuery = findViewById(R.id.btn_Query);

        lvDSSV = findViewById(R.id.lv_DSsv);
        myList = new ArrayList<>();
        myAdapter = new ArrayAdapter<>(this , android.R.layout.simple_list_item_1 , myList) ;
        lvDSSV.setAdapter(myAdapter);
    }
}