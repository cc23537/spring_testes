package com.example.api.controllers;

import com.example.api.models.Alimento;
import com.example.api.models.Cliente;
import com.example.api.models.Compras;
import com.example.api.repositories.ComprasRepository;
import com.example.api.repositories.ClienteRepository;
import com.example.api.repositories.AlimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/compras")
public class ComprasController {

    @Autowired
    private ComprasRepository comprasRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private AlimentoRepository alimentoRepository;

    @PostMapping
    public ResponseEntity<Compras> criarCompra(@RequestBody Compras compra) {
        // Verifica se o cliente existe
        Optional<Cliente> clienteOptional = clienteRepository.findById(compra.getCliente().getIdCliente());
        if (clienteOptional.isPresent()) {
            // Verifica se o alimento já existe
            Optional<Alimento> alimentoOptional = alimentoRepository.findByNomeAlimento(compra.getAlimentoASerComprado().getNomeAlimento());
            Alimento alimento;
            
            if (alimentoOptional.isPresent()) {
                alimento = alimentoOptional.get();
            } else {
                // Se o alimento não existir, cria um novo
                alimento = new Alimento();
                alimento.setNomeAlimento(compra.getAlimentoASerComprado().getNomeAlimento());
                alimento.setCalorias(compra.getAlimentoASerComprado().getCalorias());
                // Aqui você pode associar o cliente se necessário
                alimento.setCliente(clienteOptional.get()); // Associa o cliente ao alimento
                alimento = alimentoRepository.save(alimento); // Salva o novo alimento
            }
            
            // Associa o alimento e o cliente à compra
            compra.setAlimentoASerComprado(alimento);
            compra.setCliente(clienteOptional.get());
            
            // Salva a compra
            Compras compraSalva = comprasRepository.save(compra);
            return new ResponseEntity<>(compraSalva, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
    }

    @DeleteMapping("/{idCompra}/alimento/{idAlimento}")
    public ResponseEntity<Void> removerAlimentoDeCompra(@PathVariable int idCompra, @PathVariable int idAlimento) {
        Optional<Compras> compraOptional = comprasRepository.findById(idCompra);
        if (compraOptional.isPresent()) {
            Compras compra = compraOptional.get();

            if (compra.getAlimentoASerComprado().getIdAlimento() == idAlimento) {
                compra.setAlimentoASerComprado(null);
                comprasRepository.save(compra);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{idCompra}")
    public ResponseEntity<Compras> atualizarCompra(@PathVariable int idCompra, @RequestBody Compras compraAtualizada) {
        return comprasRepository.findById(idCompra)
                .map(compra -> {
                    compra.setAlimentoASerComprado(compraAtualizada.getAlimentoASerComprado());
                    compra.setQuantidade(compraAtualizada.getQuantidade());
                    if (compraAtualizada.getCliente() != null) {
                        compra.setCliente(compraAtualizada.getCliente());
                    }

                    Compras compraSalva = comprasRepository.save(compra);
                    return new ResponseEntity<>(compraSalva, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Compras>> listarComprasPorCliente(@PathVariable int id) {
        // Busca o cliente pelo ID
        Optional<Cliente> clienteOptional = clienteRepository.findById(id);
        
        // Se o cliente existir, busca as compras associadas
        if (clienteOptional.isPresent()) {
            List<Compras> compras = comprasRepository.findByCliente(clienteOptional.get());
            return ResponseEntity.ok(compras);
        } else {
            // Retorna 404 Not Found se o cliente não existir
            return ResponseEntity.notFound().build();
        }
    }
}
