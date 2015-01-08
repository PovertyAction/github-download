# Adapted from http://stackoverflow.com/questions/1871524/convert-from-json-to-csv-using-python
import csv
import json

with open("comments.txt") as file:
	data = json.load(file)

with open("comments.csv", "wb") as file:
	csv_file = csv.writer(file)
	csv_file.writerow(['user:login', 'path', 'commit_id', 'url', 'line',
		'html_url', 'created_at', 'body'])
	for item in data:
		csv_file.writerow([item['user']['login'], item['path'],
			item['commit_id'], item['url'], item['line'], item['html_url'],
			item['created_at'], item['body']])
