package pc.examples

import java.util.Random

import pc.modelling.{CTMCAnalysis, DAP, DAPGrid}
import pc.utils.{MSet, Grids}

object DAPGossip extends App {
  object place extends Enumeration {
    val A,B,C = Value
  }
  type Place = place.Value
  type ID = (Int,Int)
  import place._
  import DAP._

  val gossipRules = DAP[Place](
    Rule(MSet(A,A), m=>1000,MSet(A),MSet()),   // a|a --1000--> a
    Rule(MSet(A), m=>1,MSet(A),MSet(A)),       // a --1--> a|^a
  )
  val gossipCTMC = DAP.toCTMC[ID,Place](gossipRules)
  val net = Grids.createRectangularGrid(5,5)
  // an `a` initial on top left
  val state = State[ID,Place](MSet(Token((0,0),A)),MSet(),net)

  val analysis = CTMCAnalysis(gossipCTMC)
  analysis.newSimulationTrace(state,new Random).take(50).toList.foreach(
    step => {
      println(step._1) // print time
      println(DAPGrid.simpleGridStateToString[Place](step._2,A)) // print state, i.e., A's
    })
}