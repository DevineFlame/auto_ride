from django.shortcuts import render
from django.http import HttpResponse
from django.template import loader
from . import sql_functions
from .import misc_functions
from django.views.decorators.csrf import csrf_exempt
from django.http import JsonResponse
from decimal import Decimal
from datetime import datetime


# Create your views here.
def index(request):
    sql="select id,name,lat,lng,type from markers where 1"
    DB_NAME="cm_gps"
    result=sql_functions.query(DB_NAME,sql)
    context = {'result': result}
    
    return render(request, 'user_side/index.html', context)



@csrf_exempt
def accept_data(request):
    #data=request.POST.get("userData")
    
    email=request.POST.get("email")
    password=request.POST.get("password")
    
    print email+" "+password

    #context={"user_data":username}
    #return JsonResponse({'id':1234567})
    #return render(request, 'user_side/success_register.html', context)
    return HttpResponse("shubham")


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
@csrf_exempt
def get_auto_details(request):
    sql="select id,name,lat,lng,type from markers where 1"
    DB_NAME="cm_gps"
    result=sql_functions.query(DB_NAME,sql)
    return JsonResponse({'result':result})
    


#SELECT * FROM(    SELECT *,(((acos(sin((@latitude*pi()/180)) * sin((Latitude*pi()/180))+cos((@latitude*pi()/180)) * cos((Latitude*pi()/180)) * cos(((@longitude - Longitude)*pi()/180))))*180/pi())*60*1.1515*1.609344) as distance FROM Distances) t
#WHERE distance <= @distance

@csrf_exempt
def auto_in_range(request):
    lat=request.POST.get("lat")
    lng=request.POST.get("lng")
    #lat= misc_functions.dec_to_rad(-32.9108) 
    #lng=misc_functions.dec_to_rad(149.194) 
    #lat=25.481241
    #lng=81.856597
    print(lat)
    print(lng)

    lat=float(lat)
    lng=float(lng)
    max_lat=lat+0.057790
    min_lat=lat-0.052510

    max_lng=lng+0.052790
    min_lng=lng-0.056430
    #misc_functions.generate_lat_lang(lat,lng)

    sql="select * from test where lat < %s && lat > %s && lng<%s && lng >%s  limit 1,10" %(max_lat,min_lat,max_lng,min_lng)
    DB_NAME="cm_gps"
    res=sql_functions.query(DB_NAME, sql)
    
    print(sql)
    return JsonResponse({'result':res})
    

   




@csrf_exempt
def user_signup(request):
    username=request.POST.get('username')
    name=request.POST.get('name')
    email=request.POST.get("email")
    password=request.POST.get("password")
    address=request.POST.get('address')
    msg={}
    sql="select username from users where username=%s"%(username)
    print(sql)
    DB_NAME="cm_gps"
    res=sql_functions.query(DB_NAME, sql)
    if res :
         msg["error"]="user_exist"
    else:
        try:
            sql='insert into users values(\'%s\',\'%s\',\'%s\',\'%s\',\'%s\')'\
            %(username,name,email,password,address)
            print(sql)
            sql_functions.udi_query(DB_NAME,sql)
            msg['error']="success"
        except DatabaseError as e:
            msg["error"]="data_not_inserted"
    return JsonResponse({'msg':msg})


@csrf_exempt
def auto_driver_signup(request):
    username=request.POST.get('username')
    name=request.POST.get('name')
    email=request.POST.get("email")
    password=request.POST.get("password")
    address=request.POST.get('address')
    license=request.POST.get('lic')
    vehicle=request.POST.get('platenumber')
    auto_type=request.POST.get('type')
    status=1;
    msg={}
    sql="select username from users where username=%s"%(username)
    DB_NAME="cm_gps"
    res=sql_functions.query(DB_NAME, sql)
    if res :
         msg["error"]="user_exist"
    else:
        try:
            sql='insert into users values(\'%s\',\'%s\',\'%s\',\'%s\',\'%s\')'\
            %(username,name,email,password,address)
            print(sql)
            sql_functions.udi_query(DB_NAME,sql)

            sql='insert into drivers values(\'%s\',\'%s\',\'%s\',\'%s\')'\
            %(username,vehicle,auto_type,status)
            sql_functions.udi_query(DB_NAME,sql)

            msg['error']="success"
        except DatabaseError as e:
            msg["error"]="data_not_inserted"
    return JsonResponse({'msg':msg})    


@csrf_exempt
def login(request):
    email=request.POST.get("email")
    password=request.POST.get("password")
    sql="select username from users where email=\'%s\' and password=\'%s\'"%(email,password)
    print(sql)
    DB_NAME="cm_gps"
    msg={}
    res=sql_functions.query(DB_NAME, sql)
    if res:
        request.session["username"]=res[0]
        request.session["email"]=email
        msg["error"]="success"  
    else:
        msg["error"]="failed"
    

    return  JsonResponse({'loginStatus':msg})



@csrf_exempt
def get_notifications(request):       #notifications
    user_type=request.POST.get('type')
    if user_type =='driver':
        driver_name=request.POST['username']
        sql="select * from notifications where driver=\'%s\'"%(driver_name)
        print(sql)
        res=sql_functions.query("cm_gps",sql)
        print(res)
        return JsonResponse({'result':res})
    if user_type =='passenger':
        passenger_name=request.POST['username']
        sql="select * from notifications where passenger=\'%s\'"%(passenger_name)
        print(sql)
        res=sql_functions.query("cm_gps",sql)
        return JsonResponse({'result':res})
       


@csrf_exempt
def logout(request):
    try:
        del request.session['username']
    except KeyError:
        pass
    return HttpResponse("You're logged out.")



@csrf_exempt
def change_status(request):
    status=request.POST.get('status')
    username=request.session['username']
    if status == 'enable':
        sql="update users set status=1 where username=\'%s\'"%(requsername)
    if status == 'disable':
        sql="update users set status=1 where username=\'%s\'"%(requsername)

    sql_functions.udi_query("users",sql)
    pass

@csrf_exempt
def update_notifications(request):
    driver_name=request.POST.get('driver')
    passenger=request.POST.get('user')
    price=request.POST.get('price')
    size=request.POST.get('size')
    dest=request.POST.get('dest')
    p_lat=request.POST.get('p_lat')
    p_lng=request.POST.get('p_lng')
    dr_lat=request.POST.get('dr_lat')
    dr_lng=request.POST.get('dr_lng')
    init="passenger"
    status=1;
    date=datetime.now();

    sql="insert into notifications values(\'%s\',\'%s\',\'%s\',\'%s\',\'%s\',\'%s\',\'%s\',\'%s\',\'%s\',\'%s\',\'%s\',\'%s\')"\
    %(driver_name,passenger,price,size,dest,p_lat,p_lng,dr_lat,dr_lng,init,date)
   
    sql="insert into reserve values(\'%s\',\'%s\',\'%s\')"\
    %(driver_name,passenger,status)
    print(sql)
   




def test(request):
    misc_functions.generate_lat_lang(25.481241,81.856597)
    return HttpResponse("done")
@csrf_exempt  
def driver_reply(request):
    print("hello")
    passenger=request.POST.get('passenger')
    driver_name=request.POST.get('driver')
    status=request.POST.get('status')
    misc_functions.change_driver_status(driver_name,status)
    misc_functions.notify_passenger(driver_name,status,passenger)
    return HttpResponse("status changed")




