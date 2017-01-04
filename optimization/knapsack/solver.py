#!/usr/bin/python
# -*- coding: utf-8 -*-

from collections import namedtuple
Item = namedtuple("Item", ['index', 'value', 'weight'])
import time

def naive_solution(items, capacity):
    value = 0
    weight = 0
    taken = [0]*len(items)

    for item in items:
        if weight + item.weight <= capacity:
            taken[item.index] = 1
            value += item.value
            weight += item.weight

    return taken, value, weight

def dynamic_solution(items, capacity, n_items):
    item = items[n_items-1]
    if n_items == 0:
        return [], 0
    elif item.weight <= capacity:
        taken1, value1 = dynamic_solution(items, capacity, n_items-1)
        taken2, value2 = dynamic_solution(items, capacity-item.weight, n_items-1)
        value2 += item.value
        if value1 > value2:
            return taken1 + [0], value1
        else:
            return taken2 + [1], value2
    else:
        taken, value = dynamic_solution(items, capacity, n_items-1)
        return taken + [0], value

def density(item):
    return 1.*item.value/item.weight

def heuristic(items, capacity_left, is_sorted = False):
    def try_item(item):
        if try_item.capacity == 0:
            return 0.
        elif item.weight <= try_item.capacity:
            try_item.capacity -= item.weight
            return 1.*item.value
        else:
            v = 1.*try_item.capacity/item.weight * item.value
            try_item.capacity = 0
            return v

    try_item.capacity = capacity_left
    if capacity_left < 0:
        return 0

    #taken_sorted = list(filter(try_item, sorted(items, key = density, reverse=True)))
    #print taken_sorted
    #taken = [1 if item in taken_sorted else 0 for item in items]
    #return sum([item.value for item in taken_sorted])
    if is_sorted:
        return sum(map(try_item, items))
    else:
        return sum(map(try_item, sorted(items, key = density, reverse = True)))

def dfs(items, capacity, is_sorted = False, time_limit = None, level = 0):
    ans = [0] * len(items)
    max_value = 0
    i = 1
    start_time = time.time()
    time_limit_local = time_limit
    if time_limit and level < 2:
        time_limit_local = 1 + time_limit_local/10
    for item in items:
        local_choice = 0
        local_strategy = []
        if time_limit and time.time() - start_time > time_limit_local:
            break
        #print [el.index for el in items], i, item, capacity
        if item.weight == capacity:
            local_choice = item.value
            local_strategy = [0] * (i-1) + [1] + [0] * (len(items)-i)
        elif item.weight  < capacity:
            h = heuristic(items[i:], capacity-item.weight, is_sorted)
            if h + item.value > max_value:
                local_strategy, local_choice = dfs(items[i:], capacity-item.weight, is_sorted, time_limit_local, level+1)
                #print local_choice, item.value
                local_choice += item.value
                local_strategy = [0] * (i-1) + [1] + local_strategy
            else:
                local_choice = item.value
                local_strategy = []
        if local_choice > max_value:
            max_value = local_choice
            ans = local_strategy
        i += 1
    #print max_value, ans
    return ans, max_value

def calculate_solution_weight(items, taken):
    return reduce(lambda x,y: x+y, [i[0]*i[1].weight for i in zip(taken, items)])

def unsort_items(items, items_sorted, taken_sorted):
#    taken_unsorted = [0] * len(items)
#    for ind_sorted in [i for i, x in enumerate(taken) if x==1]:
#        local_item = items_sorted[i]
#        for ind in [i for i, x in enumerate(items) if x==local_item]:
#            if taken_unsorted[ind] == 0:
#                taken_unsorted[ind] = 1
#                break
    taken =[0] * len(items)
    for i, item in enumerate(items_sorted):
        taken[item.index] = taken_sorted[i]
    return taken

def solve_it(input_data):
    lines = input_data.split('\n')

    firstLine = lines[0].split()
    item_count = int(firstLine[0])
    capacity = int(firstLine[1])

    items = []

    for i in range(1, item_count+1):
        line = lines[i]
        parts = line.split()
        items.append(Item(i-1, int(parts[0]), int(parts[1])))

    takens = [None] * 5
    values = [None] * 5

    if len(items) < 40:
        takens[0], values[0] = dynamic_solution(items, capacity, len(items))

    items_sorted = sorted(items, key = density, reverse = True)

    takens[1], values[1], _ = naive_solution(items, capacity)
    takens[2], values[2], _ = naive_solution(items_sorted, capacity)
    takens[3], values[3] = dfs(items_sorted, capacity, True, 3600, 0)
    #takens[4], values[4] = dfs(items, capacity, False, 300, 0)

    best_indx = values.index(max(values))

    taken = takens[best_indx]
    value = values[best_indx]
    if best_indx == 2 or best_indx==3:
        taken = unsort_items(items, items_sorted, takens[best_indx])

    weight = calculate_solution_weight(items, taken)

    output_data = str(value) + ' ' + str(0) + '\n'
    output_data += ' '.join(map(str, taken))

    return output_data


if __name__ == '__main__':
    import sys
    if len(sys.argv) > 1:
        file_location = sys.argv[1].strip()
        with open(file_location, 'r') as input_data_file:
            input_data = input_data_file.read()
        print(solve_it(input_data))
    else:
        print('This test requires an input file.  Please select one from the data directory. (i.e. python solver.py ./data/ks_4_0)')
