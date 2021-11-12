WFMA
======
Who Finish My Activity?

Android 的 Handler 机制会导致调用栈断裂,就无法有效追踪导致 Activity.onDestroy() 被执行的原因.
WFMA 可以绕过 Handler 机制,直达导致 Activity.onDestroy() 被执行的原因.






