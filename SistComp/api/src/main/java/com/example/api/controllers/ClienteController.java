package com.example.api.controllers;

import com.example.api.models.Alimento;
import com.example.api.models.AlimentoEstocado;
import com.example.api.models.AlimentoEstocadoDTO;
import com.example.api.models.Cliente;
import com.example.api.models.Compras;
import com.example.api.repositories.AlimentoEstocadoRepository;
import com.example.api.repositories.AlimentoRepository;
import com.example.api.repositories.ClienteRepository;
import com.example.api.repositories.ComprasRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired 
    private ComprasRepository comprasRepository;

    @Autowired
    private AlimentoRepository alimentoRepository;

    @Autowired
    private AlimentoEstocadoRepository alimentoEstocadoRepository;

 @GetMapping("/{email}/{senha}")
    public ResponseEntity<Integer> buscarClientePorEmailSenha(
        @PathVariable String email, 
        @PathVariable String senha) {
        
        Optional<Cliente> clienteOptional = clienteRepository.findByEmailAndSenha(email, senha);
        
        if (clienteOptional.isPresent()) {
            return ResponseEntity.ok(clienteOptional.get().getIdCliente());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Cliente> criarCliente(@RequestBody Cliente cliente) {
        Cliente novoCliente = clienteRepository.save(cliente);
        return ResponseEntity.ok(novoCliente);
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listarClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarClientePorId(@PathVariable int id) {
        Optional<Cliente> clienteOptional = clienteRepository.findById(id);
        if (clienteOptional.isPresent()) {
            return ResponseEntity.ok(clienteOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizarCliente(@PathVariable int id, @RequestBody Cliente clienteAtualizado) {
        Optional<Cliente> clienteOptional = clienteRepository.findById(id);
        if (clienteOptional.isPresent()) {
            Cliente cliente = clienteOptional.get();
            cliente.setNomeCliente(clienteAtualizado.getNomeCliente());
            cliente.setEmail(clienteAtualizado.getEmail());
            cliente.setSenha(clienteAtualizado.getSenha());
            cliente.setPeso(clienteAtualizado.getPeso());
            cliente.setAltura(clienteAtualizado.getAltura());
            Cliente clienteSalvo = clienteRepository.save(cliente);
            return ResponseEntity.ok(clienteSalvo);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCliente(@PathVariable int id) {
        if (clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    public ResponseEntity<List<Compras>> listarComprasPorCliente(@PathVariable int idCliente) {
        Optional<Cliente> clienteOptional = clienteRepository.findById(idCliente);
        if (clienteOptional.isPresent()) {
            List<Compras> compras = comprasRepository.findByCliente(clienteOptional.get());
            return new ResponseEntity<>(compras, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping("/{idCliente}/alimentosestocados")
    public ResponseEntity<List<AlimentoEstocadoDTO>> listarAlimentosEstocadosPorCliente(@PathVariable int idCliente) {
        Optional<Cliente> clienteOptional = clienteRepository.findById(idCliente);
        if (clienteOptional.isPresent()) {
            // Obtenha a lista de AlimentoEstocado para o cliente
            List<AlimentoEstocado> estoque = alimentoEstocadoRepository.findByCliente(clienteOptional.get());

            // Mapeie a lista de AlimentoEstocado para a lista de AlimentoEstocadoDTO
            List<AlimentoEstocadoDTO> estoqueDTO = estoque.stream().map(alimentoEstocado -> {
                String validadeFormatada = alimentoEstocado.getValidade().format(DateTimeFormatter.ISO_LOCAL_DATE);
                return new AlimentoEstocadoDTO(
                    alimentoEstocado.getAlimentoASerEstocado().getNomeAlimento(),
                    validadeFormatada,  // Validade formatada
                    alimentoEstocado.getQuantidadeEstoque(),
                    alimentoEstocado.getAlimentoASerEstocado().getCalorias(),
                    alimentoEstocado.getEspecificacoes()
                );
            }).collect(Collectors.toList());

            // Retorne a lista de AlimentoEstocadoDTO
            return new ResponseEntity<>(estoqueDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @GetMapping("/{idCliente}/alimentoscomprados")
    public ResponseEntity<List<Alimento>> listarAlimentosCompradosPorCliente(@PathVariable int idCliente) {
        Optional<Cliente> clienteOptional = clienteRepository.findById(idCliente);
        if (clienteOptional.isPresent()) {
            List<Compras> compras = comprasRepository.findByCliente(clienteOptional.get());

   
            List<Alimento> alimentosComprados = compras.stream()
                    .map(compra -> compra.getAlimentoASerComprado()) 
                    .distinct() 
                    .collect(Collectors.toList());

            return new ResponseEntity<>(alimentosComprados, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
