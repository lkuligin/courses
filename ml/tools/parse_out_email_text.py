#!/usr/bin/python

from nltk.stem.snowball import SnowballStemmer
#from nltk.tokenize import word_tokenize
import string

def parseOutText(f):
	f.seek(0)  ### go back to beginning of file (annoying)
	all_text = f.read()
	### split off metadata
	content = all_text.split("X-FileName:")
	words = ""
	if len(content) > 1:
		### remove punctuation
		text_string = content[1].translate(string.maketrans("", ""), string.punctuation)
		#words = text_string
		stemmer = SnowballStemmer("english")
		for word in text_string.split():#word_tokenize(text_string):
			#word = stemmer.stem(word.strip().strip('\n\r\t'))
			#if len(word) > 0: 
			#	#words = ' '.join((words,word))
			#	words += ' '
			words += ' ' + stemmer.stem(word)
	words = words.lstrip()
	return words.strip()

def main():
	ff = open("../text_learning/test_email.txt", "r")
	text = parseOutText(ff)
	print 'test'
	print text

if __name__ == '__main__':
	main()

