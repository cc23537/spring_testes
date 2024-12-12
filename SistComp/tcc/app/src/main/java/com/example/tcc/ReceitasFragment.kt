package com.example.tcc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tcc.api.getRetrofit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import retrofit2.http.GET
import retrofit2.http.Path

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ReceitasFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var userViewModel: ClienteViewModel

    interface ApiService {
        @GET("/alimentosestocados/cliente/{id}")
        fun getAlimentoEstocadoC(@Path("id") id: Int): retrofit2.Call<List<String>>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_receitas, container, false)
        userViewModel = ViewModelProvider(requireActivity()).get(ClienteViewModel::class.java)
        val idcliente = userViewModel.clienteId.value

        val retrofit = getRetrofit()
        val apiService = retrofit.create(ApiService::class.java)

        val recyclerView: RecyclerView = view.findViewById(R.id.receitasRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        idcliente?.let { id ->
            CoroutineScope(Dispatchers.IO).launch {
                val response = apiService.getAlimentoEstocadoC(id).execute()
                if (response.isSuccessful) {
                    val alimentos = response.body() ?: listOf()
                    getReceitas(alimentos) { receitas ->
                        activity?.runOnUiThread {
                            recyclerView.adapter = RecipeAdapter(receitas)
                        }
                    }
                }
            }
        }

        return view
    }

    private fun getReceitas(alimentos: List<String>, callback: (List<String>) -> Unit) {
        val client = OkHttpClient()
        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=AIzaSyApayH2H5xMN5eoRDucugFWex38HVNcRJw"

        // Create a single string from the list of alimentos
        val alimentosString = alimentos.joinToString(separator = ", ")

        val json = """
    {
      "contents": [{
        "parts":[{"text": "Generate Recipes using the following ingredients, give only the name of the recipe: $alimentosString"}]
        }]
       }
    """.trimIndent()

        val body = RequestBody.create("application/json; charset=utf-8".toMediaType(), json)
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    val responseBody = response.body
                    if (!response.isSuccessful) {
                        println("Request failed with code: ${response.code}")
                        responseBody?.let {
                            val responseString = it.string()
                            println("Response body: $responseString")
                        }
                    } else {
                        responseBody?.let {
                            val responseString = it.string()
                            try {
                                val jsonResponse = JSONObject(responseString)
                                val receitasJson = jsonResponse.getJSONArray("candidates")
                                    .getJSONObject(0).getJSONObject("content")
                                    .getJSONArray("parts").getJSONObject(0).getString("text")
                                val receitasList = receitasJson.split("\n").filter { it.isNotBlank() }
                                callback(receitasList)
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        } ?: run {
                            println("Response body is null")
                        }
                    }
                }
            })
        }
    }









    companion object {
        fun newInstance(param1: String, param2: String) =
            ReceitasFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
