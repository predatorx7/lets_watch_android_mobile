# Lets Watch
<img src="app/src/main/ic_launcher-playstore.png" alt="Lets Watch Icon" width="100"/>


[![pipeline status](https://github.com/predatorx7/lets_watch_android_mobile/badges/master/pipeline.svg)](https://github.com/predatorx7/lets_watch_android_mobile/-/commits/master)

## Description

Built as a demo, "Lets Watch" is a very minimal sample of an OTT app written in Native Android.

This repository contains source code for the android version of Lets Watch App. Majority of the code is written in Kotlin.

## Contributing

- Fork it (If you do not have push permissions)
- Create your feature branch (git checkout -b my-new-feature) or if you're a developer, you can create dev/<gitlab-account-username> as branch.
- Write Tests if applicable
- Run tests
- If everything looks good then commit your changes (git commit -m 'Add some feature')
- Push your branch (git push origin my-new-feature)
- Create a new "Pull Request" if you do not have push permissions

## com/magnificsoftware/letswatch package

### api/

Contains API service interfaces. Intention is to provide an implementation of these interfaces as a usable "Service" by Retrofit, util.LiveDataCallAdapterFactory, etc.

### commons/

Consists of common enums and constants used in the package.

### controller/

Contains AppVideoPlayerController for controlling the brightcove video view implemented by this app.

### data/

Contains Database access object and  other database connectivity/utility classes

### data_class/

Contains all types of basic data container classes. Immutable classes are preferred under this package.

#### data_class.plain/

Plain object classes which are preferably used for serialization, parcelization, etc. 

#### data_class.vo/

View objects which are preferably used for database rows.

### di/

Contains modules which have binding for interfaces, directly unprovidable classes for dependency injection framework (Hilt). Should include module for Api Service creation.

### navigator/

Contains classes and interfaces to allow navigation and data passing between fragments, activities, etc

### repository/

Contains classes which provides communications with resources that provides data

### ui/

Contains UI related classes

### ui.activity/

Contains activity classes

### ui.adapter/

Contains View adapters like RecyclerViewAdapter, ListViewAdapter, etc

### ui.fragments/

Contains androidx fragments, fragment screens

### util/

utility classes

### view_model/

Contains ViewModels for Android App LifeCycle aware components

## Development

1. The "Apply changes and restart activity" option in Android Studio won't work on a few fragments (EpisodesListFragment, ShowDetailsFragment, etc) as for such restart a zero argument fragment constructor is required.
1. Version name can only be manually updated by changing the name in file `version`. Build version number gets auto-generated and the last generated build number is set in the file `version-code`.
1. Ink ripple are drawn on surface foreground by adding this attribute-value = `android:foreground="?android:attr/selectableItemBackground"` in supported layout. (Check `res/layout-23`)

## Resources

..

## Information

### App version

Application's Version name and version code are determined by files version and version-code in the project's root directory. version name must only contain numbers in major.minor.min format, for example 0.1.0, 1.0.110, etc. The version-code files must only contain integers to represent build number.

