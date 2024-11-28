package com.example.api.controllers;

import com.example.api.models.Alimento;
import com.example.api.models.AlimentoEstocado;
import com.example.api.models.Cliente;
import com.example.api.repositories.AlimentoEstocadoRepository;
import com.example.api.repositories.ClienteRepository;
import com.example.api.repositories.AlimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
        AlimentoEstocado alimentoEstocadoSalvo = alimentoEstocadoRepository.save(alimentoEstocado);
    
        return new ResponseEntity<>(alimentoEstocadoSalvo, HttpStatus.CREATED);
    }
    
    @PutMapping("/{idEstoque}")
    public ResponseEntity<AlimentoEstocado> atualizarAlimentoEstocado(@PathVariable int idEstoque, @RequestBody AlimentoEstocado alimentoEstocadoAtualizado) {
        return alimentoEstocadoRepository.findById(idEstoque)
                .map(alimentoEstocado -> {
                    alimentoEstocado.setEspecificacoes(alimentoEstocadoAtualizado.getEspecificacoes());
                    alimentoEstocado.setValidade(alimentoEstocadoAtualizado.getValidade());
                    alimentoEstocado.setAlimentoASerEstocado(alimentoEstocadoAtualizado.getAlimentoASerEstocado());
                    alimentoEstocado.setCliente(alimentoEstocadoAtualizado.getCliente());

                    AlimentoEstocado alimentoEstocadoSalvo = alimentoEstocadoRepository.save(alimentoEstocado);
                    return new ResponseEntity<>(alimentoEstocadoSalvo, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)); 
    }

    @DeleteMapping("/{idEstoque}")
    public ResponseEntity<Void> removerAlimentoEstocado(@PathVariable int idEstoque) {
        if (alimentoEstocadoRepository.existsById(idEstoque)) {
            alimentoEstocadoRepository.deleteById(idEstoque);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<AlimentoEstocado>> listarTodosAlimentosEstocados() {
        List<AlimentoEstocado> alimentosEstocados = alimentoEstocadoRepository.findAll();
        return new ResponseEntity<>(alimentosEstocados, HttpStatus.OK);
    }
}
