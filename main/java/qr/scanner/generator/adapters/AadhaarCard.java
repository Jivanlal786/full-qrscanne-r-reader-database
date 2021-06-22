package qr.scanner.generator.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import qr.scanner.generator.R;
import qr.scanner.generator.room.User;

public class AadhaarCard extends RecyclerView.Adapter<AadhaarCard.MyCardHolder> {

    Context context;
    List<User>userList;

    public AadhaarCard(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyCardHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.aadhaarcard_adapter, parent, false);
        return new MyCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  AadhaarCard.MyCardHolder holder, int position) {

        holder.tvname.setText(userList.get(position).getName());
        holder.tvgender.setText(userList.get(position).getGender());
        holder.tvUid.setText(userList.get(position).getUid());
        holder.tvbirth.setText(userList.get(position).getBirth());
        holder.ivQR.setImageBitmap(userList.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyCardHolder extends RecyclerView.ViewHolder {
        
        ImageView ivQR;
        TextView tvname,tvgender,tvbirth,tvUid;
        
        public MyCardHolder(@NonNull  View itemView) {
            super(itemView);
        
        ivQR = itemView.findViewById(R.id.iv_qr);
        tvname = itemView.findViewById(R.id.tvname);
        tvgender = itemView.findViewById(R.id.tvgender);
        tvbirth = itemView.findViewById(R.id.tvbirth);
        tvUid = itemView.findViewById(R.id.tvuid);
        }
    }
}
