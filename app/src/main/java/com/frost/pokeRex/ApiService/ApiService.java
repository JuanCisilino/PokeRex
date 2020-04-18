package com.frost.pokeRex.ApiService;

import com.frost.pokeRex.model.PokeRespuesta;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("pokemon")
    Call<PokeRespuesta> obtenerListaPokemon(@Query("limit") int limit, @Query("offset") int offset);
}
