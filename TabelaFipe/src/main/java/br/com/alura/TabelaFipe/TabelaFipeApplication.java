package br.com.alura.TabelaFipe;

import br.com.alura.TabelaFipe.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

public class TabelaFipeApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(TabelaFipeApplication.class, args);

    }
    @Override
    public void run(String... args) throws Exception {
        Principal principal = new Principal();
        principal.exibeMenu();
    }
}