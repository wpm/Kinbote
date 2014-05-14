package com.github.wpm.kinbote

import edu.arizona.sista.processors.corenlp.CoreNLPProcessor
import edu.arizona.sista.processors.Sentence
import com.github.wpm.kinbote.Annotation._
import scalax.collection.GraphEdge.DiHyperEdge


class StanfordNLP extends Annotator {
  protected val stanfordNLPProcessor = new CoreNLPProcessor()

  def annotate(document: Document, annotations: Annotations = Annotations()): Annotations = {
    val doc = stanfordNLPProcessor.annotate(document)
    (annotations /: doc.sentences.zipWithIndex) {
      case (as, (sentence, n)) =>
        val tokens = sentenceTokens(sentence)
        val tags = sentenceAnnotations(sentence.tags, PartOfSpeech)
        val lemmas = sentenceAnnotations(sentence.lemmas, Lemma)

        val tokenInfo = (for (p <- List(tags, lemmas); t <- p) yield t).transpose
        val tokenEdges = if (tokenInfo.isEmpty) Nil
        else tokens.zip(tokenInfo).map { case (t, ti) => DiHyperEdge(Seq(t) ++ ti)}
        val sEdge = DiHyperEdge(Seq(Sentence(n)) ++ tokens)
        as + sEdge ++ tokenEdges
    }
  }

  protected def sentenceTokens(sentence: Sentence): Seq[Token] =
    sentence.startOffsets.zip(sentence.endOffsets).map { case (start, end) => Token(start, end)}

  protected def sentenceAnnotations[A <: Annotation](as: Option[Array[String]], f: (String) => A): Option[Seq[A]] =
    for (a <- as) yield a.map(f).toSeq
}

object StanfordNLP {
  def apply() = new StanfordNLP()

  def main(args: Array[String]) {
    import spray.json._
    import AnnotationJSONProtocol._

    val a = StanfordNLP()
    implicit val document =
      "He ran"
    val annotations = a.annotate(document)
    println(annotations.toJson.prettyPrint)
    println(annotations.get[Token]().toSeq.sorted.map(_.content).mkString("\n"))
    println(annotations)
  }
}
