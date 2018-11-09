package com.strawpay.issuer
import upickle.default.{macroRW, ReadWriter ⇒ RW}

/**
  *
  */
package object api {
  case object RegisterForm{
    val template = RegisterForm("required", "required")
    implicit def rw: RW[RegisterForm] = macroRW
  }
  case class RegisterForm(phoneNumber: String, email: String) {
    // Todo replace with cats validation https://www.gregbeech.com/2018/08/12/akka-http-entity-validation/
    def validate: Either[String, RegisterForm] = {
      if (phoneNumber.isBlank || phoneNumber.isEmpty) return Left("invalid phone number")
      Right(this)
    }
  }

  case object Note {
    implicit def rw: RW[Note] = macroRW
  }
  case class Note(value: String)

  case object Account {
    implicit def rw: RW[Account] = macroRW
  }
  case class Account(accountId: String)

}
