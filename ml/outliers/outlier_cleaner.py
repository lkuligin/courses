#!/usr/bin/python
import numpy as np

def outlierCleaner(predictions, ages, net_worths):
	ln = len(predictions)
	outliers = int(0.1*ln)
	print 'outliers: ', ln, outliers
	cleaned_data = []
	data = [(float(ages[i]), float(net_worths[i]), float(abs(predictions[i]-net_worths[i]))) for i in range(ln)]
	data = sorted(data, key = lambda el: el[2])
	cleaned_data = data[:-outliers]
	return cleaned_data

