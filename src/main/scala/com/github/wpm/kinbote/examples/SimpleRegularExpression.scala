package com.github.wpm.kinbote.examples

import com.github.wpm.kinbote.DocumentAnalysis.LabeledHyperEdge
import com.github.wpm.kinbote._

/**
 * Simple regular-expression based annotators.
 */
object SimpleRegularExpression {

  /**
   * Tokenize a document on whitespace.
   */
  class WhitespaceTokenizer extends Annotator {
    override def apply(document: Document, analysis: DocumentAnalysis = DocumentAnalysis()) = {
      val tokens = """\w+|\S+""".r.findAllIn(document).matchData.map(m => Token(m.toString(), m.start, m.end))
      analysis addAnnotations tokens
    }
  }

  object WhitespaceTokenizer {
    def apply() = new WhitespaceTokenizer
  }

  /**
   * Split a tokenized document into sentences using '.', '?', and '!' as sentences delimiters.
   */
  class SentenceSplitter extends Annotator {
    override def apply(document: Document, analysis: DocumentAnalysis) = {
      val tokens = analysis.get[Token]().toSeq.sorted
      val (lastA, ts, lastN) = ((analysis, Set.empty[Annotation], 0) /: tokens) { case ((a, s, n), t) =>
        if (t.s.matches( """[.?!]""")) (a addEdge LabeledHyperEdge(Sentence(n), s + t), Set.empty[Annotation], n + 1)
        else (a, s + t, n)
      }
      if (ts.nonEmpty) lastA addEdge LabeledHyperEdge(Sentence(lastN), ts)
      else lastA
    }
  }

  object SentenceSplitter {
    def apply() = new SentenceSplitter
  }

  def main(args: Array[String]) {
    val document = "Pat saw Kim. She nodded."
    val annotator = AnnotatorChain(WhitespaceTokenizer(), SentenceSplitter())
    val analysis = annotator(document)
    println(analysis.toDot)
    println(analysis.toJson.prettyPrint)
  }
}
