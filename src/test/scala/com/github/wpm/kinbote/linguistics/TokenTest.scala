package com.github.wpm.kinbote.linguistics

import org.scalatest.FunSuite
import com.github.wpm.kinbote.Document

class TokenTest extends FunSuite {
  implicit val document = new Document("one two three")
  val token = Token(4, 7)

  test("Token content") {
    assert(token.content === "two")
  }
}
