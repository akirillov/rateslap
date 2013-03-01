package org.awsm.rscore;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.awsm.rscommons.AuthObject;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.ErrorHandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by: akirillov
 * Date: 1/31/13
 */
public class TestClass {
    public static void main(String ... args) throws IOException {
        testWebClient();
    }

    public static void testWebClient() throws IOException {
        String date = "2012-02-01";

        String application = "Cut The Rope";
        String store = "appstore";
        String rankType = "ranks";
        AuthObject auth =  new AuthObject("akirillov@zeptolab.com", "7ru57n01");
//  lazy val crawler = new AppAnnieCrawler("Cut The Rope", "appstore", "ranks", AuthObject("akirillov@zeptolab.com", "7ru57n01"))


        WebClient webClient = buildWebClient();


        HtmlPage page = webClient.getPage("https://www.appannie.com/account/login/");
        HtmlForm form = page.getFormByName("");

        // authentication
        try {
            form.getInputByName("username").setValueAttribute(auth.username());
        } catch (Throwable t){
            t.printStackTrace();
        }

        try {
            form.getInputByName("password").setValueAttribute(auth.password());
        } catch (Throwable t){
            t.printStackTrace();
        }

        //TODO: consider the case when no page found  -> should verify this situation and provide warning or error

        form.getButtonByName("").click(); //TODO: CATCH AUTH FAIL HERE by String "wrong username or password"
        System.out.println("----------------------------------------------> FORM SUBMITTED");

        final CookieManager cm = webClient.getCookieManager();


//        WebClient webClient2 = buildWebClient();


        String url = "http://www.appannie.com/app/" + (store.equals("appstore")?"ios":store) + "/" + application.replaceAll(" ", "-").toLowerCase() + "/ranking/#view=" + rankType + "&date=";


        ExecutorService executor = Executors.newCachedThreadPool();
        for(int i=1; i<4; i++){
              executor.submit(new WebClientRunnable(url, cm, i));
          /*  try{
                String xml = ((HtmlPage)buildWebClient(cm).getPage(url+"2012-02-0"+i)).asXml();

                // Create file
                FileWriter fstream = new FileWriter("/tmp/parse/Thread_"+i+".xml");
                BufferedWriter out = new BufferedWriter(fstream);
                out.write(xml);
                //Close the output stream
                out.close();
            }catch (Exception e){//Catch exception if any
                System.err.println("Error: " + e.getMessage());
            }*/
        }
        
       /* new TestThread("")
        
        
        String scala.scala.xml =  ((HtmlPage)webClient.getPage(url+date)).asXml();

        String xml2 = ((HtmlPage)webClient.getPage(url+"2012-02-02")).asXml();

        System.out.println(scala.xml +"\n\n\n\nxml2 : -------------------------------------------------->\n"+xml2+"\n\n EQUALS? => "+ scala.xml.equals(xml2));*/
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
        webClient.getOptions().setThrowExceptionOnScriptError(false);

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

        return webClient;
    }

    
    static class WebClientRunnable implements Runnable {
        String url;
        CookieManager cm;
        int i;

        WebClientRunnable(String url, CookieManager cm, int i) {
            this.url = url;
            this.cm = cm;
            this.i = i;
        }

        @Override
        public void run() {
            try{
                String xml = ((HtmlPage)buildWebClient(cm).getPage(url+"2012-02-0"+i)).asXml();

                // Create file
                FileWriter fstream = new FileWriter("/tmp/parse/Thread_"+i+".xml");
                BufferedWriter out = new BufferedWriter(fstream);
                out.write(xml);
                //Close the output stream
                out.close();
            }catch (Exception e){//Catch exception if any
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
}
