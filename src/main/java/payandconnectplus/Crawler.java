package payandconnectplus;

import com.microsoft.cognitiveservices.speech.ResultReason;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechRecognizer;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;
import com.microsoft.cognitiveservices.speech.audio.AudioProcessingConstants;
import com.microsoft.cognitiveservices.speech.audio.AudioProcessingOptions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


public class Crawler {
    private final String studentNumber;
    private final String password;
    private final WebDriver driver;
    private WebDriverWait wait;
    public Crawler(String studentNumber, String password) {

        this.driver = new ChromeDriver();
        this.studentNumber = studentNumber;
        this.password = password;
        this.wait = new WebDriverWait(driver , Duration.ofSeconds(30));
        driver.get("https://app.payandconnect.co.za/login");
    }

    public void run() {
        signIn();
// navigate to the voucher page
        wait.until( )
        requestVoucher();
    }

    private void signIn() {
        WebElement studentNumberInput = driver.findElement(By.id("user"));
        WebElement passwordInput = driver.findElement(By.id("password"));
        WebElement signingButton = driver.findElement(By.id("submit-button"));

        studentNumberInput.sendKeys(studentNumber);
        passwordInput.sendKeys(password);
        signingButton.click();

// wait for recaptcha and press it
        var wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt((By.xpath("//iframe[@title='reCAPTCHA"))));
        var captchaCheckbox = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='recaptcha-checkbox-border']")));
        captchaCheckbox.click();
        driver.switchTo().defaultContent();

        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.xpath("//iframe[@title='recaptcha challenge expires in two minutes']")));
        var audioOptionButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("recaptcha-audio-button")));
        audioOptionButton.click();


        var audioProcessingOptions = AudioProcessingOptions.create(AudioProcessingConstants.AUDIO_INPUT_PROCESSING_ENABLE_DEFAULT);
        var audioInput = AudioConfig.fromDefaultMicrophoneInput(audioProcessingOptions);
        List<String> recognizedSpeechParts = new ArrayList<>();

        SpeechConfig config = SpeechConfig.fromSubscription("hrllp", "gfsdjkl");
        var recognizer = new SpeechRecognizer(config, audioInput);
        {
            recognizer.recognized.addEventListener((s, e) -> {
                if (e.getResult().getReason() == ResultReason.RecognizedSpeech) {
                    recognizedSpeechParts.add(e.getResult().getText());
                    System.out.println("RECOGNIZED: Text=" + e.getResult().getText());
                }
                else if (e.getResult().getReason() == ResultReason.NoMatch) {
                    System.out.println("NOMATCH: Speech could not be recognized.");
                }
            });

            // Starts continuous recognition. Uses stopContinuousRecognitionAsync() to stop recognition.
            recognizer.startContinuousRecognitionAsync().get();
            var playButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='PLAY']")));
            playButton.click();
            Thread.sleep(10000);
            recognizer.stopContinuousRecognitionAsync().get();
        }

        config.close();
        audioInput.close();
        audioProcessingOptions.close();
        recognizer.close();

        var audioResponseInput = driver.findElement(By.id("audio-response"));
        var captchaText =  String. join("", recognizedSpeechParts);
        audioResponseInput.sendKeys(captchaText);

        var verifyButton = driver.findElement(By.id("recaptcha-verify-button"));
        verifyButton.click();

        driver.switchTo().defaultContent();
        var submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("wpforms-submit-3347")));
        submitButton.click();

    }

    private void requestVoucher(){
        var requestButtonClassNames = By.cssSelector("MuiButtonBase-root MuiButton-root MuiButton-contained MuiButton-containedPrimary MuiButton-sizeSmall MuiButton-containedSizeSmall MuiButton-root MuiButton-contained MuiButton-containedPrimary MuiButton-sizeSmall MuiButton-containedSizeSmall jss286 css-1shrfnk");
        wait.until(ExceptionConditions.elementToBeClickable(requestButtonClassNames));
        WebElement requestButton = driver.findElement(requestButtonClassNames);

        requestButton.click();

    }
}
