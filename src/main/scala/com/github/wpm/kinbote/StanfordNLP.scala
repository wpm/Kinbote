package com.github.wpm.kinbote

import edu.arizona.sista.processors.corenlp.CoreNLPProcessor
import com.github.wpm.kinbote.Annotation._
import spray.json._
import AnnotationJSONProtocol._
import edu.arizona.sista.processors.Sentence
import scalax.collection.GraphEdge._


class StanfordNLP {
  val stanfordNLPProcessor = new CoreNLPProcessor()

  def annotate(document: Document,
               annotations: Annotations = Annotations()): Annotations = {
    val doc = stanfordNLPProcessor.annotate(document)
    (annotations /: doc.sentences) {
      (as, sentence) =>
        val tokens = sentenceTokens(sentence)
        sentence.tags match {
          case Some(tags) => as ++ (for ((t, p) <- tokens.zip(tags)) yield DiHyperEdge(t, PartOfSpeech(p)))
          case None => as.addAnnotations(tokens)
        }
    }
  }

  def sentenceTokens(sentence: Sentence): Seq[Token] =
    sentence.startOffsets.zip(sentence.endOffsets).map(o => Token(o._1, o._2))
}

object StanfordNLP {
  def apply() = new StanfordNLP()

  def main(args: Array[String]) {
    val a = StanfordNLP()
    implicit val document =
      "He ran"
    val annotations = a.annotate(document)
    println(annotations.toJson.prettyPrint)
    println(annotations.get[Token]().toSeq.sorted.map(_.content).mkString("\n"))
    println(annotations)
  }
}
