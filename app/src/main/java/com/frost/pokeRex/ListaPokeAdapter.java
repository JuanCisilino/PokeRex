package com.frost.pokeRex;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.frost.pokeRex.model.Pokemon;

import java.util.ArrayList;
import java.util.List;

public class ListaPokeAdapter extends RecyclerView.Adapter<ListaPokeAdapter.PokeViewHolder> {

    private List<Pokemon> listaDePokemones;
    private Context context;
    private Dialog pokepopup;

    public ListaPokeAdapter(Context context) {
        this.context = context;
        this.listaDePokemones = new ArrayList<Pokemon>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PokeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pokemon, parent, false);
        pokepopup = new Dialog(context);
        return new PokeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PokeViewHolder holder, int position) {
        Pokemon p = listaDePokemones.get(position);
        holder.pokeName.setText(p.getName());

        Glide.with(context)
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + p.getNumber() + ".png")
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.pokeImage);
    }

    @Override
    public int getItemCount() {
        return listaDePokemones.size();
    }

    public void addPokeList(List<Pokemon> pokemonList) {
        listaDePokemones.addAll(pokemonList);
        notifyDataSetChanged();
    }

    public class PokeViewHolder extends RecyclerView.ViewHolder {

        private ImageView pokeImage;
        private TextView pokeName;

        public PokeViewHolder(@NonNull View itemView) {
            super(itemView);

            pokeImage = itemView.findViewById(R.id.pokeImageView);
            pokeName = itemView.findViewById(R.id.pokeTextview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Pokemon pokemonSelected = listaDePokemones.get(getAdapterPosition());
                    TextView pokeX;
                    ImageView pokeSelMalImage;
                    ImageView pokeSelFemImage;
                    TextView pokeSelName;
                    pokepopup.setContentView(R.layout.pokepopup);
                    pokeX = pokepopup.findViewById(R.id.pokeSelectedX);
                    pokeSelMalImage = pokepopup.findViewById(R.id.pokeSelectedMaleImageView);
                    pokeSelFemImage = pokepopup.findViewById(R.id.pokeSelectedFemaleImageView);
                    pokeSelName = pokepopup.findViewById(R.id.pokeSelectedNameTextView);
                    pokeX.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            pokepopup.dismiss();
                        }
                    });
                    Glide.with(context)
                            .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + pokemonSelected.getNumber() + ".png")
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(pokeSelMalImage);
                    Glide.with(context)
                            .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/" + pokemonSelected.getNumber() + ".png")
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(pokeSelFemImage);
                    pokeSelName.setText(pokemonSelected.getName());
                    pokepopup.show();
                }
            });
        }
    }

}
