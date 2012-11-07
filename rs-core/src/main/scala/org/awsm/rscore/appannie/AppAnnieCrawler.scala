package org.awsm.rscore.appannie

import java.net.URL
import com.gargoylesoftware.htmlunit.html.{HtmlInput, HtmlForm, HtmlPage}
import com.gargoylesoftware.htmlunit._
import scala.xml.XML
import org.awsm.rscommons.AuthObject


/**
 * Created by: akirillov
 * Date: 10/23/12
 */

class AppAnnieCrawler(date: String, appName: String, store: String, rankType: String) {

  val url = "http://www.appannie.com/app/" + store + "/" + appName.replaceAll(" ", "-").toLowerCase + "/ranking/#view=" + rankType + "&date=" + date

  //todo: add exceptions throwing and test invalid parameters
  def crawl(auth: AuthObject): Option[String] = {
    val webClient: WebClient = new WebClient(BrowserVersion.FIREFOX_3_6)
    webClient.setThrowExceptionOnScriptError(false)
    webClient.setCssEnabled(false)
    webClient.setJavaScriptEnabled(true)
    webClient.setAjaxController(new NicelyResynchronizingAjaxController)
    webClient.waitForBackgroundJavaScript(5000)
    webClient.setUseInsecureSSL(true)

    val page: HtmlPage = webClient.getPage("https://www.appannie.com/account/login/")



    val form: HtmlForm = page.getFormByName("")

    // authentication
    try {
      form.getInputByName("username").asInstanceOf[HtmlInput].setValueAttribute(auth.username)
    } catch {
      case e: ElementNotFoundException => println("Parsing error. Something is wrong with HTML on AppAnnie page")
    }

    try {
      form.getInputByName("password").asInstanceOf[HtmlInput].setValueAttribute(auth.password)
    } catch {
      case e: ElementNotFoundException => println("Parsing error. Something is wrong with HTML on AppAnnie page")
    }

    //TODO: consider the case when no page found  -> should verify this situation and provide warning or error

    val p: HtmlPage = form.getButtonByName("").click().asInstanceOf[HtmlPage]        //TODO: CATCH AUTH FAIL HERE by String "wrong username or password"
    println(p.asXml())
    //page crawling
    new Some[String](webClient.getPage(url).asInstanceOf[HtmlPage].asXml())
  }
}
