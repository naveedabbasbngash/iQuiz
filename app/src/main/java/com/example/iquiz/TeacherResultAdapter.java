package com.example.iquiz;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TeacherResultAdapter extends RecyclerView.Adapter<TeacherResultAdapter.ViewHolder> {
    ResultInterface mresultInterface;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference("INU");
    DatabaseReference dbref1 = db.child("Students");

    List<TeacherResultModel> mylist;
    onObjectClickListener onObjectClickListener;

    public TeacherResultAdapter(List<TeacherResultModel> mylist, TeacherResultAdapter.onObjectClickListener onObjectClickListener, ResultInterface resultInterface) {
        this.mylist = mylist;
        this.onObjectClickListener = onObjectClickListener;
        mresultInterface=resultInterface;
    }

    @NonNull
    @Override
    public TeacherResultAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.teacherresultview,parent,false);
        return new ViewHolder(v,onObjectClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherResultAdapter.ViewHolder holder, int position) {
        String studentid = mylist.get(position).getStudent_id();
        String studentmarks = mylist.get(position).getTest_marks();
        String stdresult;
        String color;

        Double smarks = Double.parseDouble(studentmarks);
        if(smarks>5)
        {
            stdresult = "PASS";
            color = "#00ff00";
        }
        else
        {
            stdresult = "FAIL";
            color = "#ff0000";
        }

        holder.setData(studentid,studentmarks,stdresult,color);
    }

    @Override
    public int getItemCount() {
        return mylist.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtid,txtname,txtsection,txtmarks,txtresult;
        onObjectClickListener onObjectClickListener;
        String stdName,stdSection;

        public ViewHolder(@NonNull View itemView, onObjectClickListener onObjectClickListener) {
            super(itemView);

            txtid = itemView.findViewById(R.id.stdid);
            txtname = itemView.findViewById(R.id.stdname);
            txtmarks = itemView.findViewById(R.id.stdmarks);
            txtresult = itemView.findViewById(R.id.stdresult);
            txtsection = itemView.findViewById(R.id.stdsection);

            this.onObjectClickListener = onObjectClickListener;
        }

        public void setData(String id,String marks,String result,String color)
        {
            txtid.setText(id);
            //txtsection.setText(Model.getInstance().model_section_name);
            txtmarks.setText(marks);
            txtresult.setText(result);
            txtresult.setBackgroundColor(Color.parseColor(color));

            int uid = Integer.parseInt(id);
            Query qry = dbref1.orderByChild("roll").equalTo(uid);
            qry.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot values : dataSnapshot.getChildren()) {
                        System.out.println("-----------------------------------------------------------------");
                        StudentModel records = values.getValue(StudentModel.class);
//                        System.out.println("Student Name: -------------------------------------------------"+records.getName());
//                        System.out.println("Section Name: -------------------------------------------------"+records.getSection());
                        stdName = records.getName();
                        stdSection = records.getSection();
                        txtname.setText(stdName);
                        txtsection.setText(stdSection);
                        TeacherResultModel teacherResultModel=new TeacherResultModel();
                        teacherResultModel.setName(records.getName());
                        teacherResultModel.setSub(records.getSection());
                        List<TeacherResultModel> tec=new ArrayList<>();

                      ;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public interface onObjectClickListener
    {
        public void onObjectClick(int position);
    }
}
