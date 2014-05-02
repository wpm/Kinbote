package com.github.wpm

import scalax.collection.Graph
import scalax.collection.GraphEdge.DiEdge
import scala.collection.GenTraversableOnce
import scala.util.matching.Regex

/**
 * Standoff document annotation
 */
package object kinbote {

  /**
   * Information about a document
   */
  abstract class Annotation

  /**
   * Standoff annotation associates a document with annotations that describe the document
   * @param document document to annotate
   * @param annotationGraph graph of annotations of the document
   */
  case class AnnotatedDocument(document: String, annotationGraph: Graph[Annotation, DiEdge] = Graph()) {
    def ++[A <: Annotation](as: GenTraversableOnce[A]) = AnnotatedDocument(document, (annotationGraph /: as)(_ + _))

    //    def ++(edges: GenTraversableOnce[DiEdge[Annotation]]) = this

    //    def ++[A <: Annotation](edges: Traversable[DiEdge[A]]) = AnnotatedDocument(document, (annotationGraph /: edges)(_ + _))

    val annotations = annotationGraph.nodes.toOuter
  }

  object AnnotatedDocument {
    implicit def AnnotatedDocumentToDocument(a: AnnotatedDocument) = a.document
  }


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

  /**
   * Uses a regular expression to tokenize a document
   * @param delimiter token regular expression, default is words and one-character punctuation
   * @param document document to tokenize
   * @return document with token annotations
   */
  def regularExpressionTokenizer(delimiter: Regex = "\\w+|[\"'().,?!]".r)(implicit document: AnnotatedDocument) =
    document ++ delimiter.findAllMatchIn(document).map(m => Token(m.start, m.end)).toTraversable

  implicit def DocumentToAnnotatedDocument(document: String) = AnnotatedDocument(document)

  type Annotator = AnnotatedDocument => AnnotatedDocument
}
