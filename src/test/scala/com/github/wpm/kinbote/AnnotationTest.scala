package com.github.wpm.kinbote

import org.scalatest.FunSuite
import com.github.wpm.kinbote.linguistics._
import Person._
import Number._

import spray.json._

class AnnotationTest extends FunSuite {

  import AnnotationJsonProtocol._

  test("Token to JSON") {
    val token = Token(3, 4)
    assert(token === token.asInstanceOf[Annotation].toJson.convertTo[Annotation])
  }

  test("1SG Verb to JSON") {
    val verb1sg = Verb(Some(first), Some(singular))
    assert(verb1sg === verb1sg.asInstanceOf[Annotation].toJson.convertTo[Annotation])
  }

  test("2nd Verb to JSON") {
    val verb2 = Verb(Some(second), None)
    assert(verb2 === verb2.asInstanceOf[Annotation].toJson.convertTo[Annotation])
  }

  test("1PL Pronoun to JSON") {
    val pronoun = Pronoun(Some(first), Some(plural), None)
    assert(pronoun === pronoun.asInstanceOf[Annotation].toJson.convertTo[Annotation])
  }
}
