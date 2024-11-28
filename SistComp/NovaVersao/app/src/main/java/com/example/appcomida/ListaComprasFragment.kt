import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.appcomida.R


class ListaComprasFragment : Fragment() {

    private lateinit var listViewAlimentos: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lista, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //listViewAlimentos = view.findViewById(R.id.listViewAlimentos)

        // Dados de exemplo
        val alimentos = listOf("Maçã", "Banana", "Laranja")

        //Usando ArrayAdapter para preencher o ListView
        /*val adapter = ArrayAdapter(
            requireContext(),
            R.layout.item_alimento,
            R.id.itemNome,
            alimentos
        )

        listViewAlimentos.adapter = adapter*/
    }
}
