codebrag-intellij-idea-plugin
=============================

Experimental plugin integrating Codebrag (code-review tool) with IntelliJ Idea
---------------------

This is the first version of plugin written for [Codebrag](http://codebrag.com). It allows to open reviewed file directly in your beloved IDE right from your browser with Codebrag. 



#### Version 0.1

* Allows to open file by its name


### How it works

Plugin launches an internal HTTP server listening on port **8880** (http://localhost:8880/codebrag-plugin). 

When it receives
the request on _http://localhost:8880/codebrag-plugin?&lt;fullNameOfFileToOpen&gt;_ it tries to find given file in your opened projects and 
then opens tab with it in your IntelliJ IDEA running instance.
