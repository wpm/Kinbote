package com.github.wpm.kinbote

import spray.json._
import DefaultJsonProtocol._
import com.github.wpm.kinbote.linguistics._
import com.github.wpm.kinbote.linguistics.Gender
import com.github.wpm.kinbote.linguistics.ReflexivePronoun
import com.github.wpm.kinbote.linguistics.Pronoun
import com.github.wpm.kinbote.linguistics.Token
import scala.Some
import com.github.wpm.kinbote.linguistics.Verb

trait Annotation {
  def json: JsObject = {
    val fvs: Seq[(String, Any)] = Seq(("name", getClass.getSimpleName)) ++
      getClass.getDeclaredFields.map(field => {
        field setAccessible true
        (field.getName, field.get(this))
      })
    val jfvs = for ((f, v) <- fvs.toSeq if v != None;
                    jv = v match {
                      case Some(x) => JsString(x.toString)
                      case x: Int => JsNumber(x)
                      case x => JsString(x.toString)
                    }) yield f -> jv
    JsObject(jfvs: _*)
  }
}

object Annotation {
  def fromJson(value: JsValue): Annotation = {
    val fields = value.asJsObject.fields
    fields("name").convertTo[String] match {
      case "Token" => Token(fields("start").convertTo[Int], fields("end").convertTo[Int])
      case "Verb" => Verb(Person.unapply(fields), Number.unapply(fields))
      case "Pronoun" => Pronoun(Person.unapply(fields), Number.unapply(fields), Gender.unapply(fields))
      case "ReflexivePronoun" => ReflexivePronoun(Person.unapply(fields), Number.unapply(fields), Gender.unapply(fields))
      case _ => throw new Exception(s"Unrecognized annotation $value")
    }
  }
}
