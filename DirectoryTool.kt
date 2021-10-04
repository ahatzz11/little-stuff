import java.io.*;
import java.util.*
import java.text.*
import java.math.*
import java.util.regex.*

// file location : <filename, file data>
val folderMap : MutableMap<String, MutableMap<String, String>> = mutableMapOf()

fun main(args: Array<String>) {
      /* Enter your code here. Read input from STDIN. Print output to STDOUT */

      val path : String = "/code"
      val filename : String = "/code/file.txt"

      // make directory and write
      mkdir(path)
      writeFile(filename, "really-fancy-code")
      writeFile("/code/second-file.txt", "fancier-code")

      // assert we can read it
      assert("really-fancy-code" == readFile(filename))


      val path2 : String = "/foo/bar"
      val filename2: String = "/foo/bar/file.txt"

      // make directory and write
      mkdir(path2)
      writeFile(filename2, "foo-bar-code")

      println(folderMap)

      assert("foo-bar-code" == readFile(filename2))
}

// create directory with empty list of files
fun mkdir(path: String) {

    // add if doesn't exist
    if(folderMap[path] == null) {
        folderMap.put(path, mutableMapOf())
    } else {
        // throw Exception, handled later
        println("folder $path already exists, error")
    }
}

// add file to a given directory\
// val folder : String = path.split("/").toString()
fun writeFile(path : String, data : String) {

    val fileAndPathPair : Pair<String, String> = separatePathAndName(path)

    // get file path and name
    val filePath : String = fileAndPathPair.first
    val fileName : String = fileAndPathPair.second

    // add to folder if it exists
    if (folderMap[filePath] == null) {
        println("directory does not exist, error")
    } else {
        folderMap[filePath]!!.put(fileName, data)
    }
}

fun readFile(path : String) : String? {
    val fileAndPathPair : Pair<String, String> = separatePathAndName(path)

    // get file path and name
    val filePath : String = fileAndPathPair.first
    val fileName : String = fileAndPathPair.second

    if(folderMap[filePath] == null) {
        println("directory does not exist, error")
        return null
    } else {

        if(folderMap[filePath]!![fileName] == null) {
            println("file not found, error")
            return null
        } else {
            // return "something"
            return folderMap[filePath]!![fileName]
        }
    }
}

fun separatePathAndName (path : String) : Pair<String, String> {

    val brokenPath : List<String> = path.split("/")
    val fileIndex : Int = brokenPath.size - 1 // final index for the file name
    val finalIndex : Int = fileIndex - 1 // index starts at 0 and I want to ignore filename

    val pathBuilder : StringBuilder = StringBuilder()

    // start at index 1 because fully qualified path name starts with /
    (1..finalIndex).forEach { index ->
        pathBuilder.append("/")
        pathBuilder.append(brokenPath[index])
    }

    return Pair(pathBuilder.toString(), brokenPath[fileIndex])
}
