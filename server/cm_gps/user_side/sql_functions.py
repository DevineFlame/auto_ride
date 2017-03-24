from django.db import connections,DatabaseError
from django.shortcuts import render_to_response,redirect
from django.core.urlresolvers import reverse

from .models import sqlerror


def dictfetchall(cursor):
    "Return all rows from a cursor as a dict"
    columns = [col[0] for col in cursor.description]
    return [
            dict(zip(columns, row))
            for row in cursor.fetchall()
        ]


def get_name(regno):
	db = 'simple_cms'
	cursor = connections[db].cursor()
	cursor.execute("select * from user where regno=%s",[regno])
	row = dictfetchall(cursor)
	return row[0]['name']
	

def query(db,sql):
	cursor = connections[str(db)].cursor()
	try:
		cursor.execute(sql)
		row = dictfetchall(cursor)
		return row
	except DatabaseError as e:
		sqlobj = sqlerror.objects.create(error = e,sql = sql,db = db)
		sqlobj.save()
	
	
def udi_query(db,sql):
	cursor = connections[str(db)].cursor()
	try:
		cursor.execute(sql)
	except DatabaseError as e:
		sqlobj = sqlerror.objects.create(error = e,sql = sql,db = db)
		sqlobj.save()