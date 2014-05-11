package com.github.wpm.kinbote

import org.scalatest.FunSuite
import com.github.wpm.kinbote.Annotation.Token
import spray.json._
import AnnotationJSONProtocol._

class TokenTest extends FunSuite {
  test("Deserialize from JSON") {
    val actual = """{"type":"Token","start":3,"end":5}""".parseJson.convertTo[Token]
    assert(actual === Token(3, 5))
  }

  test("Roundtrip JSON serialization") {
    val original = Token(3, 5)
    val j = original.toJson
    val roundTrip = j.convertTo[Token]
    assert(original === roundTrip)
  }

  test("Get covered text") {
    implicit val document: Document = "The world is what it is."
    assert(Token(4, 9).content === "world")
  }
}
