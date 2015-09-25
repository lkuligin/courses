#!/usr/bin/python

import os
import pickle
import re
import sys

sys.path.append( "../tools/" )
from parse_out_email_text import parseOutText

from_sara  = open("from_sara.txt", "r")
from_chris = open("from_chris.txt", "r")

from_data = []
word_data = []
to_remove = ["sara", "shackleton", "chris", "germani", "sshacklensf", "cgermannsf"]

temp_counter = 0

for name, from_person in [("sara", from_sara), ("chris", from_chris)]:
	for path in from_person:
		temp_counter += 1
		if 1 == 1:#temp_counter <= 100:
			path = os.path.join('C:/Users/kuligin/Downloads/enron_mail_20150507', path[:-1])
			email = open(path, "r")
			text = parseOutText(email)
			for word in to_remove:
				text = text.replace(word, '')
			word_data.append(text)
			if name == 'sara': from_data.append(0)
			else: from_data.append(1)
			email.close()
from_sara.close()
from_chris.close()
print 'successfully proccessed'
print len(word_data)

pickle.dump( word_data, open("your_word_data.pkl", "w") )
pickle.dump( from_data, open("your_email_authors.pkl", "w") )

from sklearn.feature_extraction.text import TfidfVectorizer
vectorizer = TfidfVectorizer(stop_words = 'english')
X = vectorizer.fit_transform(word_data)
print '# of words: ', len(vectorizer.get_feature_names())
#print vectorizer.get_feature_names()
print vectorizer.get_feature_names()[34576:34598]
