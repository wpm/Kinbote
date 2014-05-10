package com.github.wpm.kinbote

import org.scalatest.FunSuite
import com.github.wpm.kinbote.Annotation.Token
import AnnotationJSON._
import spray.json._

class TokenTest extends FunSuite {
  test("Serialize as JSON") {
    val actual = Token(3, 5).compactPrint
    assert(actual === """{"type":"Token","start":3,"end":5}""")
  }

  test("Deserialize from JSON") {
    val actual: Annotation = JsonParser( """{"type":"Token","start":3,"end":5}""")
    assert(actual === Token(3, 5))
  }

  test("Get covered text") {
    implicit val document: Document = "The world is what it is."
    assert(Token(4, 9).content === "world")
  }
}
