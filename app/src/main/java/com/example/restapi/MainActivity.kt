package com.example.restapiInmueble

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restapiInmueble.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var adapter: InmuebleAdapter
    val imuebles = mutableListOf<InmuebleResponse>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()
        mostrar()
        val recyclerView = binding.rvInmuebles
        val btnAgregar = binding.btnAgregar as? FloatingActionButton
        btnAgregar?.setOnClickListener() {
            anadir()
            val itemCount = recyclerView.adapter?.itemCount ?: 0
            recyclerView.smoothScrollToPosition(itemCount)
        }

    }
    private fun initRecyclerView(){
        adapter=InmuebleAdapter(imuebles)
        binding.rvInmuebles.layoutManager = LinearLayoutManager(this)
        binding.rvInmuebles.adapter=adapter
    }


    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.10.30.218:8080/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private fun anadir(){
        val inmueble = InmuebleResponse("Piso nuevo", 1000.0f, "Descripción", 80L, 70.0, "Ubicación", "Zona", "2023-01-01", 3L, 2L, -1)
        val call = getRetrofit().create(InmuebleApi::class.java).altaInmueble(inmueble)
        call.enqueue(object : Callback<InmuebleResponse> {
            override fun onResponse(call: Call<InmuebleResponse>, response: Response<InmuebleResponse>) {
                if (response.isSuccessful) {
                    val inmuebleResponse = response.body()
                    mostrar()
                } else {
                    showError()
                }
            }

            override fun onFailure(call: Call<InmuebleResponse>, t: Throwable) {
                showError()
            }
        })
    }
    private fun mostrar() {
        val call = getRetrofit().create(InmuebleApi::class.java).getImuebles()
        call.enqueue(object : Callback<List<InmuebleResponse>> {
            override fun onResponse(
                call: Call<List<InmuebleResponse>>,
                response: Response<List<InmuebleResponse>>
            ) {
                if (response.isSuccessful) {
                    val inmueblesI: List<InmuebleResponse>? = response.body()
                    imuebles.clear()
                    if (inmueblesI != null) {
                        imuebles.addAll(inmueblesI)
                    }
                    adapter.notifyDataSetChanged()
                } else {

                }
            }

            override fun onFailure(call: Call<List<InmuebleResponse>>, t: Throwable) {
                Log.e("ANADIR", "Error al enviar la petición", t)
                showError()
            }
        })
    }
    private fun showError() {
        Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
    }
}