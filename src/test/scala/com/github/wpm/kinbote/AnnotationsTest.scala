package com.github.wpm.kinbote

import org.scalatest.FunSuite
import com.github.wpm.kinbote.Annotation._

import spray.json._
import AnnotationJSONProtocol._
import scalax.collection.GraphEdge.DiHyperEdge

class AnnotationsTest extends FunSuite {
  // The tokens in the sentence "He loves himself."
  val heLovesHimselfTokens = Annotations() addAnnotations
    (Token(0, 2) :: Token(3, 8) :: Token(9, 16) :: Token(16, 17) :: Nil)

  val smallSentence = Annotations() +
    DiHyperEdge(Token(3, 4), PartOfSpeech("Noun")) +
    DiHyperEdge(Sentence(0), Token(0, 2), Token(3, 4))

  test("Round trip JSON serialization") {
    val roundTrip = heLovesHimselfTokens.toJson.convertTo[Annotations]
    assert(heLovesHimselfTokens === roundTrip)
  }

  test("Add Edge") {
    val roundTrip = smallSentence.toJson.convertTo[Annotations]
    assert(smallSentence === roundTrip)
  }

  test("Render as DOT") {
    // The order of DOT expressions inside the {} is undefined, so match sets.
    val content = """  n3  [label = "Token(3,4)"]
                    |  n1 -> {n2,n3}
                    |  n2  [label = "Token(0,2)"]
                    |  n1  [label = "Sentence(0)"]
                    |  n3 -> n0
                    |  n0  [label = "PartOfSpeech(Noun)"]""".stripMargin
    val dot = smallSentence.toDot()

    val actual = dot.split("\n")
    assert(actual.head === "digraph {", s"Incorrect first line\n$dot")
    val actualLines = Set() ++ actual.slice(1, actual.size - 1)
    val expectedLines = Set() ++ content.split("\n")
    assert(actualLines === expectedLines, s"Dot\n$dot\nDoes not match\n$content")
    assert(actual.last === "}", s"Incorrect last line\n$dot")
  }
}
