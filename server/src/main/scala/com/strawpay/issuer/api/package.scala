package com.strawpay.issuer
import upickle.default.{ReadWriter, macroRW}

/**
  *
  */
package object api {
  case object RegisterForm{
    val template = RegisterForm("required", "required")
    implicit def rw: ReadWriter[RegisterForm] = macroRW
  }
  case class RegisterForm(phoneNumber: String, email: String) {
    // Todo replace with cats validation https://www.gregbeech.com/2018/08/12/akka-http-entity-validation/
    def validate: Either[String, RegisterForm] = {
      if (phoneNumber.isEmpty) return Left("invalid phone number")
      Right(this)
    }
  }

  case object Note {
    implicit def rw: ReadWriter[Note] = macroRW
  }
  case class Note(value: String)

  case object Account {
    implicit def rw: ReadWriter[Account] = macroRW
  }
  case class Account(accountId: String)

}
