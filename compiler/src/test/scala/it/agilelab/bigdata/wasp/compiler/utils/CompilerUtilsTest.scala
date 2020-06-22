package it.agilelab.bigdata.wasp.compiler.utils

import org.scalatest.{FlatSpec, Matchers}

class CompilerUtilsTest extends FlatSpec with Matchers {

  it should "test wrong code" in {
    val output = CompilerUtils.validate(
      """val a = "banana"
        | a.test """.stripMargin)
    output.size shouldBe 1
    output.head.toString should startWith ("<virtual>:2")
  }


  it should "test validate code" in {
   val output =  CompilerUtils.validate(
      """val a = "banana"
        |a.toString()
        |val c = "bar" """.stripMargin)
     output.size shouldBe 0
  }

  it should "test validate code with warning" in {
    val output =  CompilerUtils.validate(
      """val a = "banana"
        |a""".stripMargin)
    output.count(_.errorType.equals("error")) shouldBe 0
    output.count(_.errorType.equals("warning")) shouldBe 1
  }


  it should "test complete code 1" in {
    val code = """val a = "banana"
                 |a.""".stripMargin
    val output =  CompilerUtils.complete(code,code.length)
    val a = "banana"
    output.exists(m=> m.toComplete.equals("toInt")) shouldBe true
    output.exists(m=> m.toComplete.equals("zip")) shouldBe true

  }



  it should "test complete code 2" in {
    val code =
      """val test = "banana"
        |val testi = "ciao"
        |val home = "home"
        |test.to""".stripMargin

    val output =  CompilerUtils.complete(code,code.length)
    output.exists(m=> m.toComplete.equals("toInt")) shouldBe true
    output.exists(m=> m.toComplete.equals("toString")) shouldBe true
    output.exists(m=> m.toComplete.equals("zip")) shouldBe false

  }


  it should "test complete code 3" in {
    val code =
      """val test = "banana"
        |val test1 = "ciao"
        |val home = "home"
        |te""".stripMargin
    val output =  CompilerUtils.complete(code,code.length)
    output.exists(m=> m.toComplete.equals("test")) shouldBe true
    output.exists(m=> m.toComplete.equals("test1")) shouldBe true
    output.size shouldBe 2


  }

  it should "test complete code 4" in {
    val code =
      """val test = "banana"
        |val test1 = "ciao"
        |val home = "home"
        |to""".stripMargin
    val output =  CompilerUtils.complete(code,code.length)
    output.exists(m=> m.toComplete.equals("test")) shouldBe false
    output.exists(m=> m.toComplete.equals("test1")) shouldBe false
    output.exists(m=> m.toComplete.equals("toString")) shouldBe true
    output.size shouldBe 1



  }


  it should "test complete code 5" in {
    val code =
      """val test0 : Int = {
        |val test = "banana"
        |val test1 = "ciao"
        |val home = "home"
        |1}
        |te""".stripMargin
    val output =  CompilerUtils.complete(code,code.length)
    output.exists(m=> m.toComplete.equals("test0")) shouldBe true
    output.exists(m=> m.toComplete.equals("test")) shouldBe false
    output.exists(m=> m.toComplete.equals("test1")) shouldBe false
    output.exists(m=> m.toComplete.equals("toString")) shouldBe false
    output.size shouldBe 1

  }


  it should "test complete code 6" in {
    val code =
      """val test10 = "Hello
        |val test0 : Int = {
        |val test = "banana"
        |val test1 = "ciao"
        |te""".stripMargin
    val output =  CompilerUtils.complete(code,code.length)

    output.size shouldBe 0 //TODO there is a problem when the code is on a block.

  }


  it should "test complete code 7" in {
    val code =
      """val test = "banana"
        |val house = te
        |val home = "hello"
        |""".stripMargin
    val output =  CompilerUtils.complete(code,34)

    output.exists(m=> m.toComplete.equals("test")) shouldBe true
    output.size shouldBe 1

  }


  it should "test complete code 8" in {
    val code =
      """val test = "banana"
        |val house = test.toDoub
        |val home = "hello"
        |""".stripMargin
    val output =  CompilerUtils.complete(code,43)
    println(output.mkString("\n"))
    output.exists(m=> m.toComplete.equals("toDouble")) shouldBe true
    output.size shouldBe 1

  }


}

