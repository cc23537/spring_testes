package layout

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.appcomida.databinding.NavHeaderMainBinding

class NavHeader(private val context: Context) : AppCompatActivity() {

    private lateinit var binding: NavHeaderMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializa o binding
        binding = NavHeaderMainBinding.inflate(layoutInflater)

        // Recupera o e-mail do SharedPreferences
        val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("user_email", "E-mail não encontrado")

        // Atualiza o texto do TextView de email
        binding.email.text = "teste"
        // Retorna o layout da View
    }
}