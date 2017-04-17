package common.auth.logic

import javax.inject.Inject
import scala.concurrent.Future
import scala.language.postfixOps
import play.api.Configuration
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.mailer._

class Mailer @Inject() (configuration:Configuration, mailer:MailerClient) {
  val from = configuration.getString("mail.from").get
  val replyTo = configuration.getString("mail.reply")

  def sendEmailAsync(recipients:String*)(subject:String, bodyHtml:Option[String], bodyText:Option[String]) = {
    Future {
      sendEmail(recipients:_*)(subject, bodyHtml, bodyText)
    } recover {
      case e => play.api.Logger.error("error sending email", e)
    }
  }

  def sendEmail(recipients:String*)(subject:String, bodyHtml:Option[String], bodyText:Option[String]) {
    val email = Email(subject = subject, from = from, to = recipients, bodyHtml = bodyHtml, bodyText = bodyText, replyTo = replyTo)
    mailer.send(email)
    ()
  }

  def welcome(fullName:String, email:String, link:String) = {
    sendEmailAsync(email)(
      subject = "Encabezado",
      bodyHtml = Some("Hola " + fullName),
      bodyText = Some("Este es el correo de bienvenida click en este enlace " + link)
    )
  }

  def resetPassword(email:String, link:String) = {
    sendEmailAsync(email)(
      subject = "Encabezado",
      bodyHtml = Some("Hola password reset"),
      bodyText = Some("Este es el correo de reseteo de contraseña click en este enlace " + link)
    )
  }
}