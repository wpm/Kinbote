package com.github.wpm.kinbote

import spray.json.JsObject

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
  }

  trait PartOfSpeech extends Annotation

  case class Verb(person: Option[Person], number: Option[Number]) extends PartOfSpeech

  case class Pronoun(person: Option[Person], number: Option[Number], gender: Option[Gender]) extends PartOfSpeech

  case class ReflexivePronoun(person: Option[Person], number: Option[Number], gender: Option[Gender])
    extends PartOfSpeech

}
