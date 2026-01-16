package edu.pga.psp.magiccollectionspring;

import edu.pga.psp.magiccollectionspring.menus.MainMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MagicCollectionSpringApplication implements CommandLineRunner {

    @Autowired
    private MainMenu mainMenu;

    public static void main(String[] args) {
        SpringApplication.run(MagicCollectionSpringApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("--- Iniciando Magic Collection con Spring Boot ---");
        mainMenu.showMenu();
    }

}
