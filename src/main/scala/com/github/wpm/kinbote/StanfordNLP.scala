package com.github.wpm.kinbote

import edu.arizona.sista.processors.corenlp.CoreNLPProcessor
import com.github.wpm.kinbote.Annotation.Token
import spray.json._
import AnnotationJSONProtocol._

class StanfordNLP {
  val stanfordNLPProcessor = new CoreNLPProcessor()

  def annotate(document: Document,
               annotations: Annotations = Annotations()): Annotations = {
    val doc = stanfordNLPProcessor.annotate(document)
    val tokens = for (sentence <- doc.sentences;
                      tokenOffsets = sentence.startOffsets.zip(sentence.endOffsets);
                      (start, end) <- tokenOffsets
    ) yield Token(start, end)
    annotations ++ tokens
  }
}

object StanfordNLP {
  def apply() = new StanfordNLP()

  def main(args: Array[String]) {
    val a = StanfordNLP()
    val document =
      "A screaming comes across the sky. It has happened before, but there is nothing to compare it to now."
    val annotations = a.annotate(document)
    println(annotations.toJson.prettyPrint)
  }
}
