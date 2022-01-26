PROJECT 

Zoho Git Project

Description

1)This project follows MVVM architecture

2)App works on Both Online and Offline Mode

3)This project uses Dagger Hilt,Retrofit,OkHttp3,Glide and Navigation Libraries

4)As this project follows Mvvm Architecture I have designed folder structure like this
    -->common (This Folder contains All Generic functions and Helper function need for the app entirely)
    -->datasources(This Folder contains Model classes and Remote and Local Database source Implementations)
    -->features (This Folder holds the View,View Model,Adapters and Binding Adapters)

5)LiveData is Used to Update the Ui for views via Databinding

6)KotlinFlow is also experimented by me for the first  to fetch data from remote and local data sources 

7)KotlinFlow fetches data from remote sources and Updates LiveData

9) Screens used
   List Screen Fragment = "Where Git pulic repo's are listed"
   Search Screen Fragment ="Where Git Repo's can be searched"
   GitRepoActivity Screen =which holds ListScreen Fragment and Search Screen Fragment
   
8) Activity View Model is shared to Both the Fragment Screen

9)Each Fragment has a seperate view Model to populate Ui

10)Ui Data Survives in ViewModel even after Configuration changes
  
11) Showed Some Api Error and Db Fetch Errors as Toast  


App Usescases:

1) when user opens the app with internet available :
   
   ListScreen:
   1)Data will be fetched from remote
   2)In List screen users can see listed data
   3) Also data Fetched from remoted will be stored in Db
   
   SearchScreen:
   1) Data will be fetched from remote
   2)In search screen users can see searched data
   3) Also data Fetched from remoted will be stored in Db
    

2) when user opens the app with no internet and no Offline data cache stored in Db :
   
   ListScreen:
   1) "No Network Error" View Will be shown
   2)Search Icon will be Gone from View
   3) On tapping retry each time it will try make a Remote Api Call 
   4)If Remote Api call fetch is not successfull "No network error" screen retails
   5)if Remote Api call is successfull "No network error screen" Gone and Search Icon is Visible

3) when user opens the app with no internet and Offline data cache stored in Db :

    ListScreen:
    1)Data will be fetched from local Db
    2)In List screen users can see listed data available in db

   SearchScreen:
    1) Data will be fetched from local db 
    2)In search screen users can see searched data in db
       
4) User switches Turns of internet while using the App
   
   1)The App can seamlessly fetch data from Local Db and Show


5) when user Turns on internet while using the App in Offline Mode
   
   1)The App can seamlessly fetch data from Remote Server and Show
    
   

   
   


   
 
   


