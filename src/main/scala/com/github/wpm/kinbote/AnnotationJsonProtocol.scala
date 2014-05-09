package com.github.wpm.kinbote

import spray.json._
import com.github.wpm.kinbote.linguistics._
import scala.Some

object AnnotationJsonProtocol extends DefaultJsonProtocol {

  implicit object AnnotationJsonFormat extends RootJsonFormat[Annotation] {
    def write(a: Annotation) = {
      val fvs: Seq[(String, Any)] = Seq(("name", a.getClass.getSimpleName)) ++
        a.getClass.getDeclaredFields.map(field => {
          field setAccessible true
          (field.getName, field.get(a))
        })
      val jfvs = for ((f, v) <- fvs.toSeq if v != None;
                      jv = v match {
                        case Some(x) => JsString(x.toString)
                        case x: Int => JsNumber(x)
                        case x => JsString(x.toString)
                      }) yield f -> jv
      JsObject(jfvs: _*)
    }

    def read(value: JsValue) = {
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

}
