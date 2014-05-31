package com.github.wpm.kinbote

import edu.arizona.sista.processors.corenlp.CoreNLPProcessor
import com.github.wpm.kinbote.DocumentAnalysis.LabeledHyperEdge


class StanfordNLP extends Annotator {
  type StanfordSentence = edu.arizona.sista.processors.Sentence

  protected val stanfordNLPProcessor = new CoreNLPProcessor()

  def apply(document: Document, analysis: DocumentAnalysis = DocumentAnalysis()): DocumentAnalysis = {
    val doc = stanfordNLPProcessor.annotate(document)
    (analysis /: doc.sentences.zipWithIndex) {
      case (as, (sentence, n)) =>
        val tokens = sentenceTokens(document, sentence)
        val tags = sentenceAnnotations(sentence.tags, PartOfSpeech)
        val lemmas = sentenceAnnotations(sentence.lemmas, Lemma)

        val tokenInfo = (for (p <- List(tags, lemmas); t <- p) yield t).transpose
        val tokenEdges = tokenInfo.zip(tokens).map { case (ti, t) => LabeledHyperEdge(t, ti.toSet)}
        val sEdge = LabeledHyperEdge(Sentence(n), tokens.toSet)
        as.addEdge(sEdge).addEdges(tokenEdges)
    }
  }

  protected def sentenceTokens(document: Document, sentence: StanfordSentence): Seq[Token] =
    sentence.startOffsets.zip(sentence.endOffsets).map {
      case (start, end) => Token(document.substring(start, end), start, end)
    }

  protected def sentenceAnnotations[A <: Annotation](as: Option[Array[String]], f: (String) => A): Option[Seq[A]] =
    for (a <- as) yield a.map(f).toSeq
}

object StanfordNLP {
  def apply() = new StanfordNLP()

  def main(args: Array[String]) {
    val a = StanfordNLP()
    val document = "I was the shadow of the waxwing slain. By the false azure in the windowpane."
    val analysis = a(document)
    println(analysis.toDot)
  }
}
