package org.awsm.rscore.appannie;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.awsm.rscommons.AuthObject;
import org.awsm.rscore.exception.ParsingException;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.ErrorHandler;

import java.io.IOException;

/**
 * Created by: akirillov
 * Date: 2/19/13
 */
public class AppAnnieCrawlerJava {
    private CookieManager cookieManager;

    //base url without date specified
    private String url;

    public String crawl(String date){
        try {
            return ((HtmlPage)buildWebClient(cookieManager).getPage(url+date)).asXml();
        } catch (IOException e) {
            e.printStackTrace();  //todo: throw appropriate exception
        }
        return null;
    }

    public AppAnnieCrawlerJava(String application, String store, String rankType, AuthObject auth) throws ParsingException {
        url = "http://www.appannie.com/app/" + (store.equals("appstore")?"ios":store) + "/" + application.replaceAll(" ", "-").toLowerCase() + "/ranking/#view=" + rankType + "&date=";

        WebClient webClient = buildWebClient();

        HtmlPage page = null;
        try {
            page = webClient.getPage("https://www.appannie.com/account/login/");
        } catch (IOException e) {
            e.printStackTrace();  //todo: throw exception
        }

        HtmlForm form = page.getFormByName("");

        // authentication
        try {
            form.getInputByName("username").setValueAttribute(auth.username());
        } catch (Throwable t){
            throw new ParsingException("Login form incorrect! Please check "+getClass().getName()+" for errors.");
        }

        try {
            form.getInputByName("password").setValueAttribute(auth.password());
        } catch (Throwable t){
            throw new ParsingException("Login form incorrect! Please check "+getClass().getName()+" for errors.");
        }

        try {
            form.getButtonByName("").click();
        } catch (IOException e) {
            throw new ParsingException("Login form incorrect! Please check "+getClass().getName()+" for errors.");
        }

        cookieManager = webClient.getCookieManager();
    }

    public static WebClient buildWebClient(CookieManager manager){
        WebClient webClient = buildWebClient();

        webClient.setCookieManager(manager);

        return webClient;
    }

    public static WebClient buildWebClient(){
        WebClient webClient = new WebClient(BrowserVersion.FIREFOX_10);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.getOptions().setCssEnabled(true);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setUseInsecureSSL(true);

        webClient.setCssErrorHandler(new ErrorHandler() {
            @Override
            public void warning(CSSParseException exception) throws CSSException {
                //nothing to do here
            }

            @Override
            public void error(CSSParseException exception) throws CSSException {
                //todo: log or throw exception
            }

            @Override
            public void fatalError(CSSParseException exception) throws CSSException {
                //todo: log or throw exception
            }
        });

        webClient.setIncorrectnessListener(new IncorrectnessListener() {
            @Override
            public void notify(String message, Object origin) {
                //todo: analyze and throw exception
            }
        });

        webClient.waitForBackgroundJavaScript(100000);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setRedirectEnabled(true);

        return webClient;
    }
}
