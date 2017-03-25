from __future__ import division
from math import radians, cos, sin, asin, sqrt,pi
import random
from . import sql_functions
from datetime import datetime
# Testing simlation of generating random points 

import numpy as np


def create_random_point(x0,y0,distance):
    """
            Utility method for simulation of the points
    """   
    r = distance/ 111300
    u = np.random.uniform(0,1)
    v = np.random.uniform(0,1)
    w = r * np.sqrt(u)
    t = 2 * np.pi * v
    x = w * np.cos(t)
    x1 = x / np.cos(y0)
    y = w * np.sin(t)
    return (x0+x1, y0 +y)




def haversines(lon1, lat1, lon2, lat2):
    """
    Calculate the great circle distance between two points 
    on the earth (specified in decimal degrees)
    """
    # convert decimal degrees to radians 
    lon1, lat1, lon2, lat2 = map(radians, [lon1, lat1, lon2, lat2])

    # haversine formula 
    dlon = lon2 - lon1 
    dlat = lat2 - lat1 
    a = sin(dlat/2)**2 + cos(lat1) * cos(lat2) * sin(dlon/2)**2
    c = 2 * asin(sqrt(a)) 
    r = 6371 # Radius of earth in kilometers. Use 3956 for miles
    return c * r


def generate_lat_lang(i_lat,i_lng):
    i=i_lat
    j=i_lng
    M=2
    val=random.uniform(0.002,0.5)
    DB_NAME="cm_gps"
    for k in (1,20):
            #m=random.uniform(i_lat)
            m,n=create_random_point(i,j,100)
            sql="insert into test (lat,lng,type) values(%s,%s,\'%s\' )"%(str(m),str(n),"auto")
            print(sql)
            sql_functions.udi_query(DB_NAME,sql)
    


def dec_to_rad(value):
    PI=3.141592653589793
    return (PI * value) / 180;


def take_log(request):
    username=request.session["username"]
    lat=request.session['lat']
    lng=request.session['lng']
    status=request.session['status']
    client_ip = request.META['REMOTE_ADDR']

    sql="insert into log (\'%s\',\'%s\',\'%s\',\'%s\',NOW() )"%(username,client_ip,lat,lng,status)
    print(sql)



def get_notification_id(driver,passenger):
    notification_id=driver+"~"+passenger+"~"+str(datetime.now())
    return notification_id

def change_driver_status(driver,status):
    
    sql="update reserve set status=\'%s\' where driver=\'%s\'"%(status,driver)
    print(sql)
    sql_functions.query('cm_gps',sql)


def notify_passenger(driver,status,passenger):
    
    pass 