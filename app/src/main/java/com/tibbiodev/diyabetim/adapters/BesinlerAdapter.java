package com.tibbiodev.diyabetim.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tibbiodev.diyabetim.R;
import com.tibbiodev.diyabetim.models.Food;
import com.tibbiodev.diyabetim.utils.ItemClickListener;

import java.util.List;

/**
 * Created by User on 4.11.2016.
 */
public class BesinlerAdapter extends RecyclerView.Adapter<BesinlerAdapter.ViewHolder> {

    private List<Food> foodListesi;

    public BesinlerAdapter(List<Food> foodListesi){
        this.foodListesi = foodListesi;
    }

    @Override
    public BesinlerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.besin_tablosu_item,
                parent,
                false
        );
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BesinlerAdapter.ViewHolder holder, int position) {
        Food food = foodListesi.get(position);
        holder.textViewbesinIsmi.setText(food.getIsim());
        holder.textViewbesinGlisemikIndeks.setText(
                Integer.toString(food.getGlisemikIndeks()));
        holder.textViewbesinGlisemikYuk.setText(
                Integer.toString(food.getGlisemikYuk())
        );

        int besinGlisemikIndeks = food.getGlisemikIndeks();
        if(besinGlisemikIndeks >= 0 && besinGlisemikIndeks <= 55){
            holder.imageViewbesinUyariIcon.setImageResource(R.drawable.shape_circle_yellow);
        }
        else if(besinGlisemikIndeks >= 56 && besinGlisemikIndeks <= 69){
            holder.imageViewbesinUyariIcon.setImageResource(R.drawable.shape_circle_green);
        }/* >= 70 */
        else{
            holder.imageViewbesinUyariIcon.setImageResource(R.drawable.shape_circle_red);
        }

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                    Toast.makeText(view.getContext(), "Uzun tiklama", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(view.getContext(), "kisa tiklama", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodListesi.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener, View.OnLongClickListener{

        public TextView textViewbesinIsmi;
        public TextView textViewbesinGlisemikIndeks;
        public TextView textViewbesinGlisemikYuk;
        public ImageView imageViewbesinUyariIcon;
        private ItemClickListener itemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewbesinIsmi = (TextView) itemView.findViewById(R.id.besinIsmi);
            textViewbesinGlisemikIndeks = (TextView) itemView.findViewById(R.id.besinGlisemikIndeks);
            textViewbesinGlisemikYuk = (TextView) itemView.findViewById(R.id.besinGlisemikYuk);
            imageViewbesinUyariIcon = (ImageView) itemView.findViewById(R.id.besinUyariIcon);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setItemClickListener(ItemClickListener listener){
            itemClickListener = listener;
        }

        @Override
        public void onClick(View v) {
            if(itemClickListener != null){
                itemClickListener.onClick(v, getAdapterPosition(), false);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(itemClickListener != null){
                itemClickListener.onClick(v, getAdapterPosition(), true);
            }
            return true;
        }
    }





}
