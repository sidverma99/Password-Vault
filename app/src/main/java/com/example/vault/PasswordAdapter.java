package com.example.vault;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PasswordAdapter extends RecyclerView.Adapter<PasswordAdapter.MyViewHolder> {
    private Context context;
    private List<Data> mDataList;
    private Data data;
    public  PasswordAdapter(Context context,List<Data>dataList){
        this.context=context;
        this.mDataList=dataList;
    }
    private DatabaseHelper mDatabaseHelper;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        data=mDataList.get(position);
        holder.savedLink.setText(data.getWebsite());
        holder.savedUsername.setText(data.getEmail());
        String p=data.getPassword();
        char[] a=p.toCharArray();
        for(int i=0;i<p.length();i++){
            a[i]='*';
        }
        String q=new String(a);
        holder.savedPassword.setText(q);
        holder.showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.savedPassword.setText(data.getPassword());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView savedLink,savedUsername,savedPassword;
        private Button showPassword,update;
        private ImageButton delete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            savedLink=itemView.findViewById(R.id.website);
            savedUsername=itemView.findViewById(R.id.username);
            savedPassword=itemView.findViewById(R.id.password);
            showPassword=itemView.findViewById(R.id.show_password_btn);
            update=itemView.findViewById(R.id.update_password_btn);
            delete=itemView.findViewById(R.id.delete_btn);
        }
    }
}
