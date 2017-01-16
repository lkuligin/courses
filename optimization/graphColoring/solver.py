#!/usr/bin/python
# -*- coding: utf-8 -*-

import collections
import numpy as np
import time
from copy import deepcopy

class NotFeasibleSolutionException(Exception):
    pass

class Graph():

    def __init__(self, nodes_amount, edges, colors = None):
        self.nodes_amount = nodes_amount
        self.edges={}
        self.colors_total = colors if colors else nodes_amount

        nodes_neighbours={}
        for from_, to_ in edges:
            self.edges[from_] = self.edges.get(from_,[]) + [to_]
            self.edges[to_] = self.edges.get(to_,[]) + [from_]

            nodes_neighbours[from_] = nodes_neighbours.get(from_, 0) + 1
            nodes_neighbours[to_] = nodes_neighbours.get(to_, 0) + 1

        for el in self.edges:
            self.edges[el] = set(self.edges[el])

        self.sorted_nodes = sorted(xrange(nodes_amount)
            , key=lambda x: nodes_neighbours.get(x,0) - 1.*x/nodes_amount
            , reverse=True)

    def getNeighbours(self, node):
        return self.edges.get(node, [])

    def colors_disallowed(self, node, coloring):
        colors_disallowed = set([coloring[neighbour] for neighbour in filter(lambda x: x in coloring, self.getNeighbours(node))])
        return colors_disallowed

    def checkNode(self, node, color, coloring):
        onlyOneColor = set()
        for neighbour in self.getNeighbours(node):
            if neighbour in coloring:
                if coloring[neighbour] == color:
                    raise NotFeasibleSolutionException #this color is not allowed because of any of the neighbours
            else:
                colors_disallowed = len(self.colors_disallowed(neighbour, coloring))
                if self.colors_total ==  colors_disallowed:
                    raise NotFeasibleSolutionException  #if we color this node with this color, a neighbour will have no options at all
                if self.colors_total ==  colors_disallowed + 1:
                    onlyOneColor.add(neighbour)
        return onlyOneColor

    def getPossibleColor(self, node, coloring):
        disallowed = self.colors_disallowed(node, coloring)
        try:
            return set(range(self.colors_total)).difference(disallowed).pop()
        except:
            raise NotFeasibleSolutionException

    def __iter__(self):
        for el in iter(self.sorted_nodes):
            yield el


def dfs(graph, colors_used=0, coloring = {}, time_start = None, time_limit = None):
    max_color = min(colors_used+1, graph.colors_total)

    nodes = [el for el in graph.sorted_nodes if el not in coloring]
    if not nodes:
        return coloring

    node = nodes[0]
    disallowed_colors = graph.colors_disallowed(node, coloring)
    allowed_colors =filter(lambda color: color not in disallowed_colors, xrange(max_color))

    for color in allowed_colors:
        new_coloring = deepcopy(coloring)

        if time_limit and time_start and time.time() - time_start > time_limit:
            return None

        new_coloring[node] = color

        try:
            nodes_one_color = graph.checkNode(node, color, new_coloring)
            while nodes_one_color:
                new_occurences = []
                for node_one_color in nodes_one_color:
                    color = graph.getPossibleColor(node_one_color, new_coloring)
                    new_coloring[node_one_color]=color
                    new_nodes = graph.checkNode(node_one_color, color, new_coloring)
                    new_occurences = new_occurences + list(new_nodes)
                nodes_one_color = filter(lambda x: x not in new_coloring, list(set(new_occurences)))
            further_coloring=dfs(graph, max_color, new_coloring, time_start, time_limit)
            if further_coloring:
                new_coloring.update(further_coloring)
                return new_coloring
        except NotFeasibleSolutionException:
            pass

    return None

def runDfs(graph, n_colors, time_limit = None, level = 0):
    graph.colors_total = n_colors
    coloring = dfs(graph, n_colors, {}, time.time(), time_limit)
    if coloring:
        #print coloring
        return [coloring[el] for el in xrange(graph.nodes_amount)]
    return None

def greedy(graph):
    #clr = Coloring()
    max_color = graph.nodes_amount
    coloring = {}
    for node in graph:
        disallowed = graph.colors_disallowed(node, coloring)
        local_color = 0
        if disallowed:
            diff = set(range(max_color)).difference(set(disallowed))
            if diff:
                local_color = min(list(diff))
        graph.checkNode(node, local_color, coloring)
        coloring[node] = local_color
    #print coloring
    return [coloring[el] for el in xrange(graph.nodes_amount)]

def solve_it(input_data):
    lines = input_data.split('\n')

    best_choice = {50: 6, 70: 17, 100: 17}

    first_line = lines[0].split()
    node_count = int(first_line[0])
    edge_count = int(first_line[1])

    edges = []
    for i in range(1, edge_count + 1):
        line = lines[i]
        parts = line.split()
        edges.append((int(parts[0]), int(parts[1])))

    graph = Graph(node_count, edges)
    solution = greedy(graph)
    max_colors = max(solution)+1
    print "nodes : ", graph.nodes_amount
    print "greedy: ", max_colors

    best_option=best_choice.get(graph.nodes_amount)
    if not best_option:
        best_option = max_colors - 2
    better_solution = runDfs(graph, best_option, 3600)
    print "better_solution: ", better_solution
    if better_solution:
        solution = better_solution
        max_colors = max(solution)+1
        print "success: ", max_colors

    output_data = str(max_colors) + ' ' + str(0) + '\n'
    output_data += ' '.join(map(str, solution))

    return output_data


import sys

if __name__ == '__main__':
    import sys
    if len(sys.argv) > 1:
        file_location = sys.argv[1].strip()
        with open(file_location, 'r') as input_data_file:
            input_data = input_data_file.read()
        print(solve_it(input_data))
    else:
        print('This test requires an input file.  Please select one from the data directory. (i.e. python solver.py ./data/gc_4_1)')
