package com.github.wpm.kinbote

import org.scalatest.FunSuite
import com.github.wpm.kinbote.Annotation._

import spray.json._
import AnnotationJSONProtocol._
import scalax.collection.GraphEdge.DiHyperEdge

class AnnotationsTest extends FunSuite {
  val heLovesHimselfTokens = Annotations() addAnnotations
    (Token("He", 0, 2) :: Token("loves", 3, 8) :: Token("himself", 9, 16) :: Token(".", 16, 17) :: Nil)

  test("Round trip JSON serialization") {
    val roundTrip = heLovesHimselfTokens.toJson.convertTo[Annotations]
    assert(heLovesHimselfTokens === roundTrip)
  }

  test("Add Edge") {
    val heRan = Annotations() +
      DiHyperEdge(Token("ran", 3, 6), PartOfSpeech("Verb")) +
      DiHyperEdge(Sentence(0), Token("He", 0, 2), Token("ran", 3, 6))
    val roundTrip = heRan.toJson.convertTo[Annotations]
    assert(heRan === roundTrip)
  }
}
