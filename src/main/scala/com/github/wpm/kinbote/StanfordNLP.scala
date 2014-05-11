package com.github.wpm.kinbote

import edu.arizona.sista.processors.corenlp.CoreNLPProcessor
import com.github.wpm.kinbote.Annotation._
import spray.json._
import AnnotationJSONProtocol._
import edu.arizona.sista.processors.Sentence

class StanfordNLP {
  val stanfordNLPProcessor = new CoreNLPProcessor()

  def annotate(document: Document,
               annotations: Annotations = Annotations()): Annotations = {
    val doc = stanfordNLPProcessor.annotate(document)
    (annotations /: doc.sentences) {
      (as, s) => as ++ sentenceTokens(s) ++ sentencePOS(s)
    }
  }

  def sentenceTokens(sentence: Sentence): Seq[Token] =
    sentence.startOffsets.zip(sentence.endOffsets).map(o => Token(o._1, o._2))

  def sentencePOS(sentence: Sentence): Seq[PartOfSpeech] = sentence.tags.toSeq.flatten.map(PartOfSpeech)
}

object StanfordNLP {
  def apply() = new StanfordNLP()

  def main(args: Array[String]) {
    val a = StanfordNLP()
    implicit val document =
      "A screaming comes across the sky. It has happened before, but there is nothing to compare it to now."
    val annotations = a.annotate(document)
    println(annotations.toJson.prettyPrint)
    println(annotations.get[Token]().toSeq.sorted.map(_.content).mkString("\n"))
  }
}
