from __future__ import unicode_literals

from django_google_maps import fields as map_fields
from django.db import models

# Create your models here.

class EndUser(models.Model):
    username=models.CharField(max_length=200)
    password=models.CharField(max_length=56)
    mac=models.CharField(max_length=200)
    lat=models.CharField(max_length=200)
    lng=models.CharField(max_length=200)
    def __str__(self):
        return self.username



class sqlerror(models.Model):
	error = models.CharField(max_length=1000)
	sql = models.CharField(max_length=1000)
	db = models.CharField(max_length=1000)
	time = models.DateTimeField(auto_now_add=True, null=False)
	_DATABASE = 'login'
	class Meta:
		db_table = 'sqlerror'


	def __unicode__(self):
		return self.error




class Rental(models.Model):
  address = map_fields.AddressField(max_length=200)
  geolocation = map_fields.GeoLocationField(max_length=100)