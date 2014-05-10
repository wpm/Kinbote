package com.github.wpm.kinbote


trait Annotation

object Annotation {

  case class Token(start: Offset, end: Offset) extends Annotation with Ordered[Token] {

    def content(implicit document: Document) = document.substring(start, end)

    override def compare(that: Token) = start.compare(that.start) match {
      case 0 => end.compare(that.end)
      case x => x
    }
  }

  case class PartOfSpeech(pos: String) extends Annotation

}