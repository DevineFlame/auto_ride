from django.conf.urls import url
from . import views

urlpatterns = [
    url(r'^$', views.index,name='index'),   
    url(r'^accept_data/$', views.accept_data,name='accept_data'), 
    url(r'^map_data/$', views.map_data,name='map_data'), 
    url(r'^map/$', views.map,name='map'), 
    url(r'^auto_in_range/$', views.auto_in_range,name='auto_in_range'), 
    url(r'^get_auto_details/$', views.get_auto_details,name='get_auto_details'), 
    url(r'^test/$', views.test,name='test'), 
    url(r'^user_signup/$', views.user_signup,name='user_signup'),
    url(r'^auto_driver_signup/$', views.auto_driver_signup,name='auto_driver_signup'),
    url(r'^login/$', views.login,name='login'),
    url(r'^user_notify_driver/$', views.user_notify_driver,name='user_notify_driver'),
    url(r'^driver_notify_user/$', views.driver_notify_user,name='driver_notify_user'),

]