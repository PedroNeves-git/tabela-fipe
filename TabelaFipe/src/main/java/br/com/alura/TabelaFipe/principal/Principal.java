package br.com.alura.TabelaFipe.principal;

import br.com.alura.TabelaFipe.model.Dados;
import br.com.alura.TabelaFipe.model.Modelos;
import br.com.alura.TabelaFipe.model.Veiculo;
import br.com.alura.TabelaFipe.service.ConsumoApi;
import br.com.alura.TabelaFipe.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private Scanner sc = new Scanner(System.in);

    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();

    private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";
    public void exibeMenu() {
        var menu = """
                *** OPÇÕES ***
                CARRO
                MOTO
                CAMINHÃO
                                
                Digite uma das opções para consultar:
                                
                """;

        System.out.println(menu);
        var opcao = sc.nextLine();

        String endereco;
        if (opcao.toLowerCase().contains("carro")) {
            endereco = URL_BASE + "carros/marcas";
        } else if (opcao.toLowerCase().contains("moto")) {
            endereco = URL_BASE + "motos/marcas";
        } else {
            endereco = URL_BASE + "caminhoes/marcas";
        }

        var json = consumo.obterDados(endereco);
        System.out.println(json);
        var marcas = conversor.obterLista(json, Dados.class);
        marcas.stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("Informe o código da Marca para consulta: ");
        var codigoMarca = sc.nextLine();

        endereco = endereco + "/" + codigoMarca + "/modelos";
        json = consumo.obterDados(endereco);
        var modeloLista = conversor.obterDados(json, Modelos.class);

        System.out.println("Modelos dessa marca: ");
        modeloLista.modelos().stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("Digite o nome do carro desejado: ");
        var nomeVeiculo = sc.nextLine().toLowerCase();

        List<Dados> modelosFiltrados = modeloLista.modelos().stream()
                .filter(m -> m.nome().toLowerCase().contains(nomeVeiculo))
                .collect(Collectors.toList());

        System.out.println("Modelos Filtrados");
        if (modelosFiltrados.isEmpty()) {
            System.out.println("Nenhum modelo encontrado com o nome especificado.");
        } else {
            modelosFiltrados.forEach(System.out::println);
        }

        System.out.println("Digite o código do modelo desejado: ");
        var codigoModelo = sc.nextLine();

        endereco = endereco + "/" + codigoModelo + "/anos";
        json = consumo.obterDados(endereco);
        List<Dados> anos = conversor.obterLista(json, Dados.class);
        List<Veiculo> veiculos = new ArrayList<>();

        for (int i = 0; i < anos.size(); i++) {
            var enderecoAnos = endereco + "/" + anos.get(i).codigo();
            json = consumo.obterDados(enderecoAnos);
            Veiculo veiculo = conversor.obterDados(json, Veiculo.class);
            veiculos.add(veiculo);
        }

        System.out.println("\nVeículos filtrados por ano: ");
        veiculos.forEach(System.out::println);

        boolean voltarMenu = false;
        while (!voltarMenu) {
            System.out.println("\nDeseja voltar ao menu inicial ou ao menu anterior?");
            System.out.println("1 - Menu inicial");
            System.out.println("2 - Menu anterior");
            System.out.println("3 - Sair");

            var escolha = sc.nextLine();
            switch (escolha) {
                case "1":
                    voltarMenu = true; // Sai do loop interno e volta ao menu inicial
                    break;
                case "2":
                    voltarMenu = true; // Sai do loop interno e repete a consulta da mesma marca
                    break;
                case "3":
                    System.out.println("Saindo...");
                    return; // Encerra o programa
                default:
                    System.out.println("Opção inválida, tente novamente.");
                    break;
            }

        }
    }
}
