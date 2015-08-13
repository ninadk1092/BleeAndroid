# BleeAndroid

Blee works over WiFi.

1. User opens app for the first time, and is led to an in-app
walkthrough
2. At the end of it, user asked :
   Do you have WiFi ? / Would you like to use your cellular
   data ?
   1. **WiFi**
	  1. Ask to enter WiFi password and SSID
   2. **Cellular data**
	  1. Check if data connectivity exists
	  2. Generates/fetches SSID/passwd
3. Press a combination of buttons on the ring.
   1. Ring goes to access point mode with preset SSID
   2. Phone connects to the ring and passes the SSID/passwd
4. Phone disconnects :
   1. **WiFi**
	  1. Phone connects to the WiFi 
   2. **Cellular data**
	  1. Phone creates a hotspot with previously generated SSID/password
5. Check internet connectivity
6. Open app to settings/vibration pattern/etc activity
