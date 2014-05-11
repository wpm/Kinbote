package com.github.wpm.kinbote

import org.scalatest.FunSuite
import com.github.wpm.kinbote.Annotation._

import spray.json._
import AnnotationJSONProtocol._

class AnnotationsTest extends FunSuite {
  // The tokens in the sentence "He loves himself."
  val heLovesHimselfTokens = Annotations() addAnnotations (Token(0, 2) :: Token(3, 8) :: Token(9, 16) :: Token(16, 17) :: Nil)

  test("Add token annotations") {
    println(heLovesHimselfTokens)
    println(heLovesHimselfTokens.toJson.prettyPrint)
  }

  test("Round trip JSON serialization") {
    val roundTrip = heLovesHimselfTokens.toJson.convertTo[Annotations]
    assert(heLovesHimselfTokens === roundTrip)
  }
}
