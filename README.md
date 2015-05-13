#Introduction

`AzulStackProf` is a command line application to parse Azul RTPM XML files. It is useful only for company using Azul Systems. 

This information comes from Azul website:
 "RTPM is a diagnostics and tuning tool instrumented into the Azul VM. `[...]` RTPM is always-on and has zero performance overhead".
Zero performance overhead makes me think we can have a rudimentary profiling tool running in production for free. Great!

Let's start...

#How does it work?

Like HProf CPU profiling, RTPM periodically samples the stack of all running threads. `AzulStackProf` simply parses stacks to detect the most frequently active methods. We can expect to find easily the application hotspots by analyzing this top N most active methods.

#Releases

* [AzulStackProf-1.0-bin.zip](http://code.google.com/p/azulstackprof/downloads/detail?name=AzulStackProf-1.0-bin.zip)

#Getting started

* Download `AzulStackProf-<major>.<inc>-bin.zip` and unzip it
* cd `AzulStackProf-<major>.<inc>-bin/bin`
* For Linux
  * `chmod 775 start.sh`
  * `/start.sh` [file|dir]
* For Windows
  * `/start.bat` [file|dir]


`file`: Specify stack file path (e.g: `C:/temp/rtpm_11212020/STACK.1.xml`). The application will parse the indicated file only.
or
`dir`: Specify folder path (e.g: `C:/temp/rtpm_11212020`). The application will parse all stack files inside the indicated folder.

#Results

##Thread state report

```
ID	R	S	IO	WOM	AM	AR	WWSM	U
1	0	13	4	0	0	0	4	0
2	0	13	4	0	0	0	4	0
3	1	13	4	0	0	0	4	0
4	0	13	4	0	0	0	4	0
5	0	13	5	0	0	0	4	0
6	1	12	4	0	0	0	4	0
7	0	13	4	0	0	0	4	0
8	0	13	4	0	0	0	4	0
9	0	13	4	0	0	0	4	0
10	0	13	4	0	0	0	4	0
11	0	13	4	0	0	0	4	0
12	0	13	5	0	0	0	4	0
13	0	13	4	0	0	0	4	0
14	0	13	5	0	0	0	4	0
15	0	13	4	0	0	0	4	0
16	0	13	5	0	0	0	4	0
17	0	13	4	0	0	0	4	0
18	0	13	4	0	0	0	4	0
19	0	13	4	0	0	0	4	0
20	0	13	4	0	0	0	4	0
21	0	13	4	0	0	0	4	0
22	0	12	5	0	0	0	4	0
23	0	13	4	0	0	0	4	0
24	1	13	4	0	0	0	4	0
25	1	13	4	0	0	0	4	0
26	0	13	4	0	0	0	4	0
27	0	13	4	0	0	0	4	0
28	0	13	4	0	0	0	4	0
29	1	13	4	0	0	0	4	0
30	1	13	4	0	0	0	4	0
```

Then, you can copy this result into Excel using the following spreadsheet [AzulStackProf.xls](http://azulstackprof.googlecode.com/svn/trunk/AzulStackProf/media/AzulStackProf-Thread%20state%20chart.xls) to generate a chart.

![AzulStackProf.PNG](http://azulstackprof.googlecode.com/svn/trunk/AzulStackProf/media/AzulStackProf.PNG)


##Hotspot method report

Below is a result example (it has helped to solve a real performance issue): 

```
***Azul Stack Prof result***
rank    method  count
1       7457    java.lang.Object.wait(Object.java:429)
2       5682    weblogic.kernel.ExecuteThread.run(ExecuteThread.java:174)
3       5682    weblogic.kernel.ExecuteThread.waitForRequest(ExecuteThread.java:154)
4       2845    weblogic.servlet.internal.FilterChainImpl.doFilter(FilterChainImpl.java:27)
5       2641    weblogic.kernel.ExecuteThread.execute(ExecuteThread.java:224)
6       2636    weblogic.kernel.ExecuteThread.run(ExecuteThread.java:183)
7       2630    weblogic.security.acl.internal.AuthenticatedSubject.doAs(AuthenticatedSubject.java:321)
8       2630    weblogic.security.service.SecurityManager.runAs(SecurityManager.java:121)
9       2630    weblogic.servlet.internal.ServletRequestImpl.execute(ServletRequestImpl.java:2773)
10      2630    weblogic.servlet.internal.WebAppServletContext$ServletInvocationAction.run(WebAppServletContext.java:7053)
11      2630    weblogic.servlet.internal.WebAppServletContext.invokeServlet(WebAppServletContext.java:3902)
12      2054    com.bluemartini.html.HTMLFilter.doFilter(HTMLFilter.java:316)
13      1710    com.bluemartini.core.Mutex.lock(Mutex.java:42)
14      1710    com.bluemartini.core.Mutex.lockInternal(Mutex.java:91)
15      1710    com.bluemartini.html.StandardRequestHandler.init(StandardRequestHandler.java:315)
16      1710    com.bluemartini.lock.AbstractLockHandler$Protector.lock(AbstractLockHandler.java:328)
17      1710    com.bluemartini.lock.AbstractLockHandler.lock(AbstractLockHandler.java:97)
18      1710    com.bluemartini.lock.AbstractLockHandler.lockInternal(AbstractLockHandler.java:133)
19      1710    com.bluemartini.lock.LocalLockHandler.acquireLock(LocalLockHandler.java:54)
20      1367    com.bluemartini.client.BusinessActionClient.executeBusinessAction(BusinessActionClient.java:283)
21      1367    com.bluemartini.client.BusinessActionClient.executeBusinessActionInternal(BusinessActionClient.java:761)
22      1367    com.bluemartini.server.BusinessActionServlet.execute(BusinessActionServlet.java:48)
23      1358    com.bluemartini.server.BusinessActionServlet.executeInternal(BusinessActionServlet.java:215)
24      1358    com.bluemartini.server.BusinessActionServlet.executeInternal(BusinessActionServlet.java:416)
25      1358    com.bluemartini.server.BusinessActionServlet.executeOnce(BusinessActionServlet.java:360)
26      864     com.bluemartini.client.BusinessActionClient.executeBusinessAction(BusinessActionClient.java:190)
27      864     com.bluemartini.client.BusinessActionClient.executeBusinessAction(BusinessActionClient.java:257)
28      812     com.bluemartini.htmlapp.HTMLOrderUtil.updateCartHeader(HTMLOrderUtil.java:1606)
29      810     com.bluemartini.htmlapp.HTMLOrderUtil.updateCartHeader(HTMLOrderUtil.java:1345)
30      810     com.bluemartini.htmlapp.HTMLPromotionUtil.applyAdditionalProducts(HTMLPromotionUtil.java:2557)
```

#Rule parsing with Drools
We use Drools engine to filter the elements to parse. For example, you may want to filter RTPM files by size or threads by name (quite useful to keep only http threads).

##Filter by header
If you want to filter files to parse, edit ```AzulStackProf-Header.java.drl```.
Here are some examples:

* Filter by file size
```
rule "Filter Stack file by file size"
  dialect "java"	
  when
      header:Header(fileSize > 300)
  then 
      header.setContinueParsing(true);
end
```

* Filter by file index
```
rule "Filter Stack file by file index"
  dialect "java"	
  when
      header:Header(fileIndex > 100)
  then 
      header.setContinueParsing(true);
end
```


##Filter by thread
If you want to filter threads to parse, edit `AzulStackProf-Thread.java.drl`. <br/>

Here are some examples:

* Filter by state, here we parse only running thread
```
rule "Filter Running threads"	
  when
      $thread : Thread(state == Thread.STATE_RUNNING)
  then 
      $thread.setContinueParsing(true);
end
```

* Filter by name, here we parse only Welogic Kernel Default threads:
```
rule "Filter Welogic Kernel Default threads"
	
  when
      $thread : Thread() eval(
      		startsWith($thread.getName(), "ExecuteThread: '")
      		&& endsWith($thread.getName(), "' for queue: 'weblogic.kernel.Default'")
      	)
  then 
      $thread.setContinueParsing(true);
end
```
