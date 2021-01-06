package com.example.vault;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class PasswordActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView noData;
    private FloatingActionButton fab;
    private RecyclerView mRecyclerView;
    private PasswordAdapter mPasswordAdapter;
    private DatabaseHelper mDatabaseHelper;
    private List<Data> mDataList=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        noData=(TextView)findViewById(R.id.no_data);
        mToolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mRecyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        fab=(FloatingActionButton)findViewById(R.id.fab);
        mDatabaseHelper=new DatabaseHelper(this);
        mDataList.addAll(mDatabaseHelper.getAllPassword());
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noData.setVisibility(View.GONE);
                addData();
            }
        });
        mPasswordAdapter=new PasswordAdapter(this,mDataList);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mPasswordAdapter);
    }
    public void addData(){
        Data data=new Data();
        LayoutInflater inflater=LayoutInflater.from(getApplicationContext());
        View view=inflater.inflate(R.layout.data_dialog,null);
        AlertDialog.Builder builder=new AlertDialog.Builder(PasswordActivity.this);
        builder.setView(view);
        EditText link=view.findViewById(R.id.input_website);
        EditText username=view.findViewById(R.id.input_email);
        EditText password=view.findViewById(R.id.input_password);
        TextView title=view.findViewById(R.id.title);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }
}
