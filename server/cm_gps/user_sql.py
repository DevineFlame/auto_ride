from __future__ import unicode_literals
from django.db import *
from django.shortcuts import render_to_response,redirect
from django.core.urlresolvers import reverse
from django.utils.translation import activate, get_language_info

from home.models import sqlerror
from dean.sql_functions import *
from dean.misc_functions import *




"""
	All the user details required at a single place.
"""
def get_user_details(request):
	regno = request.session['regno']
	sql = "SELECT * from `stu_per_rec` where regno = '%s'"%(regno)
	result = query('default',sql)

	details = {
		'regno' : regno,
		'fname': result[0]['father_name'],
		'dob' : result[0]['dob'],
		'contactno': result[0]['phone'],
		'mobileno' : result[0]['mobile'],
		'email' : result[0]['email'],
		'addr' : result[0]['address'],
		'bloodgrp' : result[0]['blood_grp'],
		'sex' : result[0]['sex'],
		'state' : result[0]['state'],
		'cat' : result[0]['category']
	}

	sql = "SELECT * from `new_data` where regno = '%s'"%(regno)
	result = query('default',sql)

	details.update({
		'religion':result[0]['religion'],
		'caste':result[0]['caste'],
		'nation':result[0]['nationality'],
		'hindi_name':(result[0]['hindi']).encode("utf-8"),
		'alumni_fund':result[0]['caution_alumni'],
		'ifsc':result[0]['ifsc'],
		'acc_no':result[0]['acc_no'],
		'bbrn':result[0]['bbrn']
	})


	sql = "SELECT * from `stu_acad_rec` where regno = '%s'"%(regno)
	result = query('curr_reg_db',sql)

	details.update({
		'name':result[0]['name']
	})


	return details



def set_user_details(request,content):
	regno = request.session['regno']
	sql = "UPDATE `stu_per_rec` SET phone='%s',mobile='%s',email='%s',address='%s',blood_grp='%s' WHERE regno='%s'"%(content['contactno'],content['mobileno'],content['email'],content['addr'],content['bloodgrp'],regno)
	udi_query('default',sql)
	sql = "UPDATE `new_data` SET religion='%s',caste='%s',nationality='%s',hindi='%s',caution_alumni='%s',ifsc='%s',acc_no='%s',bbrn='%s' WHERE regno='%s'"%(content['religion'],content['caste'],content['nation'],content['hindiname'],content['alumnifund'],content['ifsc'],content['accno'],content['bankname'],regno)
	udi_query('default',sql)