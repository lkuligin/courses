package quickcheck

import common._

import org.scalacheck._
import Arbitrary._
import Gen._
import Prop._

abstract class QuickCheckHeap extends Properties("Heap") with IntHeap {

  lazy val genHeap: Gen[H] = for {
    i <- arbitrary[Int]
    h <- oneOf(const(empty), genHeap)
  } yield insert(i, h)

  implicit lazy val arbHeap: Arbitrary[H] = Arbitrary(genHeap)

  property("gen1") = forAll { (h: H) =>
    val m = if (isEmpty(h)) 0 else findMin(h)
    findMin(insert(m, h)) == m
  }

  property("min1") = forAll { a: Int =>
    val h = insert(a, empty)
    findMin(h) == a
  }

  property("same el") = forAll { (a: Int, b: Int) =>
    val h = insert(a, insert(b, empty))
    findMin(deleteMin(h)) == (a max b)
  }

  property("union") = forAll { (a: Int, b: Int) =>
    val h1 = insert(a, empty)
    val h2 = insert(b, empty)
    findMin(meld(h1, h2)) == (a min b)
  }

  property("order") = forAll { (h: H) =>
    ElementsToList(h).zip(ElementsToList(h).tail).forall {
      case (x, y) => x <= y
    }
  }

  property("union heap") = forAll { (h1: H, h2: H, h3: H) =>
    val left = meld(h1, meld(h2, h3))
    val right = meld(meld(h1, h2), h3)
    ElementsToList(left) == ElementsToList(right)
  }

  def ElementsToList(h: H): List[Int] = if (isEmpty(h)) Nil else findMin(h) ::ElementsToList(deleteMin(h))

}
