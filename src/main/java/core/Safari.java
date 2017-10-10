package core;

import org.openqa.selenium.*;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.math.*;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;
import java.util.logging.*;
import java.util.regex.*;



public class Safari {


    public static void main(String[] args) throws InterruptedException {
        Logger.getLogger("").setLevel(Level.OFF);
        String url = "http://alex.academy/exe/payment/indexE.html";
        String driverPath = "";
        if (!System.getProperty("os.name").contains("Mac")) {throw new IllegalArgumentException("Safari is available only on Mac");}

        WebDriver driver = new SafariDriver();
        //driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();

        WebDriverWait wait = new WebDriverWait(driver, 15);

        driver.get(url);
//$1,654.55
        //WebElement monthly_paymant = driver.findElement(By.id("id_monthly_payment"));
        //String string_monthly_payment = (String) ((JavascriptExecutor) driver).executeScript("arguments[0].getText();", monthly_paymant);
        String string_monthly_payment = driver.findElement(By.id("id_monthly_payment")).getText();
        String regex = "^"
                + "(?:\\$)?"
                + "(?:\\s*)?"
                + "((?:\\d{1,3})(?:\\,)?(?:\\d{3})?(?:\\.)?(\\d{0,2})?)"
                + "$";
        //String regex = "(1,654.55)";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(string_monthly_payment);
        m.find(); // 1,654.55
        double monthly_payment = Double.parseDouble(m.group(1).replaceAll(",", ""));
// 1654.55 * 12 = 19854.60
        double annual_payment = new BigDecimal(monthly_payment * 12).setScale(2, RoundingMode.HALF_UP).doubleValue();
        DecimalFormat df = new DecimalFormat("0.00"); // 19854.6 =>19854.60
        String f_annual_payment = df.format(annual_payment);
        driver.findElement(By.id("id_annual_payment")).sendKeys(String.valueOf(f_annual_payment));
        WebElement id_validate_button = driver.findElement(By.id("id_validate_button"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", id_validate_button);
        String actual_result = driver.findElement(By.id("id_result")).getText();
        System.out.println("Browser is: Safari");
        System.out.println("String: \t" + string_monthly_payment);
        System.out.println("Annual Payment: " + f_annual_payment);
        System.out.println("Result: \t" + actual_result);
        driver.quit();
    }
}

