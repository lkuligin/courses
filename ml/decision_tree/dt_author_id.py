#!/usr/bin/python

import sys
from time import time
sys.path.append("../tools/")
from email_preprocess import preprocess
from sklearn import tree
from sklearn.metrics import accuracy_score

if __name__ == "__main__":
	features_train, features_test, labels_train, labels_test = preprocess()
	print 'Number of features: ', len(features_train[0])
	clf = tree.DecisionTreeClassifier(min_samples_split=40)
	clf.fit(features_train, labels_train)   
	pred = clf.predict(features_test)
	acc = accuracy_score(pred, labels_test)
	print 'Decision tree accuracy: ', acc


