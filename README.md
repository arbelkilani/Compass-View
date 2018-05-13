# Compass View
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/vlad1m1r990/Lemniscate/blob/master/LICENSE)
[![](https://jitpack.io/v/arbelkilani/BiColored-Progress.svg)](https://jitpack.io/#arbelkilani/Compass-View)
[![API](https://img.shields.io/badge/API-19%2B-green.svg?style=flat)]()

Compass view with full options style. 

![compass view](https://i.makeagif.com/media/5-13-2018/LtaULi.gif)

## Setup

Add to your module's build.gradle:

```xml
allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
```
and to your app build.gradle:

```xml
dependencies {
  implementation 'com.github.arbelkilani:Compass-View:v1.1'
}
```

## Usage

```xml
<edu.arbelkilani.compass.Compass
    android:id="@+id/compass_1"
    android:layout_width="200dp"
    android:layout_height="200dp"
    android:layout_alignParentStart="true"
    android:layout_centerVertical="true"
    app:degree_color="@color/blue"
    app:degrees_step="5"
    app:needle="@drawable/ic_needle_2"
    app:orientation_labels_color="@color/red"
    app:show_degree_value="false"
    app:show_orientation_labels="true"
    app:show_border="false"
    app:degree_value_color="@color/colorAccent"
    app:border_color="@color/red"/>
```

###### Params available in all views:

* **degree_color** (color) - color of compass degree lines
* **degrees_step** (int) - must be > 0 and < 360 . 
	enable user to change style by changig degree shown count.   
	example : for degrees_step = 90 => only for degrees will be shown.  

* **needle** (drawable) - the animated needle for the compass

* **show_orientation_labels** (boolean) - enable to show or hide orientation labels N, E, S, W.
* **orientation_labels_color** (color) - Orientation labels color, N, E, S, W

* **show_degree_value** (boolean) - enable to show or hide degrees value inside the compass item.
* **degree_value_color** (color) - change degrees value color.

* **show_border** (boolean) - enable to show or hide an outer circle for the compass.
* **border_color** (color) - change outer circle value color.


###### Compass Listener:
```java

Compass compass = findViewById(R.id.compass_view);
compass.setListener(new CompassListener() {
    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d(TAG, "onSensorChanged : " + event);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(TAG, "onAccuracyChanged : sensor : " + sensor);
        Log.d(TAG, "onAccuracyChanged : accuracy : " + accuracy);
    }
});
```

## License

    Copyright 2018 Belkilani Ahmed Radhouane

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
