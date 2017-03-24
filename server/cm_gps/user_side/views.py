from django.shortcuts import render
from django.http import HttpResponse
from django.template import loader
from . import sql_functions
from .import misc_functions
from django.views.decorators.csrf import csrf_exempt


# Create your views here.
def index(request):
	sql="select id,name,lat,lng,type from markers where 1"
	DB_NAME="cm_gps"
	result=sql_functions.query(DB_NAME,sql)
	context = {'result': result}
	
	return render(request, 'user_side/index.html', context)



@csrf_exempt
def accept_data(request):
	data=request.POST.get("userData")
	print data
	context={"user_data":data}
	return render(request, 'user_side/success_register.html', context)


def map_data(request):
	lat=request.POST.get("lat")
	lng=request.POST.get("lng")
	context={"lat":lat,"lng":lng}
	return render(request, 'user_side/map.html',context)

	pass



def map(request):
	lat=12.879535
	lng=-87.624333
	context={"lat":lat,"lng":lng}
	return render(request, 'user_side/map.html',context)

def get_auto_details(request):
	sql="select id,name,lat,lng,type from markers where 1"
	DB_NAME="cm_gps"
	result=sql_functions.query(DB_NAME,sql)
	context = {'result': result}

	
	return HttpResponse(result)


#SELECT * FROM(    SELECT *,(((acos(sin((@latitude*pi()/180)) * sin((Latitude*pi()/180))+cos((@latitude*pi()/180)) * cos((Latitude*pi()/180)) * cos(((@longitude - Longitude)*pi()/180))))*180/pi())*60*1.1515*1.609344) as distance FROM Distances) t
#WHERE distance <= @distance


def auto_in_range(request):
	lat=request.POST.get("lat")
	lng=request.POST.get("lng")



def test(request):
	misc_functions.generate_lat_lang(-33.910751,151.194168)
	return HttpResponse("done")
	
	