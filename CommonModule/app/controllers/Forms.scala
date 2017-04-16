package controllers

import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.i18n.Messages

object AuthForms {

  // Sign up
  case class SignUpData(email:String, password:String, firstName:String, lastName:String)
  def signUpForm(implicit messages:Messages) = Form(mapping(
    "email" -> email,
    "password" -> tuple(
      "password1" -> nonEmptyText.verifying(minLength(6)),
      "password2" -> nonEmptyText
    ).verifying(Messages("error.passwordsDontMatch"), password => password._1 == password._2),
    "firstName" -> nonEmptyText,
    "lastName" -> nonEmptyText
  )
  ((email, password, firstName, lastName) => SignUpData(email, password._1, firstName, lastName))
  (signUpData => Some((signUpData.email, ("",""), signUpData.firstName, signUpData.lastName)))
  )

  // Sign in
  case class SignInData(email:String, password:String, rememberMe:Boolean)
  val signInForm = Form(mapping(
    "email" -> email,
    "password" -> nonEmptyText,
    "rememberMe" -> boolean
  )(SignInData.apply)(SignInData.unapply)
  )

  // Start password recovery
  val emailForm = Form(single("email" -> email))

  // Passord recovery
  def resetPasswordForm(implicit messages:Messages) = Form(tuple(
    "password1" -> nonEmptyText.verifying(minLength(6)),
    "password2" -> nonEmptyText
  ).verifying(Messages("error.passwordsDontMatch"), password => password._1 == password._2))
}