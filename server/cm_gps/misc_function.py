from django.utils.html import escape
from django.utils.safestring import mark_safe
from hashlib import md5
from . sql_functions import *

def sanitize(my_string):
	return mark_safe(escape(my_string).encode('ascii', 'xmlcharrefreplace').strip())

def md5hash(my_string):
	my_string=my_string.encode('utf-8')
	hashed = md5(my_string).hexdigest()
	return hashed


def login(regno,password):
	sql = "SELECT * from login where regno='%s' and pass='%s'"%(regno,password)
	row = query('login',sql)
	if len(row) > 0:
		return 1
	else:
		return 0

def is_logged(request):
	if 'regno' in request.session:
		return 1
	else:
		return 0





'''
	To populate option list in crispy form... we need to return a list of tuples... 
	otherwise "Value too large to unpack" error occurs.
'''
def get_nation_list():
	sql = "SELECT * from country order by name"
	result = query('curr_reg_db',sql)
	nationlist = []
	for key in result:
		nationlist.append((key['id'],key['name']))
	return nationlist



def no_of_y(request):
	regno = request.session['regno']
	sql = "SELECT * from stepprogress where regno='%s'"%(regno)
	result = query('curr_reg_db',sql)
	return result[0]