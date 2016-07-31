# CrashCanary

A __DESPERTATELY SIMPLE__ android library which will write your app's crash stacktrace to `SDCard/Android/data/YOUR_PACKAGENAME/crashLog.txt`

Yes, you are right. The name is totally a copycat of [LeakCanary](https://github.com/square/leakcanary).

Download
--------
Will upload to `jCenter` after some inspections, so far you only can download the souce code to help me improve it.

Usage
--------
In your Application class:
```java
public class ExampleApplication extends Application {

  @Override public void onCreate() {
    super.onCreate();
    CrashCanary.install(this);
  }
}
```


