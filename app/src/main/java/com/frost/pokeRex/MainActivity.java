package com.frost.pokeRex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.frost.pokeRex.ApiService.ApiService;
import com.frost.pokeRex.model.PokeRespuesta;
import com.frost.pokeRex.model.Pokemon;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "POKEREX";
    private Retrofit retrofit;

    private RecyclerView recyclerView;
    private ListaPokeAdapter listaPokeAdapter;

    private int offset;
    private Boolean okLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.pokeRecyclerView);
        listaPokeAdapter = new ListaPokeAdapter(this);
        recyclerView.setAdapter(listaPokeAdapter);
        recyclerView.setHasFixedSize(true);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(dy > 0) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisibleItem = layoutManager.findFirstVisibleItemPosition();

                    if (okLoading) {
                        if ((visibleItemCount + pastVisibleItem) >= totalItemCount) {
                            Log.i(TAG, "Final de la Lista.");

                            okLoading = false;
                            offset += 20;
                            getData(offset);
                        }
                    }
                }
            }
        });


        retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        okLoading = true;
        offset = 0;
        getData(offset);
    }

    private void getData(int offset) {
        ApiService service = retrofit.create(ApiService.class);
        Call<PokeRespuesta> pokeRespuestaCall = service.obtenerListaPokemon(20, offset);

        pokeRespuestaCall.enqueue(new Callback<PokeRespuesta>() {
            @Override
            public void onResponse(Call<PokeRespuesta> call, Response<PokeRespuesta> response) {
                okLoading = true;
                if (response.isSuccessful()) {
                    PokeRespuesta pokeRespuesta = response.body();
                    ArrayList<Pokemon> pokeList = pokeRespuesta.getResults();

                    listaPokeAdapter.addPokeList(pokeList);

                } else {
                    Log.e(TAG, " onResponse " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<PokeRespuesta> call, Throwable t) {
                okLoading = true;
                Log.e(TAG, " onFailure " + t.getMessage());
            }
        });

    }
}
