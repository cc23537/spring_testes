package com.example.api.controllers;

import com.example.api.models.Alimento;
import com.example.api.models.AlimentoEstocado;
import com.example.api.models.AlimentoEstocadoDTO;
import com.example.api.models.Cliente;
import com.example.api.repositories.AlimentoEstocadoRepository;
import com.example.api.repositories.ClienteRepository;
import com.example.api.repositories.AlimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/alimentosestocados")
public class AlimentoEstocadoController {

    @Autowired
    private AlimentoEstocadoRepository alimentoEstocadoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private AlimentoRepository alimentoRepository;

    @PostMapping
    public ResponseEntity<AlimentoEstocado> criarAlimentoEstocado(@RequestBody AlimentoEstocado alimentoEstocado) {
        
        Optional<Cliente> clienteOptional = clienteRepository.findById(alimentoEstocado.getCliente().getIdCliente());
        if (!clienteOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
        }
    
        Optional<Alimento> alimentoOptional = alimentoRepository.findById(alimentoEstocado.getAlimentoASerEstocado().getIdAlimento());
        Alimento alimento;
        if (alimentoOptional.isPresent()) {
            alimento = alimentoOptional.get(); 
        } else {
            alimento = alimentoEstocado.getAlimentoASerEstocado();
            alimento.setCliente(clienteOptional.get()); 
            alimento = alimentoRepository.save(alimento); 
        }
    
        alimentoEstocado.setAlimentoASerEstocado(alimento);
        alimentoEstocado.setCliente(clienteOptional.get()); 
        // A quantidade de estoque deve ser definida aqui
        if (alimentoEstocado.getQuantidadeEstoque() <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Não permitir quantidade negativa ou zero
        }
        
        AlimentoEstocado alimentoEstocadoSalvo = alimentoEstocadoRepository.save(alimentoEstocado);
    
        return new ResponseEntity<>(alimentoEstocadoSalvo, HttpStatus.CREATED);
    }
    
    @PutMapping("/{idEstoque}")
    public ResponseEntity<AlimentoEstocado> atualizarAlimentoEstocado(@PathVariable int idEstoque, @RequestBody AlimentoEstocado alimentoEstocadoAtualizado) {
        Optional<AlimentoEstocado> alimentoEstocadoOptional = alimentoEstocadoRepository.findById(idEstoque);
        
        if (!alimentoEstocadoOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
        }

        AlimentoEstocado alimentoEstocado = alimentoEstocadoOptional.get();
        
        // Atualiza as propriedades
        alimentoEstocado.setEspecificacoes(alimentoEstocadoAtualizado.getEspecificacoes());
        alimentoEstocado.setValidade(alimentoEstocadoAtualizado.getValidade());
        alimentoEstocado.setAlimentoASerEstocado(alimentoEstocadoAtualizado.getAlimentoASerEstocado());
        alimentoEstocado.setCliente(alimentoEstocadoAtualizado.getCliente());

        // Valida a quantidade de estoque
        if (alimentoEstocadoAtualizado.getQuantidadeEstoque() <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Não permitir quantidade negativa ou zero
        }
        
        alimentoEstocado.setQuantidadeEstoque(alimentoEstocadoAtualizado.getQuantidadeEstoque());

        AlimentoEstocado alimentoEstocadoSalvo = alimentoEstocadoRepository.save(alimentoEstocado);
        return new ResponseEntity<>(alimentoEstocadoSalvo, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<AlimentoEstocado>> listarTodosAlimentosEstocados() {
        List<AlimentoEstocado> alimentosEstocados = alimentoEstocadoRepository.findAll();
        return new ResponseEntity<>(alimentosEstocados, HttpStatus.OK);
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<String>> getAlimentosByCliente(@PathVariable int idCliente) {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(idCliente);
        List<AlimentoEstocado> alimentosEstocados = alimentoEstocadoRepository.findByCliente(cliente);
        List<String> nomesAlimentos = alimentosEstocados.stream()
                .map(ae -> ae.getAlimentoASerEstocado().getNomeAlimento())
                .collect(Collectors.toList());
        return ResponseEntity.ok(nomesAlimentos);
    }
    @GetMapping("/{id}")
    public int getMethodName(@PathVariable int id) {
        Cliente cliente = new Cliente();
        cliente = clienteRepository.findById(id).get();
        List<AlimentoEstocado> alimentosEstocados = alimentoEstocadoRepository.findByCliente(cliente);
        return alimentosEstocados.size();
    }

    @GetMapping("/proxdatavalidade/{id}")
    public ResponseEntity<AlimentoEstocadoDTO> getProximoAlimentoAVencer(@PathVariable int id) {
        Optional<Cliente> clienteOptional = clienteRepository.findById(id);
        if (!clienteOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Cliente cliente = clienteOptional.get();
        Optional<AlimentoEstocado> alimento = alimentoEstocadoRepository.findFirstByClienteOrderByValidadeAsc(cliente);

        if (!alimento.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        AlimentoEstocado alimentoEstocado = alimento.get();
        AlimentoEstocadoDTO alimentoEstocadoDTO = new AlimentoEstocadoDTO(
            alimentoEstocado.getAlimentoASerEstocado().getNomeAlimento(),
            alimentoEstocado.getValidade().toString(),
            alimentoEstocado.getQuantidadeEstoque(),
            alimentoEstocado.getAlimentoASerEstocado().getCalorias(),
            alimentoEstocado.getEspecificacoes()
        );

        return ResponseEntity.ok(alimentoEstocadoDTO);
    }

    @DeleteMapping("/delete/{nomealimento}/{validade}/{idCliente}")
    public ResponseEntity<Void> deletarAlimentoEstocado(
            @PathVariable String nomeAlimento, 
            @PathVariable String validade, 
            @PathVariable int idCliente) {
        
        LocalDate dataValidade = LocalDate.parse(validade);
        Optional<AlimentoEstocado> alimentoEstocadoOptional = alimentoEstocadoRepository.findByAlimentoASerEstocado_NomeAlimentoAndValidadeAndCliente_IdCliente(nomeAlimento, dataValidade, idCliente);
        
        if (!alimentoEstocadoOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
        }

        alimentoEstocadoRepository.delete(alimentoEstocadoOptional.get());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    




    
}