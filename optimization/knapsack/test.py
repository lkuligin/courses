#!/usr/bin/python
# -*- coding: utf-8 -*-

import unittest
import sys, os
import logging
#from memory_profiler import profile

cur_path = os.path.normpath(os.path.join(os.getcwd(), os.path.dirname(__file__)))
sys.path.append(cur_path)
from solver import naive_solution, dynamic_solution, dfs
from solver import Item, heuristic, density, unsort_items
import time

def timeit(func):
    def timed(*args, **kwargs):
        start_time = time.time()
        result = func(*args, **kwargs)
        end_time = time.time()
        #logger.info
        print ('{0} ({1}, {2}) required {3:.2f} sec'.format(func.__name__, args, kwargs, end_time-start_time))
        return result
    return timed

class SimpleTest(unittest.TestCase):
    #data = '''4 11\n8 4\n10 5\n15 8\n4 3'''
    items = [Item(1, 8, 4), Item(2, 10, 5), Item(3, 15, 8), Item(4, 4, 3)]

    @timeit
    #@profile
    def test_simple(self):
        taken, value, weight = naive_solution(self.items, 11)
        self.assertEqual(18, value)
        self.assertEqual(9, weight)

    @timeit
    #@profile
    def dynamic_solution(self):
        taken, value = dynamic_solution(self.items, 11, len(self.items))
        self.assertEqual(19, value)
        self.assertEqual([0, 0, 1, 1], taken)

    @timeit
    #@profile
    def dfs(self):
        taken, value = dfs(self.items, 11, False)
        self.assertEqual(19, value)
        self.assertEqual([0, 0, 1, 1], taken)


class HeuristicTest(unittest.TestCase):
    items = [Item(1, 2, 4), Item(2, 3, 5), Item(3, 4, 3)]

    def test_raw(self):
        result = heuristic(self.items, 12)
        self.assertEqual(9, result)

    def no_capacity(self):
        result = heuristic(self.items, 2)
        self.assertEqual(8./3, result)

    def partial_indx(self):
        result = heuristic(self.items[1:], 5)
        print result
        self.assertEqual(5.2, result)

class UnsortTest(unittest.TestCase):
    items = [Item(0, 2, 4), Item(1, 3, 5), Item(2, 4, 3), Item(3, 4, 3)]

    def simpe_unsorting(self):
        items_sorted = sorted(self.items, key = density, reverse = True)
        taken_sorted = [1, 1, 1, 0]
        result = unsort_items(self.items, items_sorted, taken_sorted)
        print result
        expected = [0, 1, 1, 1]
        self.assertEqual(set(expected), set(result))

    def partial_equals(self):
        items_sorted = sorted(self.items, key = density, reverse = True)
        taken_sorted = [1, 0, 0, 0]
        result = unsort_items(self.items, items_sorted, taken_sorted)
        expected = [0, 0, 1, 0]
        self.assertEqual(set(expected), set(result))

def create_suite():
    test_suite = unittest.TestSuite()
    test_suite.addTest(HeuristicTest('test_raw'))
    test_suite.addTest(HeuristicTest('no_capacity'))
    test_suite.addTest(HeuristicTest('partial_indx'))
    test_suite.addTest(SimpleTest('test_simple'))
    test_suite.addTest(SimpleTest('dynamic_solution'))
    test_suite.addTest(SimpleTest('dfs'))
    test_suite.addTest(UnsortTest('simpe_unsorting'))
    test_suite.addTest(UnsortTest('partial_equals'))
    return test_suite

if __name__ == '__main__':
  suite = create_suite()

  runner = unittest.TextTestRunner()
  runner.run(suite)
