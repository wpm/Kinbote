package com.github.wpm.kinbote

import scalax.collection.Graph
import scala.util.matching.Regex

/**
 * Standoff annotation associates a document with annotations that describe the document
 * @param document document to annotate
 * @param annotations graph of annotations of the document
 */
case class AnnotatedDocument(document: String,
                             annotations: Graph[Annotation, scalax.collection.GraphEdge.DiEdge] = Graph()) {
  def ++(as: Traversable[Annotation]) = AnnotatedDocument(document, (annotations /: as)(_ + _))
}

object AnnotatedDocument {
  implicit def AnnotatedDocumentToDocument(a: AnnotatedDocument) = a.document
}

/**
 * Information about a document
 */
abstract class Annotation

/**
 * Meaningful contiguous region of a document.
 *
 * The region is specified by fencepost offsets from the start of the document. Tokens sort by position in the
 * document.
 * @param start start of region
 * @param end end of region
 * @param document document containing this token
 */
case class Token(start: Int, end: Int)(implicit document: AnnotatedDocument) extends Annotation with Ordered[Token] {
  assert(start <= end)

  val content = document.substring(start, end)

  override def compare(that: Token) = start.compare(that.start) match {
    case 0 => end.compare(that.end)
    case x => x
  }

  override def toString = s"($start:$end):$content"
}


object Kinbote {
  implicit def DocumentToAnnotatedDocument(document: String) = AnnotatedDocument(document)

  type Annotator = AnnotatedDocument => AnnotatedDocument

  /**
   * Uses a regular expression to tokenize a document
   * @param delimiter token regular expression, default is words and one-character punctuation
   * @param document document to tokenize
   * @return document with token annotations
   */
  def regularExpressionTokenizer(delimiter: Regex = "\\w+|[\"'().,?!]".r)(implicit document: AnnotatedDocument) =
    document ++ delimiter.findAllMatchIn(document).map(m => Token(m.start, m.end)).toTraversable

  def main(args: Array[String]) {
    println(regularExpressionTokenizer()("He loves himself."))
  }
}
