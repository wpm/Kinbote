package com.github.wpm.kinbote

/**
 * Information about a part of a document
 */
sealed trait Annotation

case class Sentence(n: Int) extends Annotation with Ordered[Sentence] {
  override def compare(that: Sentence) = n.compare(that.n)
}

case class Token(s: String, from: Offset, to: Offset) extends Annotation with Ordered[Token] {
  require(from <= to)

  override def compare(that: Token) = from.compare(that.from) match {
    case 0 => to.compare(that.to)
    case x => x
  }
}

case class PartOfSpeech(tag: String) extends Annotation

case class Lemma(form: String) extends Annotation
