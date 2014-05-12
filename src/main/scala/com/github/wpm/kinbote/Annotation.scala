package com.github.wpm.kinbote

import AnnotationJSONProtocol._

trait Annotation extends TypedJSONSerializable

object Annotation {

  case class Sentence(n: Int) extends Annotation with Ordered[Sentence] {
    override def compare(that: Sentence) = n.compare(that.n)
  }

  case class Token(start: Offset, end: Offset) extends Annotation with Ordered[Token] {

    def content(implicit document: Document) = document.substring(start, end)

    override def compare(that: Token) = start.compare(that.start) match {
      case 0 => end.compare(that.end)
      case x => x
    }
  }

  case class PartOfSpeech(pos: String) extends Annotation

  case class Lemma(form: String) extends Annotation with Ordered[Lemma] {
    override def compare(that: Lemma) = form.compare(that.form)
  }

}