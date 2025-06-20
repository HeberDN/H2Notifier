package com.heber.hh.H2Notifier.service;

import com.heber.hh.H2Notifier.dto.PessoaRequest;
import com.heber.hh.H2Notifier.dto.PessoaResponse;
import com.heber.hh.H2Notifier.exceptions.ResourceNotFoundException;
import com.heber.hh.H2Notifier.model.Pessoa;
import com.heber.hh.H2Notifier.model.TipoPessoa;
import com.heber.hh.H2Notifier.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PessoaService {

    private final PessoaRepository pessoaRepository;
    private final ModelMapper modelMapper;

    public PessoaResponse salvarNovaPessoa(PessoaRequest novaPessoa){

        Pessoa pessoa = modelMapper.map(novaPessoa, Pessoa.class);
        Pessoa pessoaSalva = pessoaRepository.save(pessoa);
        return modelMapper.map(pessoaSalva, PessoaResponse.class);
    }

    public List<PessoaResponse> listarTodasAsPessoas(){
        List<Pessoa> pessoas = pessoaRepository.findAll();
        return pessoas.stream()
                .map(pessoa -> modelMapper.map(pessoa, PessoaResponse.class))
                .collect(Collectors.toList());
    }

    public Pessoa buscarPessoaPorId (Long id){
        return pessoaRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Pessoa não encontrada com id: " + id));
    }

    public void deletarPessoa(Long id){
        Pessoa pessoa = buscarPessoaPorId(id);
        pessoaRepository.delete(pessoa);
    }

    public PessoaResponse editarPessoa(Long id, PessoaRequest pessoaAtualizada){
        Pessoa pessoaExistente = buscarPessoaPorId(id);
        modelMapper.map(pessoaAtualizada, pessoaExistente);
        Pessoa pessoaSalva = pessoaRepository.save(pessoaExistente);
        return modelMapper.map(pessoaSalva, PessoaResponse.class);
    }

    public List<PessoaResponse> listarPessoasPorTipoPessoa(TipoPessoa tipoPessoa) {
        List<Pessoa> pessoaListPorTipoPessoa = pessoaRepository.findAllByTipoPessoa(tipoPessoa);
        return pessoaListPorTipoPessoa.stream()
                .map(pessoa -> modelMapper.map(pessoa, PessoaResponse.class))
                .collect(Collectors.toList());
    }
}
