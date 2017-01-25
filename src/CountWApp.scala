import java.io.File
import scala.io.{Source, StdIn}

import java.nio.charset.CodingErrorAction
import scala.io.Codec



object CountWApp extends App {
  // This code is added to quick solve encoding issues I encountered.
  // Must remove
  implicit val codec = Codec("UTF-8")
  codec.onMalformedInput(CodingErrorAction.REPLACE)
  codec.onUnmappableCharacter(CodingErrorAction.REPLACE)


//  def getCurrDir = new java.io.File(".").getCanonicalPath
  def getCurrDir = "/tmp/recipes"
  val files = getFileList(getCurrDir)
  val fileCount = files.length
  val wordsMap = createWordMap(files)

  println("Word matcher.")
  println(s"Analyzing $fileCount files on directory $getCurrDir")

  while (true) {
    printf(">>> ")
    val inputStr = StdIn.readLine()
    if (inputStr == ":q" | inputStr == ":quit") System.exit(0)

    val results = evaluate(wordsMap, cleanInput(inputStr))

    if (results.length > 0) {
      formatOut(results)
    } else {
      println("No results found")
    }
  }

  /** getTokens reads lines from file and returns a Set of words
    * acording to regexp and lenght hardcoded
    *
    *  @param file file object to work on
    *  @return a Set containing all words extracted
    */
  def getTokens(file: File) =
    Source.fromFile(file).
      getLines().
      flatMap(_.split("\\W")).
      filter(_.length > 1).
      map(_.toLowerCase).
      toSet

  /** tokensMap creates a List of Map(word -> filename)
    *
    *  @param fileList a list of File objects
    *  @return a List of Maps with (word -> filename)
    */
  def tokensMap(fileList: List[File]): List[Map[String, String]] = {
    for (file <- files)
      yield getTokens(file).map(_ -> file.getName.split("/").last).toMap
  }

  /** createWordMap assembles all data from files already transformet
    *  flats list of tokens map and
    * aggregates files on name value
    *
    *  @return a Map with (word -> List(File))
    */
  def createWordMap(fileList: List[File]): Map[String, Seq[String]] = {
    tokensMap(fileList).flatten.groupBy(_._1).map{case(k, v) => k -> v.map(_._2).toSeq}
  }

  /** helper to print data on screen
    * aggregates files on name value
    *
    *  @param results an ordered List of tuples (filename, score)
    */
  def formatOut(results: Seq[(String, Int)]): Unit = {
    val maxWidth =  results.maxBy(_._1.length)._1.length

    results.foreach {
      x => {
        val fileName = x._1.padTo(maxWidth + 1, " ").mkString
        val score = x._2
        println(s"$fileName : $score")
      }
    }
  }

  /** helper to print clean data. Can be removed or inlined, since does almost nothing
    *
    *  @param inputStr String containing user input
    *  @return Array of Strings splitted by space
    */

  def cleanInput(inputStr: String): Array[String] = {
    val searchtokens = inputStr.split(" ")
    searchtokens
  }


  /** evaluate reduces words map by selecting keys passed
    * to obtain filenames, finally counts occurences of files
    *
    *  @param inputArray Array of words to search for
    *  @return a List containing tuples with (filename, score)
    */
  def evaluate(words: Map[String, Seq[String]], inputArray: Array[String]): Seq[(String, Int)] = {
    val fileList = inputArray.map(words.get(_)).flatten
    val fileMapAgg = fileList.flatten.groupBy(identity).mapValues(_.size)
    val fileMapSorted = fileMapAgg.toSeq.sortBy(_._2).reverse

    fileMapSorted
  }

  /** getFileList reads all files from passe directory
    *
    *  @param dir Directory to read from
    *  @return a List containing File elements or an empty List
    */
  def getFileList(dir: String): List[File] = {
    val d = new File(dir)
    if (d.exists && d.isDirectory) {
      d.listFiles.filter(_.isFile).toList
    } else {
      List[File]()
    }
  }
}


