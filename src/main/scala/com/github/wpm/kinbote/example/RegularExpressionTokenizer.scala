package com.github.wpm.kinbote.example

import com.github.wpm.kinbote.{Document, StandoffAnnotation, Annotator}
import scala.util.matching.Regex
import com.github.wpm.kinbote.linguistics.Token

/**
 * Tokenize a document using a regular expression.
 *
 * The default regular expression splits on whitespace and treats single-character punctuation marks as tokens.
 * @param delimiter regular expression that specifies the token delimiter
 */
class RegularExpressionTokenizer(delimiter: Regex = "\\w+|[\"'().,?!]".r) extends Annotator {
  override def annotate(document: Document, annotations: StandoffAnnotation = StandoffAnnotation()) =
    annotations.addNodes(delimiter.findAllMatchIn(document).map(m => Token(m.start, m.end)))
}

object RegularExpressionTokenizer {
  def apply(delimiter: Regex = "\\w+|[\"'().,?!]".r) = new RegularExpressionTokenizer(delimiter)
}