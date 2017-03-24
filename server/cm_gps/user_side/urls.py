from django.conf.urls import url
from . import views

urlpatterns = [
    url(r'^$', views.index,name='index'),   
    url(r'^accept_data/$', views.accept_data,name='accept_data'), 
    url(r'^map_data/$', views.map_data,name='map_data'), 
    url(r'^map/$', views.map,name='map'), 
    url(r'^get_auto_details/$', views.get_auto_details,name='get_auto_details'), 
    url(r'^test/$', views.test,name='test'), 

]