package com.github.wpm.kinbote

import org.scalatest.FunSuite
import com.github.wpm.kinbote.Annotation.Token

class AnnotationsTest extends FunSuite {
  test("Add token annotations") {
    // He loves himself.
    val actual = Annotations() ++ (Token(0, 2) :: Token(3, 8) :: Token(9, 16) :: Token(16, 17) :: Nil)
    println(actual)
  }
}
