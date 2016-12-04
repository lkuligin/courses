package recfun

object Main {
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row)  + " ")
      println()
    }
    println(balance(":-)".toList))
  }

    def pascal(c: Int, r: Int): Int = {
      if (r == 0) 1 else {
        if ((c == 0) || (r==c)) 1 else pascal(c-1, r-1) + pascal(c, r-1)
      }
    }

    def balance(chars: List[Char]): Boolean = {
      def process_line(chars: List[Char], temp_res: Int): Boolean = {
        if (chars.isEmpty) temp_res == 0 else {
          if (chars.head == '(') process_line(chars.tail, temp_res+1) else {
            if (chars.head == ')') temp_res > 0 && process_line(chars.tail, temp_res - 1)
            else process_line(chars.tail, temp_res)

          }
        }
      }
      process_line(chars, 0)
    }

    def countChange(money: Int, coins: List[Int]): Int = {
      if (money == 0) 1 else if (money > 0 && !coins.isEmpty) {
        countChange(money-coins.head, coins) + countChange(money, coins.tail)
      } else 0
    }
  }
