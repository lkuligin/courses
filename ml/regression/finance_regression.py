#!/usr/bin/python

"""
    starter code for the regression mini-project
    
    loads up/formats a modified version of the dataset
    (why modified?  we've removed some trouble points
    that you'll find yourself in the outliers mini-project)

    draws a little scatterplot of the training/testing data

    you fill in the regression code where indicated

"""    


import sys
import pickle
sys.path.append("../tools/")
from feature_format import featureFormat, targetFeatureSplit
dictionary = pickle.load( open("../final_project/final_project_dataset_modified.pkl", "r") )

### list the features you want to look at--first item in the 
### list will be the "target" feature
features_list = ["bonus", "salary"]
data = featureFormat( dictionary, features_list, remove_any_zeroes=True)
target, features = targetFeatureSplit( data )

### training-testing split needed in regression, just like classification
from sklearn.cross_validation import train_test_split
feature_train, feature_test, target_train, target_test = train_test_split(features, target, test_size=0.5, random_state=42)
train_color = "b"
test_color = "r"

from sklearn import linear_model
reg = linear_model.LinearRegression()
reg.fit(feature_train, target_train)
print 'Regression slope: ', reg.coef_
print 'Regression intercept: ', reg.intercept_
print 'R-score on training data: ', reg.score(feature_train, target_train)
print 'R-score on testing data: ', reg.score(feature_test, target_test)
print 'R-score on testing data: ', reg.score(feature_test, target_test)

features_list2 = ["bonus", "long_term_incentive"]
data2 = featureFormat( dictionary, features_list2, remove_any_zeroes=True)
target2, features2 = targetFeatureSplit(data2)
feature_train2, feature_test2, target_train2, target_test2 = train_test_split(features2, target2, test_size=0.5, random_state=42)

reg2 = linear_model.LinearRegression()
reg2.fit(feature_train2, target_train2)
print 'R-score on testing data on different factors: ', reg2.score(feature_test2, target_test2)

### draw the scatterplot, with color-coded training and testing points
import matplotlib.pyplot as plt
for feature, target in zip(feature_test, target_test):
    plt.scatter( feature, target, color=test_color ) 
for feature, target in zip(feature_train, target_train):
    plt.scatter( feature, target, color=train_color ) 

### labels for the legend
plt.scatter(feature_test[0], target_test[0], color=test_color, label="test")
plt.scatter(feature_test[0], target_test[0], color=train_color, label="train")




### draw the regression line, once it's coded
try:
    plt.plot( feature_test, reg.predict(feature_test) )
except NameError:
    pass
reg.fit(feature_test, target_test)
plt.plot(feature_train, reg.predict(feature_train), color="b") 
print 'Coefficient without outlier :', reg.coef_
plt.xlabel(features_list[1])
plt.ylabel(features_list[0])
plt.legend()
plt.show()
