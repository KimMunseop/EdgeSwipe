# EdgeSwipe
Edge swipe for android



## [ How to use.. ] 
* 1. Create superclass of activity and add logic below.

<pre><code>
@Override
protected void onCreate(@Nullable Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  
  ActivityManager.getInstance().addActivityToStack(this);
}

@Override
public boolean dispatchTouchEvent(MotionEvent ev) {
  if(ActivityManager.getInstance().onEdgeSwipeEvent(ev)) {       
    return super.dispatchTouchEvent(ev);   
  }   
  return false;
}

@Override
protected void onDestroy() {
  super.onDestroy();
  
  ActivityManager.getInstance().removeActivityFromStack(this);
}
</code></pre>

* 2. Create a transparent theme (style.xml)


* 3. Apply a transparent theme to all activities (AndroidManifest.xml)




