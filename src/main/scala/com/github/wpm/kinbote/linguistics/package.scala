package com.github.wpm.kinbote

import spray.json.{JsNumber, JsString, JsObject}

/**
 * Linguistic concepts that may be used in the annotation of natural language.
 */
package object linguistics {

  object Person extends Enumeration {
    type Person = Value
    val first, second, third = Value
  }

  import Person._

  object Gender extends Enumeration {
    type Gender = Value
    val masculine, feminine = Value
  }

  import Gender._

  object Number extends Enumeration {
    type Number = Value
    val singular, plural = Value
  }

  import Number._

  case class Token(start: Offset, end: Offset) extends Annotation with Ordered[Token] {

    def content(implicit document: Document) = document.substring(start, end)

    override def compare(that: Token) = start.compare(that.start) match {
      case 0 => end.compare(that.end)
      case x => x
    }

    override val json: JsObject = JsObject(
      "name" -> JsString("Token"),
      "start" -> JsNumber(start),
      "end" -> JsNumber(end)
    )
  }

  trait PartOfSpeech extends Annotation

  case class Verb(person: Option[Person], number: Option[Number]) extends PartOfSpeech {
    override val json: JsObject = {
      val fvs = Seq("person" -> person, "number" -> number)
      val jfvs = Seq("name" -> JsString(getClass.getSimpleName)) ++
        (for ((f, v) <- fvs if v != None; j = JsString(v.get.toString)) yield f -> j)
      JsObject(jfvs: _*)
    }
  }

  case class Pronoun(person: Option[Person], number: Option[Number], gender: Option[Gender]) extends PartOfSpeech {
    override val json: JsObject = {
      val fvs = Seq("person" -> person, "number" -> number, "gender" -> gender)
      val jfvs = Seq("name" -> JsString(getClass.getSimpleName)) ++
        (for ((f, v) <- fvs if v != None; j = JsString(v.get.toString)) yield f -> j)
      JsObject(jfvs: _*)
    }
  }

  case class ReflexivePronoun(person: Option[Person], number: Option[Number], gender: Option[Gender])
    extends PartOfSpeech {
    override val json: JsObject = {
      val fvs = Seq("person" -> person, "number" -> number, "gender" -> gender)
      val jfvs = Seq("name" -> JsString(getClass.getSimpleName)) ++
        (for ((f, v) <- fvs if v != None; j = JsString(v.get.toString)) yield f -> j)
      JsObject(jfvs: _*)
    }
  }

}
