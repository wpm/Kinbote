package com.github.wpm.kinbote

import org.scalatest.FunSuite
import com.github.wpm.kinbote.Annotation.Token
import spray.json._
import AnnotationJSONProtocol._

class TokenTest extends FunSuite {
  test("Deserialize from JSON") {
    val actual = """{"type":"Token", "content":"dog", "start":3,"end":5}""".parseJson.convertTo[Token]
    assert(actual === Token("dog", 3, 5))
  }

  test("Roundtrip JSON serialization") {
    val original = Token("dog", 3, 5)
    val j = original.toJson
    val roundTrip = j.convertTo[Token]
    assert(original === roundTrip)
  }
}
