from nltk.corpus import stopwords
sw = stopwords.words("english")
print 'Amount of English stopwords: ', len(sw)
sw = stopwords.words("russian")
print 'Amount of Russian stopwords: ', len(sw)
for word in sw:
	print word
from nltk.stem.snowball import SnowballStemmer
stemmer = SnowballStemmer("english")
stemmer.stem("responsiveness")
