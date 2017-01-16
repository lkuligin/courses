#!/usr/bin/python
# -*- coding: utf-8 -*-

import unittest
import sys, os
from solver import Graph, ColoringConstraints, greedy, runDfs

class  ColoringConstraintsTest (unittest.TestCase):
    nodes_amount=4
    edges=[(0,1),(1,2),(1,3)]
    gr = Graph(nodes_amount, edges)

    def tryColorSuccess(self):
        cc = ColoringConstraints(self.gr)
        self.assertEqual(True, paintNode.color(0, 1))

    def getPossibleColorEasy(self):
        cc = ColoringConstraints(self.gr, 1)
        color = cc.getPossibleColor(set([]))
        self.assertEqual(0, color)

    def getPossibleColorEasy1(self):
        cc = ColoringConstraints(self.gr, 4)
        color = cc.getPossibleColor(set([0,1,3]))
        self.assertEqual(2, color)

    def getPossibleColorNone(self):
        cc = ColoringConstraints(self.gr, 4)
        color = cc.getPossibleColor(set([0,1]))
        self.assertEqual(None, color)


class GraphTest(unittest.TestCase):
    nodes_amount=4
    edges=[(0,1),(1,2),(1,3)]
    gr = Graph(nodes_amount, edges)
    #print gr.edges

    def graphBasic(self):
        result = [i for i in self.gr]
        expected = [1, 0, 2, 3]
        self.assertEqual(expected, result)

    def greedyTest(self):
        results = greedy(self.gr)
        expected = [1,0,1,1]
        self.assertEqual(expected,results)

    def dfsTestEasy(self):
        #cCrnt=ColoringConstraints(self.gr)
        results = runDfs(self.gr, 5)
        expected = [1,0,1,1]
        self.assertEqual(expected,results)

    def dfsTestEasy1(self):
        results = runDfs(self.gr, 1)
        print "result2: ", results
        self.assertEqual(None,results)

class GraphTest1(unittest.TestCase):
    nodes_amount=4
    edges=[(0,1),(1,2)]
    gr = Graph(nodes_amount, edges)

    def greedyTest(self):
        results = greedy(self.gr)
        print results
        expected = [1,0,1,0]
        self.assertEqual(expected,results)

    def dfsTest(self):
        results = runDfs(self.gr,2)
        print results
        expected = [1,0,1,0]
        self.assertEqual(expected,results)

def create_suite():
    test_suite = unittest.TestSuite()
    #test_suite.addTest(GraphTest('graphBasic'))
    #test_suite.addTest(GraphTest('greedyTest'))
    test_suite.addTest(GraphTest('dfsTestEasy'))
    #test_suite.addTest(GraphTest1('greedyTest'))
    test_suite.addTest(GraphTest1('dfsTest'))
    #test_suite.addTest(ColoringConstraintsTest('getPossibleColorEasy'))
    #test_suite.addTest(ColoringConstraintsTest('getPossibleColorEasy1'))
    #test_suite.addTest(ColoringConstraintsTest('getPossibleColorNone'))
    return test_suite

if __name__ == '__main__':
  suite = create_suite()

  runner = unittest.TextTestRunner()
  runner.run(suite)
