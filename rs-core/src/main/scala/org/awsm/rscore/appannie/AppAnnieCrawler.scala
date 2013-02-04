package org.awsm.rscore.appannie

import java.net.URL
import com.gargoylesoftware.htmlunit.html.{HtmlInput, HtmlForm, HtmlPage}
import com.gargoylesoftware.htmlunit._
import scala.xml.XML
import org.awsm.rscommons.AuthObject
import org.awsm.rscore.exception.ParsingException


/**
 * Created by: akirillov
 * Date: 10/23/12
 */

class AppAnnieCrawler(val appName: String, val store: String, val rankType: String, auth: AuthObject) {

  //base url without date specified
  private val url = "http://www.appannie.com/app/" + (if(store.equals("appstore")){"ios"} else {store}) + "/" + appName.replaceAll(" ", "-").toLowerCase + "/ranking/#view=" + rankType + "&date="

  private var webClient = authenticate(auth)

  //todo: add exceptions throwing and test invalid parameters
  def crawl(date: String): Option[String] = {
    new Some[String](webClient.getPage(url+date).asInstanceOf[HtmlPage].asXml())
  }

  private def authenticate(auth: AuthObject): WebClient = {
    val webClient: WebClient = new WebClient(BrowserVersion.FIREFOX_3_6)
    webClient.setThrowExceptionOnScriptError(false)
    webClient.setCssEnabled(true)
    webClient.setJavaScriptEnabled(true)
    webClient.setAjaxController(new NicelyResynchronizingAjaxController)
    webClient.waitForBackgroundJavaScript(10000)
    webClient.setUseInsecureSSL(true)

    val page: HtmlPage = webClient.getPage("https://www.appannie.com/account/login/")
    val form: HtmlForm = page.getFormByName("")

    // authentication
    try {
      form.getInputByName("username").asInstanceOf[HtmlInput].setValueAttribute(auth.username)
    } catch {
      case e: ElementNotFoundException => throw ParsingException("Parsing error. Something is wrong with HTML on AppAnnie login page")
    }

    try {
      form.getInputByName("password").asInstanceOf[HtmlInput].setValueAttribute(auth.password)
    } catch {
      case e: ElementNotFoundException => throw ParsingException("Parsing error. Something is wrong with HTML on AppAnnie login page")
    }

    //TODO: consider the case when no page found  -> should verify this situation and provide warning or error

    val p = form.getButtonByName("").click().asInstanceOf[HtmlPage].asText()        //TODO: CATCH AUTH FAIL HERE by String "wrong username or password"

    if( p.contains("email or password is invalid") || p.contains("invalid") || p.contains("wrong")) throw ParsingException("Check username and password. It looks like they're wrong or page layout changed")

    //todo: improve analisys

    webClient
  }
}
