package com.example.newswave


   /** We are not setting the setter methods in our response in
    Transfer Objects. Since we are not going to use this value **/
   class Source(private val id: String?, private val name: String?) {

       fun getId(): String? {
           return id
       }

       fun getName(): String? {
           return name
       }
   }
