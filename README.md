# PermissionDemo
##这是我的一个关于Android6.0 API23新出的敏感权限申请的Demo
###1.关于Android权限
对于6.0以下的权限及在安装的时候，根据权限声明产生一个权限列表，用户只有在同意之后才能完成app的安装，
造成了我们想要使用某个app，就要默默忍受其一些不必要的权限（比如是个app都要访问通讯录、短信等）。
而在6.0以后，我们可以直接安装，当app需要我们授予不恰当的权限的时候，我们可以予以拒绝
（比如：单机斗地主，请求访问任何权限，我都是不同意的）。当然你也可以在设置界面对每个app的权限进行查看，
以及对单个权限进行授权或者解除授权。

新的权限机制更好的保护了用户的隐私，Google将权限分为两类，一类是Normal Permissions，
这类权限一般不涉及用户隐私，是不需要用户进行授权的，比如手机震动、访问网络等；
另一类是Dangerous Permission，一般是涉及到用户隐私的，需要用户进行授权，比如读取sdcard、访问通讯录等。

* Normal Permissions如下
```javascript
ACCESS_LOCATION_EXTRA_COMMANDS
ACCESS_NETWORK_STATE
ACCESS_NOTIFICATION_POLICY
ACCESS_WIFI_STATE
BLUETOOTH
BLUETOOTH_ADMIN
BROADCAST_STICKY
CHANGE_NETWORK_STATE
CHANGE_WIFI_MULTICAST_STATE
CHANGE_WIFI_STATE
DISABLE_KEYGUARD
EXPAND_STATUS_BAR
GET_PACKAGE_SIZE
INSTALL_SHORTCUT
INTERNET
KILL_BACKGROUND_PROCESSES
MODIFY_AUDIO_SETTINGS
NFC
READ_SYNC_SETTINGS
READ_SYNC_STATS
RECEIVE_BOOT_COMPLETED
REORDER_TASKS
REQUEST_INSTALL_PACKAGES
SET_ALARM
SET_TIME_ZONE
SET_WALLPAPER
SET_WALLPAPER_HINTS
TRANSMIT_IR
UNINSTALL_SHORTCUT
USE_FINGERPRINT
VIBRATE
WAKE_LOCK
WRITE_SYNC_SETTINGS
```
* Dangerous Permissions:
```javascript
group:android.permission-group.CONTACTS
  permission:android.permission.WRITE_CONTACTS
  permission:android.permission.GET_ACCOUNTS
  permission:android.permission.READ_CONTACTS

group:android.permission-group.PHONE
  permission:android.permission.READ_CALL_LOG
  permission:android.permission.READ_PHONE_STATE
  permission:android.permission.CALL_PHONE
  permission:android.permission.WRITE_CALL_LOG
  permission:android.permission.USE_SIP
  permission:android.permission.PROCESS_OUTGOING_CALLS
  permission:com.android.voicemail.permission.ADD_VOICEMAIL

group:android.permission-group.CALENDAR
  permission:android.permission.READ_CALENDAR
  permission:android.permission.WRITE_CALENDAR

group:android.permission-group.CAMERA
  permission:android.permission.CAMERA

group:android.permission-group.SENSORS
  permission:android.permission.BODY_SENSORS

group:android.permission-group.LOCATION
  permission:android.permission.ACCESS_FINE_LOCATION
  permission:android.permission.ACCESS_COARSE_LOCATION

group:android.permission-group.STORAGE
  permission:android.permission.READ_EXTERNAL_STORAGE
  permission:android.permission.WRITE_EXTERNAL_STORAGE

group:android.permission-group.MICROPHONE
  permission:android.permission.RECORD_AUDIO

group:android.permission-group.SMS
  permission:android.permission.READ_SMS
  permission:android.permission.RECEIVE_WAP_PUSH
  permission:android.permission.RECEIVE_MMS
  permission:android.permission.RECEIVE_SMS
  permission:android.permission.SEND_SMS
  permission:android.permission.READ_CELL_BROADCASTS
```
###2.申请权限的API
* 1.首先需要在AndroidManifest中添加需要的权限
* 2.检查版本是否大于Android6.0或者API23
```javascript
 //安卓6.0,API23以后  应用在使用敏感权限时,需要向用户申请权限,才能继续使用敏感权限
 //如果API版本高于23,则往下走
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
```
* 3.检查权限
```javascript
//判断是否有这个权限
 if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) !=PackageManager.PERMISSION_GRANTED)
```
PackageManager.PERMISSION_DENIED表示没有权限,PackageManager.PERMISSION_GRANTED表示有权限,
ActivityCompat.checkSelfPermission用来获得权限是否拥有

* 4.申请权限
```javascript
//申请权限
//第一个参数是需要申请的所有权限,第二个参数是请求码
requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);
```

* 5.处理申请权限的返回码
```javascript
 /**
     * 申请权限是系统的自行调用的异步申请,申请结果需要重写onRequestPermissionsResult,在此判断权限是否申请成功
     * @param requestCode  请求码
     * @param permissions  申请的所有权限
     * @param grantResults  申请的权限返回的结果码  0为申请成功  -1为申请成功
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        System.out.println(requestCode + ":" + Arrays.toString(permissions) + ":" + Arrays.toString(grantResults));
        switch (requestCode) {
            case mRequestCode: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }

    }
```
*6.还有一个有意思的API
这个API主要用于给用户一个申请权限的解释，该方法只有在用户在上一次已经拒绝过你的这个权限申请。也就是说，用户已经拒绝一次了，你又弹个授权框，你需要给用户一个解释，为什么要授权，则使用该方法。
```javascript
//shouldShowRequestPermissionRationale这个方法,应用安装后第一次调用时,返回false,
//当在第一次申请权限时,用户拒绝后,下次申请时,这个方法返回true,此时可以提示用户为什么需要这个权限,
// 如果用户一直拒绝且没有点击不在提示时,这个方法仍然返回true
 if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
        Log.i(TAG, "拒绝申请后显示 ");
        Toast.makeText(MainActivity.this, "shouldShowRequestPermissionRationale", Toast.LENGTH_SHORT).show();
     } else {
      //当用户在第二次申请时,并再次拒绝并点击不再提示时,这个方法返回false
        Log.i(TAG, "不在提示后显示显示 ");
        Toast.makeText(MainActivity.this, "shouldShowRequestPermissionRationale", Toast.LENGTH_SHORT).show();
  }
```
