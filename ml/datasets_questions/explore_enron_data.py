#!/usr/bin/python



import pickle

enron_data = pickle.load(open("../final_project/final_project_dataset.pkl", "r"))
print type(enron_data), len(enron_data)
print len(enron_data[enron_data.keys()[0]])
keys = enron_data.keys()
print sorted(keys)
print sum([1 for person in enron_data.keys() if enron_data[person]['poi'] == True])
print enron_data['PRENTICE JAMES']['total_stock_value']
print enron_data['COLWELL WESLEY']['from_this_person_to_poi']
print enron_data['SKILLING JEFFREY K']['exercised_stock_options']
print enron_data['FASTOW ANDREW S']['total_payments']
print enron_data['LAY KENNETH L']['total_payments']
print enron_data['SKILLING JEFFREY K']['total_payments']
print 'quantified salary: ', sum([1 for person in enron_data.keys() if enron_data[person]['salary'] <> 'NaN'])
print 'certain email: ', sum([1 for person in enron_data.keys() if enron_data[person]['email_address'] <> 'NaN'])
print 'empty total payments: ', sum([1 for person in enron_data.keys() if enron_data[person]['total_payments'] == 'NaN'])
print 'empty total payments share: ', (1.0*sum([1 for person in enron_data.keys() if enron_data[person]['total_payments'] == 'NaN'])/len(enron_data))
print 'empty total payments POIs share: ', (sum([1.0 for person in enron_data.keys() if enron_data[person]['total_payments'] == 'NaN' and enron_data[person]['poi'] == True])/sum([1 for person in enron_data.keys() if enron_data[person]['poi'] == True]))