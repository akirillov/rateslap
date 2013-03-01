package org.awsm.rscore.appannie

import java.net.URL
import com.gargoylesoftware.htmlunit.html.{HtmlInput, HtmlForm, HtmlPage}
import com.gargoylesoftware.htmlunit._
import scala.xml.XML
import org.awsm.rscommons.AuthObject
import org.awsm.rscore.exception.ParsingException
import org.w3c.css.sac.{CSSParseException, ErrorHandler}


/**
 * Created by: akirillov
 * Date: 10/23/12
 */

class AppAnnieCrawler(val appName: String, val store: String, val rankType: String, auth: AuthObject) {

  //base url without date specified
  private val url = "http://www.appannie.com/app/" + (if(store.equals("appstore")){"ios"} else {store}) + "/" + appName.replaceAll(" ", "-").toLowerCase + "/ranking/#view=" + rankType + "&date="

  private val cookieManager = authenticate(auth)

  //todo: add exceptions throwing and test invalid parameters
  def crawl(date: String):String = {
    try{
      buildWebClient(cookieManager).getPage(url+date).asInstanceOf[HtmlPage].asXml()
    } catch {
      case _:Throwable => throw ParsingException("Unable to crawl page with URL "+url+date+" Crawler error occurred!")
    }
  }

  private def authenticate(auth: AuthObject): CookieManager = {
    println("authenticating")
    val webClient: WebClient = buildWebClient

    val page: HtmlPage = webClient.getPage("https://www.appannie.com/account/login/")
    val form: HtmlForm = page.getFormByName("")

    // authentication
    try {
      form.getInputByName("username").asInstanceOf[HtmlInput].setValueAttribute(auth.username)
      form.getInputByName("password").asInstanceOf[HtmlInput].setValueAttribute(auth.password)
    } catch {
      case t: Throwable => {
        webClient.closeAllWindows()
        throw ParsingException("Can't find input fields on login page. It looks like page layout has been changed")
      }
    }
    //TODO: consider the case when no page found  -> should verify this situation and provide warning or error

    val p = form.getButtonByName("").click().asInstanceOf[HtmlPage].asText()
    if( p.contains("email or password is invalid") || p.contains("invalid") || p.contains("wrong")) {
      webClient.closeAllWindows()
      throw ParsingException("Check username and password. It looks like they're wrong or page layout changed")
    }

    println("authenticated")

    webClient.getCookieManager
  }

  private def buildWebClient(): WebClient = {
    val webClient: WebClient = new WebClient(BrowserVersion.FIREFOX_10)
    webClient.setAjaxController(new NicelyResynchronizingAjaxController)
    webClient.getOptions().setCssEnabled(true)
    webClient.getOptions().setJavaScriptEnabled(true)
    webClient.getOptions().setUseInsecureSSL(true)
    webClient.getOptions().setThrowExceptionOnScriptError(false)

    webClient.setCssErrorHandler(new ErrorHandler {
      def warning(exception: CSSParseException) {
        //ignore
      }

      def error(exception: CSSParseException) {
        //ignore
      }

      def fatalError(exception: CSSParseException) {
        //ignore
      }
    })

    webClient.setIncorrectnessListener(new IncorrectnessListener {
      def notify(message: String, origin: AnyRef) {
        //ignore
      }
    })

    webClient.waitForBackgroundJavaScript(10000)

    webClient
  }

  def buildWebClient(manager: CookieManager): WebClient = {
    val webClient: WebClient = buildWebClient
    webClient.setCookieManager(manager)
    webClient
  }
}
