# Product Discovery
TEKO Coding Test - Mobile Engineer


## Introduction 
Functional module for mobile called **Product Discovery**, developed on Android platform with `Kotlin` Language  
This module includes 2 screens: `Product Listing` and `Product Detail`

Users can search products by text. And text search will be saved and suggested for future searches. 

Users can add product to cart, but don't check if the project has been added to the cart, 
because it will do with the shopping cart management feature.

The application has loaded by the page and the error message when calling api timeout, no internet connect, and server interrupted.     

The API does not return product data of the same type, so this application doesn't have product of the same type as design

## Pre-requisites

* Android Studio 3.5 or later 
* Make sure Android Studio is updated, as well as your SDK and Gradle

## Library 

This project used `Android Jetpack`:

* Data Binding
* Lifecycles
* LiveData
* Navigation
* Paging
* Room
* ViewModel 

To work with REST API, I used `Retrofit 2` with `RxAndroid` and `Moshi` converter 

About images, I used `Glide` to load and cache images.

 

## Structure Project


This project implement the `MVVM Pattern`  

```

    com.tulc.product.discovery
    │
    ├── adapter
    │ 
    ├── api
    │
    ├── data
    │
    ├── extensions
    │    
    ├── models
    │
    ├── paging
    │
    ├── ui
    │
    ├── utils
    │
    ├── viewmodel
    │
    └── MainActivity.kt
    
```

* **adapter**: contains adapter for DataBindingAdapter, RecyclerView, and ViewPager 
* **api**: contains Retrofit Interface and Client instance that is used to call Rest Api 
* **data**: containers of Room Persistence like DAO, Repository and App database 
* **extensions**: contains Kotlin extension classes for short and clean code 
* **models**: contains `Model` class of `MVVM` pattern, that are data objects used within app 
* **paging**: define Paging State when used `Paging` of Android Jetpack and DataSource, DataSourceFactory 
* **ui**: contains `View` class of `MVVM` pattern, that contains Fragments, Dialog... 
* **utils**: contain any kind of Helper class or Utility class be used throughout the application
* **viewmodel**: contain `ViewModel` class of `MVVM` pattern and factory class to create ViewModel instance 
* **MainActivity.kt**: The application uses `Navigation` of Android Jetpack so usually there is only one Activity. 
The flow of screens in application is described in `res/navigation` direction  

## Test 

I have tested on:
* Samsung A8 2018 (Api 28)
* Samsung J7 Pro (Api 24)
* Mobiistar Zumbo J2 (Api 24)
* Huawei Nova 3e (Api 26)
* Pixel 2 - Virtual Device (Api 21)

## My opinion 

The application should not use pagination by page number.
 
When the user is scrolling down to see the product below, 
a few new products are added to the first. 
The subsequent data download is no longer accurate. It has duplicate data for subsequent.
 Application must filter into discarded duplicate data. 
 
 **Solution**: pagination by id of lastest downloaded product item.  

## Getting Started 
* Download and run the app 

## Authors

**LE Cong Tu**

Email: <tulc.dev@gmail.com> 

