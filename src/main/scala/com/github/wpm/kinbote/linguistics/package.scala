package com.github.wpm.kinbote

import spray.json._
import DefaultJsonProtocol._

/**
 * Linguistic concepts that may be used in the annotation of natural language.
 */
package object linguistics {

  def featureFromJson[E](n: String, m: Map[String, E], fields: Map[String, JsValue]): Option[E] =
    if (fields.contains(n)) m.get(fields(n).convertTo[String])
    else None

  object Person extends Enumeration {
    type Person = Value
    val first, second, third = Value

    def unapply(fields: Map[String, JsValue]): Option[Person] =
      featureFromJson(getClass.getSimpleName.toLowerCase.dropRight(1), values.map(v => v.toString -> v).toMap, fields)
  }

  import Person._

  object Number extends Enumeration {
    type Number = Value
    val singular, plural = Value

    def unapply(fields: Map[String, JsValue]): Option[Number] = {
      featureFromJson(getClass.getSimpleName.toLowerCase.dropRight(1), values.map(v => v.toString -> v).toMap, fields)
    }

  }

  import Number._

  object Gender extends Enumeration {
    type Gender = Value
    val masculine, feminine = Value

    def unapply(fields: Map[String, JsValue]): Option[Gender] =
      featureFromJson(getClass.getSimpleName.toLowerCase.dropRight(1), values.map(v => v.toString -> v).toMap, fields)
  }

  import Gender._

  case class Token(start: Offset, end: Offset) extends Annotation with Ordered[Token] {

    def content(implicit document: Document) = document.substring(start, end)

    override def compare(that: Token) = start.compare(that.start) match {
      case 0 => end.compare(that.end)
      case x => x
    }
  }

  trait PartOfSpeech extends Annotation

  case class Verb(person: Option[Person], number: Option[Number]) extends PartOfSpeech

  case class Pronoun(person: Option[Person], number: Option[Number], gender: Option[Gender]) extends PartOfSpeech

  case class ReflexivePronoun(person: Option[Person], number: Option[Number], gender: Option[Gender])
    extends PartOfSpeech

}
