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
        val tokens = sentenceTokens(document, sentence)
        val tags = sentenceAnnotations(sentence.tags, PartOfSpeech)
        val lemmas = sentenceAnnotations(sentence.lemmas, Lemma)

        val tokenInfo = (for (p <- List(tags, lemmas); t <- p) yield t).transpose
        val tokenEdges = tokenInfo.zip(tokens).map { case (ti, t) => DiHyperEdge(Seq(t) ++ ti)}
        val sEdge = DiHyperEdge(Seq(Sentence(n)) ++ tokens)
        as + sEdge ++ tokenEdges
    }
  }

  protected def sentenceTokens(document: Document, sentence: Sentence): Seq[Token] =
    sentence.startOffsets.zip(sentence.endOffsets).map {
      case (start, end) => Token(document.substring(start, end), start, end)
    }

  protected def sentenceAnnotations[A <: Annotation](as: Option[Array[String]], f: (String) => A): Option[Seq[A]] =
    for (a <- as) yield a.map(f).toSeq
}

object StanfordNLP {
  def apply() = new StanfordNLP()

  def main(args: Array[String]) {
    import spray.json._
    import AnnotationJSONProtocol._

    val a = StanfordNLP()
    val document = "I was the shadow of the waxwing slain. By the false azure in the windowpane."
    val annotations = a.annotate(document)
    println(annotations.toJson.prettyPrint)
    println(annotations.get[Token]().toSeq.sorted.map(_.content).mkString("\n"))
    println(annotations)
    println(annotations.toDot(Some(document)))
  }
}
