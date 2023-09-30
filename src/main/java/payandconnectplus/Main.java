package payandconnectplus;


import io.github.cdimascio.dotenv.Dotenv;

public class Main {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure()
                .load();
        String studentNumber = dotenv.get("STUDENT_NUMBER");
        String studentPassword = dotenv.get("PASSWORD");


        System.out.println(dotenv.get("STUDENT_NUMBER"));






    }
}