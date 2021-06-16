package com.delta.sample

object Main {

  def main(args: Array[String]): Unit = {

    print("Starting delta lake sample run")

    if (args.length == 0) {
      println("I need at least input and output path")
    }

    val inputPath = args(0)
    val deltaPath = args(1)

    println("\n" + inputPath +  ", " + deltaPath)

    DeltaTable.csvReadWrite(inputPath, deltaPath)

    DeltaTable.show(deltaPath)

  }

}
