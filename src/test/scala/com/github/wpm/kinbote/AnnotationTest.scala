package com.github.wpm.kinbote

import org.scalatest.FunSuite
import com.github.wpm.kinbote.linguistics._
import Person._
import Number._

class AnnotationTest extends FunSuite {
  test("Token to JSON") {
    val token = Token(3, 4)
    assert(token === Annotation.fromJson(token.json))
  }

  test("1SG Verb to JSON") {
    val verb1sg = Verb(Some(first), Some(singular))
    assert(verb1sg === Annotation.fromJson(verb1sg.json))
  }

  test("2nd Verb to JSON") {
    val verb2 = Verb(Some(second), None)
    assert(verb2 === Annotation.fromJson(verb2.json))
  }

  test("1PL Pronoun to JSON") {
    val pronoun = Pronoun(Some(first), Some(plural), None)
    assert(pronoun === Annotation.fromJson(pronoun.json))
  }
}
