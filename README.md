# Kinbote

*I was the shadow of the waxwing slain*  
*By the false azure of the windowpane*  
–Vladimir Nabokov, *Pale Fire*

## Overview

Kinbote is a framework for the annotation of text documents.
It provides a way to generate and serialize annotations that provide information about portions of a document.
The exact nature of the information is undefined, but a typical use case would be linguistic annotatation, for example
parts of speech, coreference chains, etc.

Kinbote is comparable to other linguistic annotation frameworks like Apache 
[Unstructured Information Management (UIMA)](https://uima.apache.org/) or the
[General Architecture for Text Engineering (GATE)](https://gate.ac.uk/).
It uses [Scala's](http://www.scala-lang.org/) cleaner syntax to cut down on the amount of boilerplate code required by
these Java-based frameworks.
It also allows provides a general-purpose cross-programming-language serialization scheme.


## Getting Started

This package contains two sample applications that demonstrate how to annotate text documents.

* `com.github.wpm.kinbote.examples.SimpleRegularExpression` uses regular expressions to tokenize a documents into tokens
and sentences.
* `com.github.wpm.kinbote.examples.StanfordNLP` uses the [Stanford NLP toolkit](http://www-nlp.stanford.edu/) to
tokenize text, do part of speech tagging, named entity detection and dependency parses.  

Both these programs run without command line arguments and analyze short passages of text, producing a JSON object
representing the complete analysis and a DOT object that may be displayed as a graph using the
[Graphviz](http://www.graphviz.org/) visualization tool. 

The unit tests also demonstrate basic functionality.


## Architecture

The core of Kinbote is the `Annotator` object. This takes a textual `Document` as input along with any previous
analysis done on that document in the form of a `DocumentAnalysis` and returns an augmented `DocumentAnalysis`.
Following to paradigm of systems like UIMA and GATE, `Annotator` objects may be chained together so that one builds on
the output of another. Unlike GATE and UIMA, Kinbote annotator chaining is accomplished with simple function composition
without the need for elaborate XML configuration.

The `DocumentAnalysis` object represented annotation as a labeled directed hypergraph.
Nodes in the graph represent information about particular spans in the text (e.g. token, part of speech, named entity
type), while hyperedges represent relationships between these nodes. This graph may be serialized as either
[JSON](http://json.org/) or [Avro](http://avro.apache.org/) which allows it to be used in a cross-platform,
cross-programming-language manner without requiring access to the code that generated it.
 

