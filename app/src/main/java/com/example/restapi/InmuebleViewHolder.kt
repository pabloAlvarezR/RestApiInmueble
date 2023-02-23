package com.example.restapiInmueble

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.restapiInmueble.databinding.ItemInmuebleBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InmuebleViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemInmuebleBinding.bind(view)
    fun bind(inmueble: InmuebleResponse, adapter: InmuebleAdapter){
        binding.titulo.text=inmueble.titulo
        binding.tvPrecio.text=inmueble.precio.toString()+" €"
        binding.tvMetros.text=inmueble.metrosUtiles.toString()+" m2"
        binding.tvDesc.text=inmueble.descripcion
        binding.borrarButton.setOnClickListener(){
            val inmuebleApiService = InmuebleApi.retrofit.create(InmuebleApi::class.java)
            val idInmueble = inmueble.idInmueble
            inmuebleApiService.deleteInmueble(idInmueble).enqueue(object: Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if(response.isSuccessful) {
                        adapter.removeInmueble(inmueble)
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    // Manejar error
                }
            })
        }

        binding.editarButton.setOnClickListener(){
            // Crear un objeto InmuebleResponse con valores por defecto
            val inmueblePorDefecto = InmuebleResponse("piso EDITADO", 1000.0f, "Descripción", 80L, 70.0, "Ubicación", "Zona", "2022-01-01", 3L, 2L, -1)

            // Llamar a la función editarInmueble del servicio web con el id del inmueble actual y el objeto con valores por defecto
            val inmuebleApiService = InmuebleApi.retrofit.create(InmuebleApi::class.java)
            inmuebleApiService.editarInmueble(inmueble.idInmueble, inmueblePorDefecto).enqueue(object:
                Callback<InmuebleResponse> {
                override fun onResponse(call: Call<InmuebleResponse>, response: Response<InmuebleResponse>) {
                    if(response.isSuccessful) {
                        // Actualizar la lista de inmuebles con el objeto editado
                        val inmuebleEditado = response.body()
                        if (inmuebleEditado != null) {
                            adapter.updateInmueble(inmueble, inmuebleEditado)
                        }
                    }
                }

                override fun onFailure(call: Call<InmuebleResponse>, t: Throwable) {
                    // Manejar error
                }
            })
        }
    }
}
