package com.example.a9projekcijafilmova.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a9projekcijafilmova.R;
import com.example.a9projekcijafilmova.db.model.Filmovi;
import com.squareup.picasso.Picasso;

import java.util.List;


public class OmiljeniAdapter extends RecyclerView.Adapter<OmiljeniAdapter.MyViewHolder> {

    private Context context;
    private List<Filmovi> filmItem;
    private OnItemClickListener listener;


    public OmiljeniAdapter(Context context, List<Filmovi> film, OnItemClickListener listener) {
        this.context = context;
        this.filmItem = film;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.search_row, parent, false );

        return new MyViewHolder( view, listener );
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvNaziv.setText( filmItem.get( position ).getmNaziv() );
        holder.tvGodina.setText( filmItem.get( position ).getmGodina() );
        holder.tvType.setText( filmItem.get( position ).getmJezik() );///
        Picasso.with( context ).load( filmItem.get( position ).getmImage() ).into( holder.ivSlika );

    }

    @Override
    public int getItemCount() {
        return filmItem.size();
    }

    public Filmovi get(int position) {
        return filmItem.get( position );
    }

    public void removeAll() {
        filmItem.clear();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvNaziv;
        private TextView tvGodina;
        private TextView tvType;
        private ImageView ivSlika;
        private OnItemClickListener vhListener;


        MyViewHolder(@NonNull View itemView, OnItemClickListener vhListener) {
            super( itemView );

            tvNaziv = itemView.findViewById( R.id.tvTitle );
            tvGodina = itemView.findViewById( R.id.tvYear );
            tvType = itemView.findViewById( R.id.tvType );
            ivSlika = itemView.findViewById( R.id.ivPoster );
            this.vhListener = vhListener;
            itemView.setOnClickListener( this );

        }

        @Override
        public void onClick(View v) {
            vhListener.onItemClick( getAdapterPosition() );
        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
