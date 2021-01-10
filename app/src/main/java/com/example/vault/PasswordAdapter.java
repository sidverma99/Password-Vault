package com.example.vault;

import android.Manifest;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.CancellationSignal;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PasswordAdapter extends RecyclerView.Adapter<PasswordAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<Data> mDataList;
    private List<Data> filteredList;
    private Data data;
    private CancellationSignal c1,c2,c3;
    public  PasswordAdapter(Context context,List<Data>dataList){
        this.context=context;
        this.mDataList=dataList;
        this.filteredList=dataList;
    }
    private DatabaseHelper mDatabaseHelper;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mDatabaseHelper=new DatabaseHelper(context);
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        data=filteredList.get(position);
        holder.savedLink.setText(data.getWebsite());
        holder.savedUsername.setText(data.getEmail());
        String p=data.getPassword();
        char[] a=p.toCharArray();
        for(int i=0;i<p.length();i++){
            a[i]='●';
        }
        String q=new String(a);
        holder.savedPassword.setText(q);
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString=constraint.toString();
                if(charString.isEmpty()){
                    filteredList=mDataList;
                } else {
                    List<Data> newList=new ArrayList<>();
                    for(Data d:mDataList){
                        if(d.getEmail().toLowerCase().contains(charString.toLowerCase()) || d.getWebsite().toLowerCase().contains(charString.toLowerCase())){
                            newList.add(d);
                        }
                    }
                    filteredList=newList;
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList=(List<Data>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView savedLink,savedUsername,savedPassword;
        private Button showPassword,update;
        private ImageButton delete;
        private Boolean flag=false;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            savedLink=itemView.findViewById(R.id.website);
            savedUsername=itemView.findViewById(R.id.username);
            savedPassword=itemView.findViewById(R.id.password);
            showPassword=itemView.findViewById(R.id.show_password_btn);
            delete=itemView.findViewById(R.id.delete_btn);
            update=itemView.findViewById(R.id.update_password_btn);
            delete.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.P)
                @Override
                public void onClick(View v) {
                    authenticateUser();
                }
            });
            update.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.P)
                @Override
                public void onClick(View v) {
                    authenticateUser1();
                }
            });
            showPassword.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.P)
                @Override
                public void onClick(View v) {
                    if(flag==false){
                        authenticateUser2();
                    }
                    if(flag==true){
                        Data m=filteredList.get(getAdapterPosition());
                        String x=m.getPassword();
                        char[] b=x.toCharArray();
                        for(int i=0;i<x.length();i++){
                            b[i]='●';
                        }
                        String y=new String(b);
                        savedPassword.setText(y);
                        showPassword.setText("Show Password");
                    }
                    flag=!flag;
                }
            });
        }
        @RequiresApi(api = Build.VERSION_CODES.P)
        public void authenticateUser(){
            BiometricPrompt biometricPrompt=new BiometricPrompt.Builder(context).setTitle("Biometric Authentication").setSubtitle("Authentication is required to continue").setDescription("This app uses biometric authentication to protect your data").setNegativeButton("Cancel", context.getMainExecutor(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).build();
            biometricPrompt.authenticate(getCancellationSignal(),context.getMainExecutor(),getAuthenticationCallback());
        }
        private Boolean checkBiometricSupport(){
            KeyguardManager keyguardManager=(KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            PackageManager packageManager=context.getPackageManager();
            if(!keyguardManager.isKeyguardSecure()){
                return false;
            }
            if(ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_BIOMETRIC)!=packageManager.PERMISSION_GRANTED){
                return false;
            }
            if(packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) return true;

            return true;
        }
        @RequiresApi(api = Build.VERSION_CODES.P)
        private BiometricPrompt.AuthenticationCallback getAuthenticationCallback(){
            return new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                }

                @Override
                public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                    super.onAuthenticationHelp(helpCode, helpString);
                }

                @Override
                public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    Data d=filteredList.get(getAdapterPosition());
                    mDatabaseHelper.deleteData(d);
                    filteredList.clear();
                    filteredList.addAll(mDatabaseHelper.getAllPassword());
                    if (filteredList.size()==0){
                        ((PasswordActivity)context).toggle();
                        PasswordAdapter.this.notifyDataSetChanged();
                    } else {
                        PasswordAdapter.this.notifyDataSetChanged();
                    }
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                }
            };
        }
        private CancellationSignal getCancellationSignal(){
            c1=new CancellationSignal();
            c1.setOnCancelListener(new CancellationSignal.OnCancelListener() {
                @Override
                public void onCancel() {

                }
            });
            return c1;
        }
        @RequiresApi(api = Build.VERSION_CODES.P)
        public void authenticateUser1(){
            BiometricPrompt biometricPrompt=new BiometricPrompt.Builder(context).setTitle("Biometric Authentication").setSubtitle("Authentication is required to continue").setDescription("This app uses biometric authentication to protect your data").setNegativeButton("Cancel", context.getMainExecutor(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).build();
            biometricPrompt.authenticate(getCancellationSignal1(),context.getMainExecutor(),getAuthenticationCallback1());
        }
        @RequiresApi(api = Build.VERSION_CODES.P)
        private BiometricPrompt.AuthenticationCallback getAuthenticationCallback1(){
            return new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                }

                @Override
                public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                    super.onAuthenticationHelp(helpCode, helpString);
                }

                @Override
                public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    LayoutInflater inflater=LayoutInflater.from(context);
                    View view=inflater.inflate(R.layout.update_data,null);
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setView(view);
                    final EditText updateLink=view.findViewById(R.id.update_website);
                    final EditText updateEmail=view.findViewById(R.id.update_email);
                    final EditText updatePassword=view.findViewById(R.id.update_password);
                    Data d=filteredList.get(getAdapterPosition());
                    updateLink.setText(d.getWebsite());
                    updateEmail.setText(d.getEmail());
                    updatePassword.setText(d.getPassword());
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(TextUtils.isEmpty(updateLink.getText().toString()) || TextUtils.isEmpty(updateEmail.getText().toString()) || TextUtils.isEmpty(updatePassword.getText().toString())){
                                Toast.makeText(context,"Every Field Is Important",Toast.LENGTH_LONG).show();
                                return;
                            }
                            Data updatedData=new Data();
                            updatedData.setWebsite(updateLink.getText().toString());
                            updatedData.setEmail(updateEmail.getText().toString());
                            updatedData.setPassword(updatePassword.getText().toString());
                            mDatabaseHelper.updateData(updatedData);
                            filteredList.set(getAdapterPosition(),updatedData);
                            if(flag==true){
                                showPassword.setText("Show Password");
                                flag=!flag;
                            }
                            if (filteredList.size()==0){
                                ((PasswordActivity)context).toggle();
                                PasswordAdapter.this.notifyItemChanged(getAdapterPosition(),updatedData);
                            } else {
                                PasswordAdapter.this.notifyItemChanged(getAdapterPosition(),updatedData);
                            }
                        }
                    });
                    AlertDialog alertDialog=builder.create();
                    alertDialog.show();
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                }
            };
        }
        private CancellationSignal getCancellationSignal1(){
            c2=new CancellationSignal();
            c2.setOnCancelListener(new CancellationSignal.OnCancelListener() {
                @Override
                public void onCancel() {

                }
            });
            return c2;
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        public void authenticateUser2(){
            BiometricPrompt biometricPrompt=new BiometricPrompt.Builder(context).setTitle("Biometric Authentication").setSubtitle("Authentication is required to continue").setDescription("This app uses biometric authentication to protect your data").setNegativeButton("Cancel", context.getMainExecutor(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).build();
            biometricPrompt.authenticate(getCancellationSignal2(),context.getMainExecutor(),getAuthenticationCallback2());
        }
        @RequiresApi(api = Build.VERSION_CODES.P)
        private BiometricPrompt.AuthenticationCallback getAuthenticationCallback2(){
            return new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                }

                @Override
                public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                    super.onAuthenticationHelp(helpCode, helpString);
                }

                @Override
                public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    Data a=filteredList.get(getAdapterPosition());
                    savedPassword.setText(a.getPassword());
                    showPassword.setText("Hide Password");
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                }
            };
        }
        private CancellationSignal getCancellationSignal2(){
            c3=new CancellationSignal();
            c3.setOnCancelListener(new CancellationSignal.OnCancelListener() {
                @Override
                public void onCancel() {

                }
            });
            return c3;
        }
    }
}
