#!/usr/bin/python

""" 
    this is the code to accompany the Lesson 2 (SVM) mini-project

    use an SVM to identify emails from the Enron corpus by their authors
    
    Sara has label 0
    Chris has label 1

"""
    
import sys
from time import time
sys.path.append("../tools/")
from email_preprocess import preprocess
from sklearn.svm import SVC
from sklearn.metrics import accuracy_score

### features_train and features_test are the features for the training
### and testing datasets, respectively
### labels_train and labels_test are the corresponding item labels
if __name__ == "__main__":
	features_train, features_test, labels_train, labels_test = preprocess()
	features_train1 = features_train[:len(features_train)/100] 
	labels_train1 = labels_train[:len(labels_train)/100] 
	#clf = SVC(kernel="linear")
	#clf.fit(features_train1, labels_train1)
	#pred = clf.predict(features_test)
	#acc = accuracy_score(pred, labels_test)
	#print 'Linear SVM accuracy: ', acc
	#print 'Linear SVM accuracy on 1% of the train set: ', acc
	
	clf2 = SVC(C=10000.0, kernel="rbf")
	clf2.fit(features_train1, labels_train1)
	pred2 = clf2.predict(features_test)
	acc2 = accuracy_score(pred2, labels_test)
	print 'Rbf SVM accuracy on 1% of the train set: ', acc2
	
	clf3 = SVC(C=10000.0, kernel="rbf")
	clf3.fit(features_train, labels_train)
	pred3 = clf3.predict(features_test)
	acc3 = accuracy_score(pred3, labels_test)
	print 'Rbf SVM accuracy C=10000: ', acc3
	print pred3[10]
	print 'Amount of predicted Chris\'s emails: ', sum(pred3)
