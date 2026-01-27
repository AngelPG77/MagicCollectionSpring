package pga.magiccollectionspring.cli;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ConsoleInterface implements CommandLineRunner {

    private final UserSession session;
    private final AuthMenu authMenu;
    private final MainMenu mainMenu;

    @Autowired
    public ConsoleInterface(UserSession session, AuthMenu authMenu, MainMenu mainMenu) {
        this.session = session;
        this.authMenu = authMenu;
        this.mainMenu = mainMenu;
    }

    @Override
    public void run(String... args) throws Exception {
        new Thread(this::startMenuLoop).start();
    }

    private void startMenuLoop() {
        try { Thread.sleep(2000); } catch (InterruptedException e) {}

        System.out.println("\n\n===========================================");
        System.out.println("   MAGIC COLLECTION - TERMINAL CLIENT");
        System.out.println("===========================================");

        while (true) {
            if (!session.isLoggedIn()) {
                authMenu.show();
            } else {
                mainMenu.show();
            }
        }
    }
}
