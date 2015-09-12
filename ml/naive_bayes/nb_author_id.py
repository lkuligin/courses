#!/usr/bin/python

""" 
    this is the code to accompany the Lesson 1 (Naive Bayes) mini-project 

    use a Naive Bayes Classifier to identify emails by their authors
    
    authors and labels:
    Sara has label 0
    Chris has label 1

"""



import sys
from time import time
sys.path.append("../tools/")
from email_preprocess import preprocess
from sklearn.naive_bayes import GaussianNB
from sklearn.metrics import accuracy_score

### features_train and features_test are the features for the training and testing datasets, respectively
### labels_train and labels_test are the corresponding item labels
if __name__ == "__main__":
	features_train, features_test, labels_train, labels_test = preprocess()
	clf = GaussianNB()
	t0 = time()
	clf.fit(features_train, labels_train)
	t1 = time()
	pred = clf.predict(features_test)
	t2 = time()
	print "predicting time: ", round(t2-t1, 3), "s"
	print "training time: ", round(t1-t0, 3), "s"
	print accuracy_score(labels_test, pred)

