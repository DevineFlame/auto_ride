from math import radians, cos, sin, asin, sqrt
from . import sql_functions

def haversine(lon1, lat1, lon2, lat2):
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
    DB_NAME="cm_gps"
    for x in xrange(-M,+M):
        for y in xrange(-M,+M):
            m=x*0.25+i_lat
            n=y*0.25+i_lng
            sql="insert into test (lat,lng,type) values(%s,%s,\'%s\' )"%(str(m),str(n),"elekRik")
            print(sql)
            sql_functions.udi_query(DB_NAME,sql)
    

