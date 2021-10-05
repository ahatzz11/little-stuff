/*
For the purposes of this interview, imagine that we own a store. This
store doesn't always have customers shopping: there might be some long
stretches of time where no customers enter the store. We've asked our
employees to write simple notes to keep track of when customers are
shopping and when they aren't by simply writing a single letter every
hour: 'Y' if there were customers during that hour, 'N' if the store
was empty during that hour.

For example, our employee might have written "Y Y N Y", which means
the store was open for four hours that day, and it had customers
shopping during every hour but its third one.

  hour: | 1 | 2 | 3 | 4 |
  log:  | Y | Y | N | Y |
                  ^
                  |
            No customers during hour 3

We suspect that we're keeping the store open too long, so we'd like to
understand when we *should have* closed the store. For simplicity's
sake, we'll talk about when to close the store by talking about how
many hours it was open: if our closing time is `2`, that means the
store would have been open for two hours and then closed.

  hour:         | 1 | 2 | 3 | 4 |
  log:          | Y | Y | N | Y |
  closing_time: 0   1   2   3   4
                ^               ^
                |               |
         before hour #1    after hour #4

(A closing time of 0 means we simply wouldn't have opened the store at
all that day.)

First, let's define a "penalty": what we want to know is "how bad
would it be if we had closed the store at a given hour?" For a given
log and a given closing time, we compute our penalty like this:

  +1 penalty for every hour that we're *open* with no customers
  +1 penalty for every hour that we're *closed* when customers would have shopped

For example:

  hour:    | 1 | 2 | 3 | 4 |   penalty = 3:
  log:     | Y | Y | N | Y |     (three hours with customers after closing)
  penalty: | * | * |   | * |
           ^
           |
         closing_time = 0

  hour:    | 1 | 2 | 3 | 4 |   penalty = 2:
  log:     | N | Y | N | Y |      (one hour without customers while open +
  penalty: | * |   |   | * |       one hour with customers after closing)
                   ^
                   |
                 closing_time = 2

  hour:    | 1 | 2 | 3 | 4 |   penalty = 1
  log:     | Y | Y | N | Y |    (one hour without customers while open)
  penalty: |   |   | * |   |
                           ^
                           |
                         closing_time = 4

Note that if we have a log from `n` open hours, the `closing_time`
variable can range from 0, meaning "never even opened", to n, meaning
"open the entire time".

1a)
Write a function `compute_penalty` that computes the total penalty, given
  a store log (as a space separated string) AND
  a closing time (as an integer)

In addition to writing this function, you should use tests to
demonstrate that it's correct.

## Examples

compute_penalty("Y Y N Y", 0) should return 3
compute_penalty("N Y N Y", 2) should return 2
compute_penalty("Y Y N Y", 4) should return 1



1b)
Write another function: `find_best_closing_time`, that returns the
best closing time given just a store log. You should write this in
terms of `compute_penalty`.

Again, you should use tests to demonstrate that it's correct.

## Example

find_best_closing_time("Y Y N N") should return 2
*/

import java.util.*

fun getStoreMap(stringStoreLog : String) : MutableMap<Int, String> {
    val storeLog : List<String> = stringStoreLog.split(" ")
    val storeLogMap : MutableMap<Int, String> = mutableMapOf()

    // create map of hour to log
    storeLog.forEachIndexed { index, value ->
        storeLogMap[index + 1] = value
    }

    return storeLogMap
}


fun computePenalty(stringStoreLog : String, closingTime : Int) : Int {

    val storeLogMap : MutableMap<Int, String> = getStoreMap(stringStoreLog)

    var penaltyCounter : Int = 0

    storeLogMap.forEach { (hour, log) ->

        // check open
        if (hour <= closingTime && log == "N") {
            penaltyCounter++
        }

        // check closed
        if (hour > closingTime && log == "Y") {
            penaltyCounter++
        }
    }

    return penaltyCounter
}

fun findBestClosingTime(stringStoreLog : String) : Int {
//    val storeLogMap : MutableMap<Int, String> = getStoreMap(stringStoreLog)

    val closingTimeToPenaltyMap : MutableMap<Int, Int> = mutableMapOf()

    (0..stringStoreLog.split(" ").size).forEach { potentialClosingTime ->

        closingTimeToPenaltyMap[potentialClosingTime] = computePenalty(stringStoreLog, potentialClosingTime)
    }

    val smallestPenalty : Int = Collections.min(closingTimeToPenaltyMap.values)

    var bestClosingTime : Int = 0

    // finds the first best closing time, even if many exist later on
    closingTimeToPenaltyMap.forEach { (closingTime, penalty) ->
        if (penalty == smallestPenalty) {
            bestClosingTime = closingTime
            return bestClosingTime
        }
    }

    // not reachable
    return -1
}

// little unit test
fun assertEquals(value : Int, expectedValue : Int) {
    if (value == expectedValue) {
        println("successful calculation")
    } else {
        println("error, incorrect calculation")
    }
}


assertEquals(findBestClosingTime("Y Y N N"), 2)
assertEquals(findBestClosingTime("Y Y N Y"), 2)

assertEquals(findBestClosingTime("Y Y Y N N N N" ), 3)
assertEquals(findBestClosingTime(""), 0)
assertEquals(findBestClosingTime("N N N N" ), 0)
assertEquals(findBestClosingTime("Y Y Y Y" ), 4)
assertEquals(findBestClosingTime("N Y Y Y Y N N N Y N N Y Y N N N N Y Y N N Y N N N" ), 5)
assertEquals(findBestClosingTime("N N N N N Y Y Y N N N N Y Y Y N N N Y N Y Y N Y N" ), 0)
assertEquals(findBestClosingTime("Y Y N N N Y Y N Y Y N N N Y Y N N Y Y Y N Y N Y Y" ), 25)

// positive test cases
assertEquals(computePenalty("Y Y N Y", 0), 3)
assertEquals(computePenalty("N Y N Y", 2), 2)
assertEquals(computePenalty("Y Y N Y", 4), 1)

// negative test cases
assertEquals(computePenalty("Y Y N Y", 0), 1)

// additional cases
assertEquals(computePenalty("Y Y Y N N N N", 0), 3)
assertEquals(computePenalty("Y Y Y N N N N", 7), 4)
assertEquals(computePenalty("Y Y Y N N N N", 3), 0)
assertEquals(computePenalty("", 0),  0)
assertEquals(computePenalty("Y N Y N N N N", 3), 1)
