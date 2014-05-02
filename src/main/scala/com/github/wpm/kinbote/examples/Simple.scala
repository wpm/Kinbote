package com.github.wpm.kinbote.examples

import com.github.wpm.kinbote.{Token, AnnotatedDocument, Annotation, regularExpressionTokenizer}


import scalax.collection.GraphPredef._

object Simple {

  case class Pronoun(gender: Symbol) extends Annotation

  def pronounDetector(implicit document: AnnotatedDocument): AnnotatedDocument = {
    val tokens = document.annotations.filter(_.isInstanceOf[Token]).map(_.asInstanceOf[Token])
    val pronouns = for (pronoun <- tokens.map {
      token => token.content.toLowerCase match {
        case "he" => Pronoun('masc) ~> token
        case "she" => Pronoun('fem) ~> token
        case "himself" => Pronoun('masc) ~> token
        case "herself" => Pronoun('fem) ~> token
        case _ => None
      }
    }) yield pronoun
    document ++ pronouns
  }

  def main(args: Array[String]) {
    println(regularExpressionTokenizer()("He loves himself."))
  }
}
