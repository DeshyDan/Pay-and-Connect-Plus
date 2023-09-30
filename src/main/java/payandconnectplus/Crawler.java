package payandconnectplus;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Crawler {
    private WebDriver driver;
    private final String studentNumber;
    private final String password;

    public Crawler(String studentNumber , String password){

        this.driver = new ChromeDriver();
        this.studentNumber = studentNumber;
        this.password = password;
        driver.get("https://app.payandconnect.co.za/login");
    }

    public void run(){
    signIn();
    }

    private void signIn(){
        WebElement studentNumberInput = driver.findElement(By.id("user"));
        WebElement passwordInput = driver.findElement(By.id("password"));
        WebElement signingButton = driver.findElement(By.id("submit-button"));

        studentNumberInput.sendKeys(studentNumber);
        passwordInput.sendKeys(password);
        signingButton.click();



    }
}
