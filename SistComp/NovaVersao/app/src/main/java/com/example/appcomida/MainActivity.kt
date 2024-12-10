package com.example.appcomida

import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import okhttp3.RequestBody.Companion.toRequestBody
import android.util.Log
import org.json.JSONObject
import org.json.JSONArray
import android.view.View
import okhttp3.OkHttpClient
import okhttp3.*
import java.io.IOException
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.fragment.NavHostFragment
import com.example.appcomida.databinding.ActivityMainBinding
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Button
import android.widget.TextView



import android.widget.ImageView
import layout.NavHeader

import org.tensorflow.lite.Interpreter

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var fruitDetection: FruitDetection
    private lateinit var interpreter: Interpreter
    private lateinit var imageView: ImageView

    private lateinit var resultTextView: TextView // Corrigido aqui



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_ajuda)

        // Inicializar componentes do layout
        imageView = findViewById(R.id.imageView)

        resultTextView = findViewById(R.id.resultTextView)

        val btnAbrirCamera = findViewById<Button>(R.id.btnAbrirCamera)

        // Inicializar o modelo TensorFlow Lite
        fruitDetection = FruitDetection(this)
        interpreter = fruitDetection.loadModel()

        // Carregar uma imagem (de assets, por exemplo)
        val bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.macateste)

        // Detectar frutas na imagem
        val detectionResults = fruitDetection.detectFruit(bitmap, interpreter)

        // Exibir a imagem original
        imageView.setImageBitmap(bitmap)

        // Exibir os resultados na TextView
        resultTextView.text = detectionResults.joinToString("\n")

        // Exemplo de ação para o botão (pode abrir a câmera, galeria, etc.)
        btnAbrirCamera.setOnClickListener {
            // Ação para abrir a câmera ou carregar outra imagem
            // Por exemplo, abra a câmera e use a imagem retornada para detecção
        }


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar a Toolbar
        setSupportActionBar(binding.toolbar)

        // Obter o NavHostFragment inicial (nav_host_start)
        val navHostStartFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_start) as NavHostFragment

        // Obter o NavHostFragment principal (nav_host_mobile)
        val navHostMobileFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_mobile) as NavHostFragment

        // Configurar o AppBarConfiguration com todos os destinos de nível superior
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_home, R.id.nav_list, R.id.nav_calendario, R.id.ajudaFragment2, R.id.ReceitasFragment),
            binding.drawerLayout
        )

        // Configure a Toolbar com o NavController correto (nav_host_mobile)
        setupActionBarWithNavController(navHostMobileFragment.navController, appBarConfiguration)

        // Configure o NavigationView com o NavController correto (nav_host_mobile)
        binding.navView.setupWithNavController(navHostMobileFragment.navController)

        // Listener para controlar a visibilidade do Toolbar e Drawer
        navHostStartFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splashFragment, R.id.loginFragment, R.id.registroFragment -> hideToolbarAndDrawer()
                else -> showToolbarAndDrawer()
            }
        }
    }

    
    //gpt para as receitas
    val question = "banana"// aqui precisa ser passado os alimentos presentes no carrinho, coloquei banana só como exemplo
       Toast.makeText(this,question, Toast.LENGTH_SHORT).show()
        if(question.isNotEmpty()){ 
           getResponse(question) { response ->
              runOnUiThread {
                    txtResponse.text = response    //  PARTE PARA EXIBIR NA TELA
               }
            }
        }
    }



    private val client = OkHttpClient()
    fun getResponse(callback: (String) -> Unit) {
        // Variável já existente no código
        val alimentos = question //banana como exemplo, porem tem que colocar aqui de alguma forma os alimentos disponiveis





        // Configurações da API
        val apiKey = "sk-proj-0vX3DHj0PfkFhT-vJMyf43aHT1nT1YhlpNbMwtTw2FEmZzdcS_5rrRQE3du9js8kv6tFUd7VcNT3BlbkFJVp4INvOxmnryAnesZ9QUSCN_9m8kc7De54VkR75O68A2fkl1HhV5fleuAs3k_CBIf8DOcDEUQAsk-proj-0vX3DHj0PfkFhT-vJMyf43aHT1nT1YhlpNbMwtTw2FEmZzdcS_5rrRQE3du9js8kv6tFUd7VcNT3BlbkFJVp4INvOxmnryAnesZ9QUSCN_9m8kc7De54VkR75O68A2fkl1HhV5fleuAs3k_CBIf8DOcDEUQA"
        val url = "https://api.openai.com/v1/engines/text-davinci-003/completions"

        // Corpo da solicitação
        val requestBody = """
        {
            "prompt": "monte uma receita com os seguintes alimentos $alimentos",
            "max_tokens": 500,
            "temperature": 0
        }
    """.trimIndent()

        // Construção da solicitação
        val request = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $apiKey")
            .post(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
            .build()

        // Realização da chamada à API
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("error", "API failed", e)
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                if (body != null) {
                    Log.v("data", body)
                } else {
                    Log.v("data", "empty")
                }
                val jsonObject = JSONObject(body)
                val jsonArray: JSONArray = jsonObject.getJSONArray("choices")
                val textResult = jsonArray.getJSONObject(0).getString("text")
                callback(textResult)
            }
        })
    }//aqui acaba a parte da IA de receitas

    private fun hideToolbarAndDrawer() {
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        supportActionBar?.hide()
    }

    private fun showToolbarAndDrawer() {
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        supportActionBar?.show()
    }

    fun navigateToMain() {
        // Troca o NavHostFragment de navi_start para mobile_navigation
        val navHostStart = findViewById<FragmentContainerView>(R.id.nav_host_start)
        val navHostMobile = findViewById<FragmentContainerView>(R.id.nav_host_mobile)

        navHostStart.visibility = View.GONE
        navHostMobile.visibility = View.VISIBLE

        // Navega para a tela principal no mobile_navigation
        val navHostMobileFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_mobile) as NavHostFragment
        navHostMobileFragment.navController.navigate(R.id.nav_home)

        showToolbarAndDrawer()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_mobile)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
