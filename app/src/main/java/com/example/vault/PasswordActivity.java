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
        if(mDataList.size()==0){
            noData.setVisibility(View.VISIBLE);
        }
        if (mDataList.size()>0){
            noData.setVisibility(View.GONE);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDialog();
            }
        }); mPasswordAdapter=new PasswordAdapter(this,mDataList);
        mPasswordAdapter=new PasswordAdapter(this,mDataList);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mPasswordAdapter);
    }
    private void addDialog(){
        LayoutInflater inflater=LayoutInflater.from(getApplicationContext());
        View view=inflater.inflate(R.layout.data_dialog,null);
        AlertDialog.Builder builder=new AlertDialog.Builder(PasswordActivity.this);
        builder.setView(view);
        final EditText link=view.findViewById(R.id.input_website);
        final EditText email=view.findViewById(R.id.input_email);
        final EditText password=view.findViewById(R.id.input_password);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(TextUtils.isEmpty(link.getText().toString()) || TextUtils.isEmpty(email.getText().toString()) || TextUtils.isEmpty(password.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Every Field Is Important",Toast.LENGTH_LONG).show();
                    return;
                }
                Data data=new Data();
                data.setWebsite(link.getText().toString());
                data.setPassword(password.getText().toString());
                data.setEmail(email.getText().toString());
                long id=mDatabaseHelper.insertPassword(data);
                Data d=mDatabaseHelper.getData(id);
                if(d!=null){
                    mDataList.add(d);
                    mPasswordAdapter.notifyDataSetChanged();
                    if(mDatabaseHelper.getNotesCount()>0){
                        noData.setVisibility(View.GONE);
                    } else {
                        noData.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
}
