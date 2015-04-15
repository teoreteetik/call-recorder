# call-recorder
Phone call recorder based on the Twilio platform.

The application enables to record and play back phone calls without a dedicated app. 

####Requirements  
A [Twilio](http://www.twilio.com) account with a voice- and message-enabled phone number.

####Example use case
Let's say X wants to call Y and wants to record the call. X does not have a smartphone or (doesn't want to / can't) download a dedicated app for recording calls.

* X calls the voice-enabled Twilio number.
* X is prompted to enter the Y's phone number on the keypad and ending the phone number with an asterisk (*).
* The call is forwarded to Y.
* When Y picks up, s/he are notified that the call will be recorded, after which s/he will have 5 seconds to decline before the call is connected.
* If Y accepts the call and the call has ended, a text message with the URL of the recording is sent to X.
* The recording can be played back by either:
  * Navigating to the URL directly
  * Forwarding the text message to the message-enabled Twilio number. After sending the text message, the sender will recevie a call and the recording will be played back.


####Building (Java 8 required)
* Fill in the configuration parameters in [Config.java](https://github.com/teoreteetik/call-recorder/blob/master/src/main/java/ee/teoreteetik/call_recorder/Config.java).
* Build the war using `gradlew war`.

