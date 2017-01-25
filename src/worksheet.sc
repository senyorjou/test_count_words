import java.io.File
import scala.io.Source
import scala.collection.mutable

def getFileList(dir: String):List[File] = {
  val d = new File(dir)
  if (d.exists && d.isDirectory) {
    d.listFiles.filter(_.isFile).toList
  } else {
    List[File]()
  }
}

var a = "one two three"

val dir = "/tmp/recipes"
val files = getFileList(dir)
val fileCount = files.length


println("Word matcher.")
println(s"Analyzing $fileCount files on directory $dir")



def tokens(filename: File) =
  Source.fromFile(filename).
  getLines().
  flatMap(_.split("\\W")).
  filter(_.length > 1).
  map(_.toLowerCase).
  toSet

var single = tokens(files(0))

var words = Map((tokens(files(0)), files(0).getName.split("/").last))

val finalMap = for (file <- files)
  yield tokens(file).map(_ -> file.getName.split("/").last).toMap


val finalMapofMaps = finalMap.flatten.groupBy(_._1).map{case(k, v) => k -> v.map(_._2).toSeq}

val wordlist = List("water", "potato", "salt")
wordlist.map(finalMapofMaps.get(_)).flatten.groupBy(identity).mapValues(_.size)

val tupArray = Array(("water", 1), ("potato", 2), ("salt", 4))

tupArray.maxBy(_._1.length)._1.length

